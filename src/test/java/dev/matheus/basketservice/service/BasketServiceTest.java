package dev.matheus.basketservice.service;

import dev.matheus.basketservice.client.response.PlatziProductResponse;
import dev.matheus.basketservice.controller.request.BasketRequest;
import dev.matheus.basketservice.controller.request.PaymentRequest;
import dev.matheus.basketservice.controller.request.ProductRequest;
import dev.matheus.basketservice.entity.Basket;
import dev.matheus.basketservice.entity.Product;
import dev.matheus.basketservice.entity.Status;
import dev.matheus.basketservice.exceptions.BusinessException;
import dev.matheus.basketservice.exceptions.DataNotFoundException;
import dev.matheus.basketservice.repository.BasketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private BasketService basketService;

    @Test
    @DisplayName("Deve lançar exceção ao criar cesta para cliente que já possui uma aberta")
    void createBasketShouldThrowExceptionWhenOpenBasketExists() {

        BasketRequest basketRequest = new BasketRequest(11L, List.of());
        when(basketRepository.findByClientAndStatus(11L, Status.OPEN))
                .thenReturn(Optional.of(Basket.builder().id("id-existente").build()));

        assertThrows(BusinessException.class, () -> basketService.createBasket(basketRequest));
        verify(basketRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve criar uma cesta com sucesso e calcular o preço total")
    void createBasketShouldSucceed() {
        ProductRequest productRequest = new ProductRequest(1L, 2);
        BasketRequest basketRequest = new BasketRequest(11L, List.of(productRequest));

        PlatziProductResponse platziResponse = new PlatziProductResponse(1L, "Produto Teste", new BigDecimal("50.00"));

        when(basketRepository.findByClientAndStatus(anyLong(), any())).thenReturn(Optional.empty());
        when(productService.getProductById(1L)).thenReturn(platziResponse);
        when(basketRepository.save(any(Basket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Basket result = basketService.createBasket(basketRequest);

        assertNotNull(result);
        assertEquals(new BigDecimal("100.00"), result.getTotalPrice());
        assertEquals(Status.OPEN, result.getStatus());
        verify(basketRepository).save(any(Basket.class));
    }

    @Test
    @DisplayName("Deve lançar DataNotFoundException quando o ID da cesta não existir")
    void getBasketByIdShouldThrowExceptionWhenNotFound() {
        when(basketRepository.findById("invalido")).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> basketService.getBasketById("invalido"));
    }

    @Test
    @DisplayName("Deve limpar a cesta com sucesso quando o status for OPEN")
    void clearBasketShouldSucceedWhenStatusIsOpen() {
        String id = "basket-01";
        Basket basket = Basket.builder()
                .id(id)
                .status(Status.OPEN)
                .products(new ArrayList<>(List.of(new Product())))
                .build();

        when(basketRepository.findById(id)).thenReturn(Optional.of(basket));
        when(basketRepository.save(any(Basket.class))).thenAnswer(i -> i.getArgument(0));

        Basket result = basketService.clearBasket(id);

        assertTrue(result.getProducts().isEmpty());
        assertNull(result.getPaymentMethod());
        verify(basketRepository).save(basket);
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException ao tentar pagar cesta que não está OPEN")
    void payBasketShouldThrowExceptionWhenStatusIsNotOpen() {
        String id = "basket-01";
        Basket closedBasket = Basket.builder().id(id).status(Status.SOLD).build();
        when(basketRepository.findById(id)).thenReturn(Optional.of(closedBasket));

        assertThrows(IllegalStateException.class, () -> basketService.payBasket(id, new PaymentRequest()));
    }

    @Test
    @DisplayName("Deve atualizar os produtos da cesta com sucesso")
    void updateBasketShouldSucceed() {
        String id = "basket-123";
        ProductRequest productRequest = new ProductRequest(1L, 5);
        BasketRequest basketRequest = new BasketRequest(123L, List.of(productRequest));

        Basket basketExistente = Basket.builder().id(id).status(Status.OPEN).build();
        PlatziProductResponse platziProductResponse = new PlatziProductResponse(1L, "Item", new BigDecimal("10.0"));

        when(basketRepository.findById(id)).thenReturn(Optional.of(basketExistente));
        when(productService.getProductById(1L)).thenReturn(platziProductResponse);
        when(basketRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Basket result = basketService.updateBasket(id, basketRequest);

        assertEquals(5, result.getProducts().get(0).getQuantity());
        assertEquals(new BigDecimal("50.0"), result.getTotalPrice());
        verify(basketRepository).save(any());
    }

    @Test
    @DisplayName("Deve deletar uma cesta existente")
    void deleteBasketShouldSucceed() {
        String id = "basket-123";
        Basket basket = Basket.builder().id(id).build();
        when(basketRepository.findById(id)).thenReturn(Optional.of(basket));

        basketService.deleteBasket(id);

        verify(basketRepository, times(1)).delete(basket);
    }
}