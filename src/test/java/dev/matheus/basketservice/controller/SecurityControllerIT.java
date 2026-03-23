package dev.matheus.basketservice.controller;

import dev.matheus.basketservice.client.PlatziStoreClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.cache.type=none")
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = true)
class SecurityControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlatziStoreClient platziStoreClient;

    @Test
    @DisplayName("Deve retornar 401 ao acessar endpoint protegido sem autenticação")
    void shouldReturn401WhenAccessingProtectedEndpointWithoutAuth() throws Exception {
        mockMvc.perform(get("/basket"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 200 ao acessar endpoint público sem autenticação")
    void shouldReturn200WhenAccessingPublicEndpointWithoutAuth() throws Exception {
        when(platziStoreClient.getAllProducts()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }
}
