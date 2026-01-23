package dev.matheus.basketservice.controller.request;

import dev.matheus.basketservice.entity.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    @Schema(description = "Método de pagamento utilizado na transação", example = "CREDIT | DEBIT | PIX")
    private PaymentMethod paymentMethod;
}
