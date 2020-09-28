package com.github.muraweb.analysis;

import lumutator.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class AnalysisServiceTest {

    @Mock
    private AnalysisRepository analysisRepository;

    private AnalysisService analysisService;

    private static final String[] repos = {"slub/urnlib", "OpenFeign/feign"};

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        analysisService = new AnalysisService(analysisRepository);

        // Setup mocks
        List<Analysis> analyses = new ArrayList<>();
        for (final String repo : repos) {
            Analysis analysis = new Analysis();
            analysis.setRepoName(repo);
            analyses.add(analysis);
        }
        when(analysisRepository.findAll()).thenReturn(analyses);
    }

    @Test
    public void startAnalysisTest() throws IOException {

        // Provide a custom config file for this test
        Configuration config = spy(Configuration.class);
        doNothing().when(config).initialize(anyString());

        String outDir = Files.createTempDirectory("out").toString();

        for (final String repo : repos) {
            Configuration.getInstance().initialize(getClass().getClassLoader().getResource("config.xml").getFile());

            AnalysisForm analysisForm = new AnalysisForm();
            analysisForm.setGitRepo("https://github.com/" + repo);
            if (repo.equals("slub/urnlib")) {
                analysisForm.setSingleModule(true);
                analysisForm.setCK(false);
                analysisForm.setCC(false);
                analysisForm.setUSG(false);
                analysisForm.setH(true);
                analysisForm.setLC(true);
                analysisForm.setIMP(true);
                analysisForm.setOptimalWeights(false);
            } else {
                analysisForm.setSingleModule(false);
                analysisForm.setModule("core");
                analysisForm.setCK(true);
                analysisForm.setCC(true);
                analysisForm.setUSG(true);
                analysisForm.setH(false);
                analysisForm.setLC(false);
                analysisForm.setIMP(false);
                analysisForm.setOptimalWeights(true);
            }

            analysisService.startAnalysis(analysisForm, outDir);

            // Check if the necessary files are generated
            assertTrue(new File(outDir + "/" + repo + "/report.html").exists());
            assertTrue(new File(outDir + "/" + repo + "/mutants.csv").exists());

            // Check if repo is deleted afterwards
            assertFalse(new File("repos/" + repo).exists());
        }
    }

    @Test
    public void getAnalysesTest() {
        assertEquals(2, analysisService.getAllAnalyses().size());
        assertEquals("slub/urnlib", analysisService.getAnalysisByRepoName("slub/urnlib").getRepoName());
        assertEquals("OpenFeign/feign", analysisService.getAnalysisByRepoName("OpenFeign/feign").getRepoName());
        assertNull(analysisService.getAnalysisByRepoName("ZhongXiLu/Mura-Web"));
    }

}