package com.github.muraweb.analysis;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.muraweb.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Collections;
import java.util.List;

@CrossOrigin
@Controller
public class AnalysisController {

    private AnalysisService analysisService;

    /**
     * Directory to store the results of the analysis.
     */
    final static String outputDir = "results";

    /**
     * Constructor.
     *
     * @param analysisService The analysis service.
     */
    @Autowired
    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    /**
     * Homepage.
     */
    @GetMapping("/")
    public String index() {
        return "index2";    // "index" is already taken by a dependency...
    }

    /**
     * Get an analysis form.
     *
     * @return The analysis form.
     */
    @GetMapping("/submit")
    public String getForm() {
        return "analysisForm";
    }

    /**
     * Create a new request for a repository to be analyzed by MuRa.
     */
    @PostMapping("/submit")
    public ResponseEntity<Object> postForm(@RequestBody AnalysisForm analysisForm) {
        analysisService.startAnalysis(analysisForm, outputDir);
        return ResponseEntity.ok().build();
    }

    /**
     * Get all the analyses.
     */
    @RequestMapping(value = "/analysis", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Analysis> getAnalyses() {
        List<Analysis> analyses = analysisService.getAllAnalyses();
        Collections.reverse(analyses);
        return analyses;
    }

    /**
     * Get the details of an analysis.
     *
     * @param repoName The name of the repository that was analyzed.
     * @return The analysis.
     */
    @GetMapping("/analysis/{repoOwner}/{repoName}")
    @Deprecated
    public String getAnalysis(Model model, @PathVariable String repoOwner, @PathVariable String repoName) {
        model.addAttribute("analysis", analysisService.getAnalysisByRepoName(repoOwner + "/" + repoName));
        return "analysis";
    }

    /**
     * Retrieve a file from the server.
     */
    @GetMapping("/analysis/{repoOwner}/{repoName}/{filename:.+}")
    @ResponseBody
    public FileSystemResource getFile(
            HttpServletResponse response,
            @PathVariable String repoOwner, @PathVariable String repoName, @PathVariable String filename
    ) {
        final File file = new File(outputDir + "/" + repoOwner + "/" + repoName + "/" + filename);
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        return new FileSystemResource(file);
    }

}
