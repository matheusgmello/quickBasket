package dev.matheus.basketservice.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new DataNotFoundException("Produto não encontrado");
            case 500 -> new RuntimeException("Erro interno do servidor");
            default -> new RuntimeException("Erro desconhecido");
        };
    }
}
