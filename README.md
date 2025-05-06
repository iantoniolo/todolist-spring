# ToDoList Spring Boot API

Este é um projeto **inicial de estudos** em Java, que implementa uma API de ToDoList utilizando **Spring Boot**, **Docker** e **PostgreSQL**. O objetivo é praticar conceitos de desenvolvimento backend, integração com banco de dados, autenticação, testes automatizados e conteinerização.

## Funcionalidades

- Cadastro e autenticação de usuários (JWT)
- CRUD de tarefas (ToDo)
- Integração com banco de dados PostgreSQL
- Testes automatizados de controllers e serviços
- Conteinerização com Docker e orquestração com Docker Compose

## Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Security (JWT)
- PostgreSQL
- Docker & Docker Compose
- Maven
- JUnit

## Como executar o projeto

### Pré-requisitos

- [Docker](https://www.docker.com/get-started) e [Docker Compose](https://docs.docker.com/compose/install/) instalados

### Subindo a aplicação

1. Clone o repositório:

   ```bash
   git clone https://github.com/iantoniolo/todolist-spring.git
   cd todolist-spring
   ```

2. Suba os containers:

  ```bash
  docker-compose up --build
  ```

 - A API estará disponível em: http://localhost:3000
 - O banco de dados PostgreSQL estará disponível na porta padrão 5432.

### Variáveis de ambiente

  - As principais variáveis já estão configuradas no docker-compose.yml, mas você pode alterá-las conforme necessário:

  SPRING_DATASOURCE_URL
  SPRING_DATASOURCE_USERNAME
  SPRING_DATASOURCE_PASSWORD

### Rodando os testes

 - Se quiser rodar os testes localmente (fora do Docker):

  ```bash
  mvn clean test
  ```
