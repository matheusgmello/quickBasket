package dev.matheus.basketservice.repository;

import dev.matheus.basketservice.entity.Basket;
import dev.matheus.basketservice.entity.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BasketRepository extends MongoRepository<Basket, String> {

    Optional<Basket> findByClientAndStatus(Long client, Status status);

}
