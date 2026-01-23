package dev.matheus.basketservice.controller;

import dev.matheus.basketservice.controller.request.BasketRequest;
import dev.matheus.basketservice.controller.request.PaymentRequest;
import dev.matheus.basketservice.entity.Basket;
import dev.matheus.basketservice.service.BasketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/basket")
@Tag(name = "Basket", description = "Endpoints para gerenciamento de cestas/carrinhos")
public class BasketController {

    private final BasketService basketService;

    @Operation(summary = "Listar cestas/carrinhos", description = "Método responsável por listar todas as cestas/carrinhos")
    @ApiResponse(responseCode = "200", description = "Retorna a lista de cestas/carrinhos")
    @GetMapping
    public ResponseEntity<List<Basket>> getAllBasket() {
        return ResponseEntity.ok(basketService.getAllBasket());
    }

    @Operation(summary = "Obter cesta/carrinho por ID", description = "Método responsável por obter uma cesta/carrinho pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Retorna a cesta/carrinho correspondente ao ID fornecido")
    @GetMapping("/{id}")
    public ResponseEntity<Basket> getBasketById(@PathVariable String id) {
        return ResponseEntity.ok(basketService.getBasketById(id));
    }

    @Operation(summary = "Criar nova cesta/carrinho", description = "Método responsável por criar uma nova cesta/carrinho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cesta/carrinho criada com sucesso",
                    content = @Content(schema = @Schema(implementation = Basket.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    public ResponseEntity<Basket> createBasket(@RequestBody BasketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(basketService.createBasket(request));
    }

    @Operation(summary = "Atualizar cesta/carrinho", description = "Método responsável por atualizar uma cesta/carrinho existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cesta/carrinho atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = Basket.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Cesta/carrinho não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Basket> updateBasket(@PathVariable String id, @RequestBody BasketRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(basketService.updateBasket(id, request));
    }

    @Operation(summary = "Pagar cesta/carrinho", description = "Método responsável por processar o pagamento de uma cesta/carrinho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cesta/carrinho paga com sucesso",
                    content = @Content(schema = @Schema(implementation = Basket.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Cesta/carrinho não encontrada")
    })
    @PutMapping("/{id}/payment")
    public ResponseEntity<Basket> payBasket(@PathVariable String id, @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(basketService.payBasket(id, request));
    }

    @Operation(summary = "Limpar cesta/carrinho", description = "Método responsável por limpar todos os produtos de uma cesta/carrinho")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cesta/carrinho limpa com sucesso",
                    content = @Content(schema = @Schema(implementation = Basket.class))),
            @ApiResponse(responseCode = "404", description = "Cesta/carrinho não encontrada")
    })
    @PatchMapping("/{id}/clear")
    public ResponseEntity<Basket> clearBasket(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(basketService.clearBasket(id));
    }

    @Operation(summary = "Deletar cesta/carrinho", description = "Método responsável por deletar uma cesta/carrinho pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cesta/carrinho deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cesta/carrinho não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBasket(@PathVariable String id) {
        basketService.deleteBasket(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
