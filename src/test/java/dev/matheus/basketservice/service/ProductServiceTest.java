package dev.matheus.basketservice.service;

import dev.matheus.basketservice.client.PlatziStoreClient;
import dev.matheus.basketservice.client.response.PlatziProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private PlatziStoreClient platziStoreClient;

    @InjectMocks
    private ProductService productService;

    @Test
    void getProductByIdShouldReturnProduct() {
        PlatziProductResponse mockResponse = new PlatziProductResponse(1L, "Console", new BigDecimal("2000"), "desc", java.util.List.of("img"));
        when(platziStoreClient.getProductById(1L)).thenReturn(mockResponse);

        PlatziProductResponse result = productService.getProductById(1L);

        assertEquals("Console", result.title());
        verify(platziStoreClient, times(1)).getProductById(1L);
    }
}