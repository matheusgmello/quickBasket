package dev.matheus.basketservice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "basket")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Basket {

    @Schema(description = "ID da cesta/carrinho", example = "64b7f9f5e1d2c3a5b6c7d8e9")
    @Id
    private String id;

    @Schema(description = "ID do cliente dono da cesta/carrinho", example = "12")
    private Long client;

    @Schema(description = "Preço total da cesta/carrinho", example = "199.98")
    private BigDecimal totalPrice;

    @Schema(description = "Lista de produtos na cesta/carrinho")
    private List<Product> products;

    @Schema(description = "Status atual da cesta/carrinho", example = "OPEN | SOLD")
    private Status status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PaymentMethod paymentMethod;

    public void calculateTotalPrice() {
        this.totalPrice = products.stream()
                .map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
