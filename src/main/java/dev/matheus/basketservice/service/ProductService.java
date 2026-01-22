package dev.matheus.basketservice.service;

import dev.matheus.basketservice.client.PlatziStoreClient;
import dev.matheus.basketservice.client.response.PlatziProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final PlatziStoreClient platziStoreClient;

    @Cacheable(value = "products")
    public List<PlatziProductResponse> getAllProducts() {
        log.info("Setting all products");
        return platziStoreClient.getAllProducts();
    }

    @Cacheable(value = "product", key = "#productId")
    public PlatziProductResponse getProductById(Long productId) {
        log.info("Setting product by id: {}", productId);
        return platziStoreClient.getProductById(productId);
    }

}
