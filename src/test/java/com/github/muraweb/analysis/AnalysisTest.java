package com.github.muraweb.analysis;

import com.github.muraweb.analysis.AnalysisController;
import com.github.muraweb.analysis.AnalysisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(AnalysisController.class)   // Mock server
class AnalysisTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalysisService analysisService;

    @Test
    public void getAnalysisTest() throws Exception {
        RequestBuilder getAnalysisRequest = MockMvcRequestBuilders.get("/submit");
        MvcResult result = mockMvc.perform(getAnalysisRequest).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void postAnalysisTest() throws Exception {
        RequestBuilder getAnalysisRequest = MockMvcRequestBuilders
                .post("/submit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("gitRepo","https://github.com/OpenFeign/feign")
                .param("singleModule", "false")
                .param("module", "core")
                .param("CK", "true")
                .param("CC", "true")
                .param("USG", "true")
                .param("H", "true")
                .param("LC", "true")
                .param("IMP", "false")
                .param("optimalWeights", "false");
        MvcResult result = mockMvc.perform(getAnalysisRequest).andReturn();
        assertEquals(302, result.getResponse().getStatus());    // redirect to homepage
    }

}
