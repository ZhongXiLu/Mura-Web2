package com.github.muraweb.analysis;

import ch.qos.logback.classic.BasicConfigurator;
import ch.qos.logback.classic.LoggerContext;
import com.github.muraweb.Util;
import core.MutantExporter;
import core.RankedMutant;
import core.ReportGenerator;
import core.rankers.ck.CKRanker;
import core.rankers.complexity.ComplexityRanker;
import core.rankers.history.HistoryRanker;
import core.rankers.impact.CoverageRanker;
import core.rankers.impact.ImpactRanker;
import core.rankers.usage.UsageRanker;
import lumutator.Configuration;
import lumutator.Mutant;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import study.ConfigurationSetup;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;

import static pitest.Parser.getMutantsWithMutantType;

@Service
public class AnalysisService {

    private AnalysisRepository analysisRepository;

    /**
     * Name of the directory to temporarily store the repositories.
     */
    final static String reposDir = "repos";

    /**
     * Constructor.
     *
     * @param analysisRepository The analysis repository.
     */
    @Autowired
    public AnalysisService(AnalysisRepository analysisRepository) {
        this.analysisRepository = analysisRepository;
    }

    /**
     * Start an analysis in a separate thread.
     *
     * @param analysisForm The form for the analysis, filled in by the user.
     * @param outputDir    The output directory where the results will be saved to.
     */
    @Async
    public void startAnalysis(AnalysisForm analysisForm, String outputDir) {
        Analysis analysis = new Analysis();
        analysis.setGitRepo(analysisForm.getGitRepo());
        analysis.setRepoName(Util.getRepoName(analysisForm.getGitRepo()));
        analysisRepository.save(analysis);
        final File projectDir = new File(reposDir + File.separator + analysis.getRepoName());

        try {
            // Clone project
            if (!projectDir.exists()) {
                projectDir.mkdirs();
                Git.cloneRepository()
                        .setURI(analysisForm.getGitRepo())
                        .setDirectory(projectDir)
                        .call();
            }

            // Initial configuration for PITest
            Configuration config = Configuration.getInstance();
            Configuration.getInstance().initialize("data/config.xml");
            config.set("projectDir", projectDir.getCanonicalPath());
            ConfigurationSetup.addPITest(new File(config.get("projectDir") + "/pom.xml"));
            if (!analysisForm.isSingleModule()) config.set("sourcePath", analysisForm.getModule() + "/src/main/java/");

            // Run mutation testing with PITest
            ProcessBuilder processBuilder = null;
            if (!analysisForm.isSingleModule()) {
                processBuilder = new ProcessBuilder(
                        "mvn", "clean", "test", "-pl", analysisForm.getModule(),
                        "-Dfeatures=+EXPORT", "org.pitest:pitest-maven:mutationCoverage"
                );
            } else {
                processBuilder = new ProcessBuilder(
                        "mvn", "clean", "test",
                        "-Dfeatures=+EXPORT", "org.pitest:pitest-maven:mutationCoverage"
                );
            }
            processBuilder.directory(new File(config.get("projectDir")));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((reader.readLine()) != null) {
            }  // read output from buffer, otherwise buffer might get full
            process.waitFor();
            reader.close();
            if (process.exitValue() != 0) {
                throw new RuntimeException("One of the tests failed while applying mutation analysis");
            }

            // Parse PITest mutants
            List<Mutant> mutants = getMutantsWithMutantType(
                    Paths.get(config.get("projectDir"),
                            analysisForm.isSingleModule() ? "" : analysisForm.getModule(),
                            "target", "pit-reports").toString(),
                    true, RankedMutant.class
            );

            // Setup configuration for MuRa
            if (!analysisForm.isSingleModule()) {
                // Append module to paths
                config.set("classFiles", projectDir + "/" + analysisForm.getModule() + "/target/classes");
                config.set("classPath", projectDir + "/" + analysisForm.getModule() + "/target/classes/:" + projectDir + "/" + analysisForm.getModule() + "/target/test-classes/");
                config.set("sourcePath", projectDir + "/" + analysisForm.getModule() + "/src/main/java/");
                config.set("testDir", projectDir + "/" + analysisForm.getModule() + "/src/test/java/");
            } else {
                config.set("classFiles", projectDir + "/target/classes");
                config.set("classPath", projectDir + "/target/classes/:" + projectDir + "/target/test-classes/");
                config.set("sourcePath", projectDir + "/src/main/java/");
                config.set("testDir", projectDir + "/src/test/java/");
            }
            ConfigurationSetup.addClassPath(config, analysisForm.isSingleModule() ? "" : analysisForm.getModule());
            BasicConfigurator basicConfigurator = new BasicConfigurator();
            basicConfigurator.configure(new LoggerContext());

            // Call MuRa rankers
            if (analysisForm.isCK()) CKRanker.rankCK(mutants, config.get("sourcePath"));
            if (analysisForm.isCC()) ComplexityRanker.rank(mutants, config.get("classFiles"));
            if (analysisForm.isUSG()) UsageRanker.rank(mutants, config.get("classFiles"));
            if (analysisForm.isH() && new File(config.get("projectDir") + File.separator + ".git").exists()) {
                HistoryRanker.rank(mutants);
            }
            if (analysisForm.isLC()) CoverageRanker.rank(mutants, config.get("classFiles"));
            if (analysisForm.isIMP()) ImpactRanker.rank(mutants, config.get("classFiles"));

            // Create output dir
            final File outDir = new File(outputDir + File.separator + analysis.getRepoName());
            if (!outDir.exists()) {
                outDir.mkdirs();
            }

            // Generate the report
            ReportGenerator.generateReport(mutants, outDir + "/report.html");
            analysis.setReport("report.html");

            // Apply optimal weights if applicable
            if (analysisForm.isOptimalWeights()) Util.applyOptimalWeights(outDir + "/report.html");

            // Also export the mutants with their scores
            MutantExporter.exportMutantsToCSV(mutants, outDir + "/mutants.csv");
            analysis.setMutants("mutants.csv");

            analysis.setSuccessful(true);

        } catch (Exception e) {
            e.printStackTrace();
            analysis.setSuccessful(false);
            analysis.setErrorMessage(e.getMessage());

        } finally {
            analysis.setFinished(true);
            analysisRepository.save(analysis);

            try {
                FileUtils.deleteDirectory(projectDir);
            } catch (IOException e) {

            }
        }
    }

    /**
     * Get the analysis by its repository name.
     *
     * @param repoName The name of the repository that was analyzed.
     * @return The analysis.
     */
    public Analysis getAnalysisByRepoName(String repoName) {
        for (Analysis analysis : analysisRepository.findAll()) {
            if (analysis.getRepoName().equals(repoName)) {
                return analysis;
            }
        }
        return null;
    }

    /**
     * Get all the analyses.
     *
     * @return All the analyses.
     */
    public List<Analysis> getAllAnalyses() {
        return (List<Analysis>) analysisRepository.findAll();
    }

}
