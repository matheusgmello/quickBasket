package dev.matheus.basketservice.controller;

import dev.matheus.basketservice.client.PlatziStoreClient;
import dev.matheus.basketservice.client.response.PlatziProductResponse;
import dev.matheus.basketservice.entity.Basket;
import dev.matheus.basketservice.entity.Status;
import dev.matheus.basketservice.repository.BasketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.cache.type=none", "spring.data.mongodb.uri=mongodb://localhost:27017/test"})
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc(addFilters = true)
class BasketControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlatziStoreClient platziStoreClient;

    @MockitoBean
    private BasketRepository basketRepository;

    @Test
    @DisplayName("Deve criar uma nova cesta com sucesso e retornar 201")
    void shouldCreateBasketWithSuccess() throws Exception {

        PlatziProductResponse productResponse = new PlatziProductResponse(
                1L, 
                "Teclado Mecânico", 
                new BigDecimal("350.00"),
                "Descrição do teclado",
                java.util.List.of("http://image.com")
        );
        when(platziStoreClient.getProductById(anyLong())).thenReturn(productResponse);
        
        when(basketRepository.findByClientAndStatus(anyLong(), any(Status.class))).thenReturn(Optional.empty());
        when(basketRepository.save(any(Basket.class))).thenAnswer(invocation -> {
            Basket b = invocation.getArgument(0);
            b.setId("test-id");
            return b;
        });

        String basketRequestJson = """
                {
                    "clientId": "123",
                    "products": [
                        { "id": 1, "quantity": 2 }
                    ]
                }
                """;

        mockMvc.perform(post("/basket")
                        .with(user("user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(basketRequestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.client").value("123"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.totalPrice").value(700.00));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar uma cesta inexistente")
    void shouldReturn404WhenBasketNotFound() throws Exception {
        when(basketRepository.findById(anyString())).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/basket/id-que-nao-existe")
                        .with(user("user")))
                .andExpect(status().isNotFound());
    }
}