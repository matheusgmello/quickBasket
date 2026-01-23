package dev.matheus.basketservice.service;

import dev.matheus.basketservice.client.response.PlatziProductResponse;
import dev.matheus.basketservice.controller.request.BasketRequest;
import dev.matheus.basketservice.controller.request.PaymentRequest;
import dev.matheus.basketservice.entity.Basket;
import dev.matheus.basketservice.entity.Product;
import dev.matheus.basketservice.entity.Status;
import dev.matheus.basketservice.exceptions.BusinessException;
import dev.matheus.basketservice.exceptions.DataNotFoundException;
import dev.matheus.basketservice.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketService {


    private final BasketRepository basketRepository;
    private final ProductService productService;

    public List<Basket> getAllBasket() {
        return basketRepository.findAll();
    }

    public Basket getBasketById(String id) {
        return basketRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cesta não encontrada"));
    }

    public Basket createBasket(BasketRequest basketRequest) {

        basketRepository.findByClientAndStatus(basketRequest.clientId(), Status.OPEN)
                .ifPresent(existingBasket -> {
                    throw new BusinessException("Cliente já possui uma Cesta com Status OPEN, apenas uma cesta por vez: " + existingBasket.getId());
                });
        List<Product> products = getProducts(basketRequest);

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

        List<Product> products = getProducts(request);

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

    public Basket clearBasket(String basketId) {
        Basket savedBasket = getBasketById(basketId);

        if (savedBasket.getStatus() != Status.OPEN) {
            throw new IllegalStateException("Apenas cestas com status OPEN podem ser limpas");
        }

        savedBasket.setProducts(new ArrayList<>());
        savedBasket.setPaymentMethod(null);
        savedBasket.getProducts().clear();
        savedBasket.calculateTotalPrice();

        return basketRepository.save(savedBasket);
    }

    public void deleteBasket(String id) {
        basketRepository.delete(getBasketById(id));
    }


    private @NonNull List<Product> getProducts(BasketRequest basketRequest) {
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
        return products;
    }

}
