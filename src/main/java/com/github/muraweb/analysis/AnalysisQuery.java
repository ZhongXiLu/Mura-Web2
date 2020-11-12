package com.github.muraweb.analysis;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnalysisQuery implements GraphQLQueryResolver {

    @Autowired
    private AnalysisService analysisService;

    /**
     * Get all the analyses.
     *
     * @return All the analyses.
     */
    public List<Analysis> getAnalyses() {
        return analysisService.getAllAnalyses();
    }

    /**
     * Find an analysis by its GitHub repository name.
     *
     * @param repoName Name of the GitHub repository of the analysis.
     * @return The analysis.
     */
    public Analysis getAnalysis(String repoName) {
        return analysisService.getAnalysisByRepoName(repoName);
    }

}
