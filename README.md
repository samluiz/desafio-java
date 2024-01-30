
# Order Management API

API que gerencia os pedidos de um restaurante.


## Referência

- [Repositório base](https://github.com/mb-data/desafio-java)


## Funcionalidades

- Criação, atualização, visualização e remoção (soft delete) de produtos
- Criação e visualização de pedidos
- Autenticação JWT
- RBAC (Role Based Access Control)
- Testes unitários
- Migrations


## Rodando localmente

Clone o projeto

```bash
  git clone https://github.com/samluiz/desafio-java/tree/main
```

Entre no diretório do projeto

```bash
  cd desafio-java
```

Instale as dependências

```bash
  mvn install
```

Inicie o servidor (é necessário possuir o Docker e docker-compose instalados em sua máquina)

```bash
  ./run.sh
```

O swagger da API estará disponível em http://localhost:8080/api/docs

Para acessar os endpoints protegidos é necessário gerar um token JWT. Para isso, faça uma requisição POST para http://localhost:8080/api/auth/login com o seguinte payload:

```json
{
  "username": "admin",
  "password": "admin"
}
```

## Rodando os testes

Para rodar os testes, rode o seguinte comando

```bash
  mvn test
```


## Stack utilizada

Java 17, Spring Boot, PostgreSQL, Flyway, Docker, JUnit 5, Mockito
