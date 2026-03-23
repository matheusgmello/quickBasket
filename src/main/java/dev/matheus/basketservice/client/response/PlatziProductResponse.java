package dev.matheus.basketservice.client.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public record PlatziProductResponse(
        Long id, 
        String title, 
        BigDecimal price, 
        String description, 
        List<String> images
) implements Serializable {
}
