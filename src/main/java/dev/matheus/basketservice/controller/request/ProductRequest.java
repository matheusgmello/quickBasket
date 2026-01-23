package dev.matheus.basketservice.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductRequest(
        @Schema(description = "ID do produto adicionado na cesta/carrinho", example = "45")
        Long id,
        @Schema(description = "Quantidade do produto adicionado na cesta/carrinho", example = "2")
        Integer quantity) {
}
