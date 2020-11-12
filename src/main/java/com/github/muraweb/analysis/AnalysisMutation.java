package com.github.muraweb.analysis;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnalysisMutation implements GraphQLMutationResolver {

    @Autowired
    private AnalysisService analysisService;

    /**
     * Directory to store the results of the analysis.
     */
    final static String outputDir = "results";

    /**
     * Start an analysis.
     *
     * @return Message.
     */
    public String submit(AnalysisForm analysisForm) {
        analysisService.startAnalysis(analysisForm, outputDir);
        return "Successfully started an analysis";
    }

}
