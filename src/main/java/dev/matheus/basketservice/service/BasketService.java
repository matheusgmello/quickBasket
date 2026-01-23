package dev.matheus.basketservice.service;

import dev.matheus.basketservice.client.response.PlatziProductResponse;
import dev.matheus.basketservice.controller.request.BasketRequest;
import dev.matheus.basketservice.controller.request.PaymentRequest;
import dev.matheus.basketservice.entity.Basket;
import dev.matheus.basketservice.entity.Product;
import dev.matheus.basketservice.entity.Status;
import dev.matheus.basketservice.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketService {


    private final BasketRepository basketRepository;
    private final ProductService productService;

    public Basket getBasketById(String id) {
        return basketRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Basket not found"));
    }

    public Basket createBasket(BasketRequest basketRequest) {

        basketRepository.findByClientAndStatus(basketRequest.clientId(), Status.OPEN)
                .ifPresent(existingBasket -> {
                    throw new IllegalStateException("Cliente já possui uma Cesta com Status OPEN, apenas uma cesta por vez: " + existingBasket.getId());
                });
        List<Product> products = new ArrayList<>();

        basketRequest.products().forEach(productRequest -> {
            PlatziProductResponse platziProductResponse = productService.getProductById(productRequest.id());

            products.add(Product.builder()
                    .id(platziProductResponse.id())
                    .title(platziProductResponse.title())
                    .price(platziProductResponse.price())
                    .quantity(productRequest.quantity())
                    .build());


        });

        Basket basket = Basket.builder()
                .client(basketRequest.clientId())
                .status(Status.OPEN)
                .products(products)
                .build();

        basket.calculateTotalPrice();
        return basketRepository.save(basket);
    }

    public Basket updateBasket(String basketId, BasketRequest request) {
        Basket savedBasket = getBasketById(basketId);

        if (savedBasket.getStatus() != Status.OPEN) {
            throw new IllegalStateException("Apenas cestas com status OPEN podem ser atualizadas");
        }

        List<Product> products = new ArrayList<>();

        request.products().forEach(productRequest -> {
            PlatziProductResponse platziProductResponse = productService.getProductById(productRequest.id());

            products.add(Product.builder()
                    .id(platziProductResponse.id())
                    .title(platziProductResponse.title())
                    .price(platziProductResponse.price())
                    .quantity(productRequest.quantity())
                    .build());
        });

        savedBasket.setProducts(products);
        savedBasket.calculateTotalPrice();

        return basketRepository.save(savedBasket);
    }

    public Basket payBasket(String basketId, PaymentRequest request) {
        Basket savedBasket = getBasketById(basketId);

        if (savedBasket.getStatus() != Status.OPEN) {
            throw new IllegalStateException("Apenas cestas com status OPEN podem ser pagas");
        }

        savedBasket.setPaymentMethod(request.getPaymentMethod());
        savedBasket.setStatus(Status.SOLD);

        return basketRepository.save(savedBasket);
    }

}
