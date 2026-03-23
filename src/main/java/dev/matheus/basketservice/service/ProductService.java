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

    @Cacheable(value = "products", key = "#offset + '-' + #limit")
    public List<PlatziProductResponse> getAllProducts(int offset, int limit) {
        log.info("Setting all products with offset {} and limit {}", offset, limit);
        return platziStoreClient.getAllProducts(offset, limit);
    }

    @Cacheable(value = "product", key = "#productId")
    public PlatziProductResponse getProductById(Long productId) {
        log.info("Setting product by id: {}", productId);
        return platziStoreClient.getProductById(productId);
    }

}
