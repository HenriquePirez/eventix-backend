🛠️ Backend - Eventix

Este repositório contém o backend da aplicação desenvolvido em Java com o framework Spring Boot 3. O sistema fornece APIs seguras para comunicação com o frontend e utiliza PostgreSQL em um container Docker para persistência de dados. A documentação das rotas está disponível via Swagger.

🚀 Tecnologias Utilizadas
Java 17+

Spring Boot 3

Spring Web

Spring Security

Spring Data JPA

PostgreSQL

Docker

Swagger (Springdoc OpenAPI)

🔐 Segurança
A aplicação utiliza Spring Security para proteger as rotas da API. As rotas públicas e privadas estão organizadas e protegidas por autenticação (ex: via JWT, se for o caso).

Configure o arquivo application.properties ou application.yml com as credenciais e chaves necessárias.

📦 Banco de Dados
O banco de dados utilizado é o PostgreSQL, que roda em um container Docker.

📂 Exemplo de docker-compose.yml
yaml
Copiar
Editar
version: '3.1'

services:
  db:
    image: postgres:15
    container_name: postgres_db
    environment:
      POSTGRES_DB: nome_do_banco
      POSTGRES_USER: usuario
      POSTGRES_PASSWORD: senha
    ports:
      - "5432:5432"
    volumes:
      - ./postgres-data:/var/lib/postgresql/data
📄 Documentação da API
A documentação das rotas da API é gerada automaticamente com o Swagger.
Após rodar a aplicação, acesse:

bash
Copiar
Editar
http://localhost:8080/swagger-ui.html
