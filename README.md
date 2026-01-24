# QuickBasket

API para gerenciamento de um carrinho de compras com uso de API externa e cache (Redis) e persistência em MongoDB.

## Menu

- [Sobre o projeto](#sobre-o-projeto)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Configuração](#configuração)
- [Passo a passo](#passo-a-passo)
- [Padrões do projeto](#padrões-do-projeto)
- [Endpoints](#endpoints)
- [Exemplos rápidos de uso](#exemplos-rápidos-de-uso)

## Sobre o projeto

Este projeto é um microserviço em Java (Spring Boot) que gerencia cestas/carrinhos de compras. Ele consulta uma API externa (Platzi / EscuelaJS) para obter dados dos produtos e mantém caches para melhorar a performance.

Funcionalidades principais:
- Criar, listar, atualizar, pagar, limpar e deletar cestas (Basket).
- Consultar produtos via serviço externo (ProductService) com cache em Redis.
- Persistência das cestas em MongoDB.
- Documentação automática via OpenAPI/Swagger.

## Arquitetura

O projeto segue arquitetura em camadas:

`src/main/java/com/movieflix/`
- `client/` — Cliente Feign para a API externa de produtos
- `config/` — Configurações do Spring
- `controller/` — Controllers REST
- `entity/` — Entidades JPA
- `repository/` — Repositórios Spring Data
- `service/` — Regras de negócio
- `exception/` — Exceções customizadas
- `mapper/` — Conversão entre DTOs e entidades

## Tecnologias

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

## Configuração

### Passo a passo

1. Clone o repositório:

```
git clone [url-do-repositorio]
```

2. Configuração do banco de dados (exemplo mongodb) e redis:

```
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=basket-service

spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=sa
spring.cache.redis.time-to-live=60000
```

3. Build do projeto (Unix):

```
./build.sh
```

ou, em Windows (PowerShell/CMD):

```
./mvnw clean package
```

4. Iniciar a aplicação (Unix):

```
./start.sh
```

ou, em Windows:

```
java -jar target/<arquivo>.jar
```

A API ficará disponível em `http://localhost:8080`.

## Padrões do projeto

- MongoDB
  - host: `localhost`
  - port: `27017`
  - database: `basket-service`
- Redis
  - host: `localhost`
  - port: `6379`
  - password: `sa` (definido no arquivo por padrão — em produção use segredo seguro)
  - cache redis time-to-live: `60000` ms
- Cliente externo de produtos
  - base URL: `https://api.escuelajs.co/api/v1` (configuração em `basket.client.platzi`)
- Swagger UI
  - UI disponível em `/swagger/index.html`
  - OpenAPI JSON em `/api/api-docs`


### Endpoints

Base: `/` (aplicação)

Products
- GET `/products` - listar todos os produtos (vindo da API externa, cacheado)
- GET `/products/{id}` - obter produto por id (cacheado)

Basket
- GET `/basket` - listar todas as cestas
- GET `/basket/{id}` - obter cesta por id
- POST `/basket` - criar nova cesta
- PUT `/basket/{id}` - atualizar cesta (substitui lista de produtos)
- PUT `/basket/{id}/payment` - processar pagamento
- PATCH `/basket/{id}/clear` - limpar cesta (remover todos os produtos e paymentMethod)
- DELETE `/basket/{id}` - deletar cesta

## Exemplos rápidos de uso

1) Criar cesta (POST /basket)
- Body: ver seção Endpoints -> POST `/basket`.
- Retorno: JSON com a cesta criada, `totalPrice` calculado com base em `price * quantity`.

2) Limpar cesta (PATCH /basket/{id}/clear)
- Remove produtos e zera `paymentMethod` e `totalPrice`.

3) Deletar cesta (DELETE /basket/{id})
- Remove documento do MongoDB; resposta HTTP 204 No Content.
