package com.concert.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ConcertControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetConcertsPublicApi() throws Exception {
        mockMvc.perform(get("/api/v1/concerts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        System.out.println("====================================================");
        System.out.println(">>> PUBLIC CONCERT API INTEGRATION TEST PASSED! <<<");
        System.out.println("====================================================");
    }

    @Test
    void testAuthLoginWithSeededCustomer() throws Exception {
        String loginJson = "{\"usernameOrEmail\":\"customer\",\"password\":\"customer123\"}";

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.role").value("CUSTOMER"));

        System.out.println("====================================================");
        System.out.println(">>> AUTH LOGIN API INTEGRATION TEST PASSED! <<<");
        System.out.println("====================================================");
    }

    @Test
    void testAuthLoginWithSeededAdmin() throws Exception {
        String loginJson = "{\"usernameOrEmail\":\"admin\",\"password\":\"admin123\"}";

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.role").value("ADMIN"));

        System.out.println("====================================================");
        System.out.println(">>> ADMIN LOGIN API INTEGRATION TEST PASSED! <<<");
        System.out.println("====================================================");
    }
}
