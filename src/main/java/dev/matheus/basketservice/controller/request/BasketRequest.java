package dev.matheus.basketservice.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record BasketRequest(
        @Schema(description = "ID do cliente dono da cesta/carrinho", example = "12")
        Long clientId,
        @Schema(description = "Lista de produtos na cesta/carrinho")
        List<ProductRequest> products) {
}
