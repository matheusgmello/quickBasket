package dev.matheus.basketservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class Product {

    @Schema(description = "ID do produto adicionado na cesta/carrinho", example = "45")
    private Long id;
    @Schema(description = "Título ou nome do produto", example = "Smartphone XYZ")
    private String title;
    @Schema(description = "Preço unitário do produto", example = "99.99")
    private BigDecimal price;
    @Schema(description = "Quantidade do produto adicionado na cesta/carrinho", example = "2")
    private Integer quantity;
}
