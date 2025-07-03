# 🧾 Spring Boot API - Todo API

API REST construída com Spring Boot para gerenciamento de tarefas.

---

## ✅ Tecnologias utilizadas

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- Spring Security (JWT)
- PostgreSQL / MySQL
- Flyway / Liquibase (opcional)
- Swagger/OpenAPI
- Docker (opcional)

---

## 📁 Estrutura do Projeto
```
src/
├── main/
│ ├── java/
│ │ └── com.if.todo/
│ │ ├── configuration/
│ │ ├── controller/
│ │ ├── dto/
│ │ ├── entity/
│ │ ├── repository/
│ │ ├── service/
│ │ └── security/
│ └── resources/
│ ├── application.properties
│ └── db/
└── test/
``

## ⚙️ Configuração

### Pré-requisitos

- Java 17+
- Maven ou Gradle
- PostgreSQL ou MySQL
- Docker (opcional)

### Variáveis de ambiente

No arquivo `application.properties`, adicione as váriveis de conexão:

```properties
spring.datasource.url=<url-banco>
spring.datasource.username=<usuario>
spring.datasource.password=<senha>

spring.jpa.show-sql=true

# JWT (se usar)
jwt.secret=<secret>
jwt.expiration=86400000
```

---

## ▶️ Como rodar localmente

### Com Maven

```bash
./mvnw spring-boot:run
```

### Com Gradle

```bash
./gradlew bootRun
```

---

## 🧪 Testes

```bash
./mvnw test
```

---

## 📮 Endpoints principais
![image](https://github.com/user-attachments/assets/891d73dd-e67e-4ba8-89e8-f6f0d51c86e2)

## 🔐 Autenticação

A API utiliza autenticação baseada em **JWT (Bearer Token)**.

Inclua o token no header de cada requisição:

```
Authorization: Bearer <seu_token>
```

---

## 📄 Licença

Este projeto está licenciado sob a [MIT License](LICENSE).


