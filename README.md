# Desafio Unifor

Este projeto é uma API REST desenvolvida em Java com Quarkus, integrada ao Keycloak para autenticação e autorização, e um frontend Angular para gestão acadêmica.

---

## Índice

- [Autenticação](#autenticação)
- [Rotas da API](#rotas-da-api)
  - [Usuários](#usuários)
  - [Cursos](#cursos)
  - [Semestres](#semestres)
  - [Disciplinas](#disciplinas)
  - [Matriz Curricular](#matriz-curricular)
  - [Dashboard](#dashboard)
  - [Token](#token)
- [Como rodar o projeto](#como-rodar-o-projeto)
- [Como parar o projeto](#como-parar-o-projeto)
- [Observações](#observações)

---

## Autenticação

A API utiliza autenticação JWT via Keycloak.  
Para acessar rotas protegidas, obtenha um token JWT do Keycloak e envie no header:

```
Authorization: Bearer <seu_token>
```

---

## Rotas da API

### Usuários

| Método | Rota                | Descrição                                         | Permissão |
|--------|---------------------|---------------------------------------------------|-----------|
| GET    | `/usuarios`         | Lista todos os usuários cadastrados               | ADMIN     |
| POST   | `/usuarios`         | Cadastra um novo usuário (cria no Keycloak)       | ADMIN     |
| GET    | `/usuarios/{id}`    | Busca um usuário pelo ID                          | ADMIN     |
| PUT    | `/usuarios/{id}`    | Atualiza um usuário existente (Keycloak incluso)  | ADMIN     |
| DELETE | `/usuarios/{id}`    | Remove um usuário                                 | ADMIN     |

#### Exemplo de Body para criação:
```json
{
  "nome": "João da Silva",
  "email": "joao@exemplo.com",
  "username": "joaosilva",
  "perfil": "ALUNO",
  "senha": "senha123"
}
```

#### Exemplo de Body para atualização:
```json
{
  "nome": "João da Silva",
  "email": "joao@exemplo.com",
  "username": "joaosilva",
  "perfil": "ALUNO",
  "senha": "novaSenha"
}
```

---

### Cursos

| Método | Rota            | Descrição                    | Permissão   |
|--------|-----------------|------------------------------|-------------|
| GET    | `/cursos`       | Lista todos os cursos        | COORDENADOR |
| POST   | `/cursos`       | Adiciona um novo curso       | COORDENADOR |
| PUT    | `/cursos/{id}`  | Atualiza um curso existente  | COORDENADOR |
| DELETE | `/cursos/{id}`  | Remove um curso              | COORDENADOR |

#### Exemplo de Body:
```json
{
  "nome": "Engenharia de Software"
}
```

---

### Semestres

| Método | Rota                | Descrição                    | Permissão   |
|--------|---------------------|------------------------------|-------------|
| GET    | `/semestres`        | Lista todos os semestres     | COORDENADOR |
| POST   | `/semestres`        | Adiciona um novo semestre    | COORDENADOR |
| PUT    | `/semestres/{id}`   | Atualiza um semestre         | COORDENADOR |
| DELETE | `/semestres/{id}`   | Remove um semestre           | COORDENADOR |

---

### Disciplinas

| Método | Rota                  | Descrição                      | Permissão   |
|--------|-----------------------|--------------------------------|-------------|
| GET    | `/disciplinas`        | Lista todas as disciplinas     | COORDENADOR |
| POST   | `/disciplinas`        | Adiciona uma nova disciplina   | COORDENADOR |
| PUT    | `/disciplinas/{id}`   | Atualiza uma disciplina        | COORDENADOR |
| DELETE | `/disciplinas/{id}`   | Remove uma disciplina          | COORDENADOR |

---

### Matriz Curricular

| Método | Rota        | Descrição                        | Permissão           |
|--------|-------------|----------------------------------|---------------------|
| GET    | `/matriz`   | Lista a matriz curricular        | ALUNO, PROFESSOR    |

---

### Dashboard

| Método | Rota           | Descrição                      | Permissão           |
|--------|----------------|--------------------------------|---------------------|
| GET    | `/dashboard`   | Dados do dashboard             | ADMIN, COORDENADOR, PROFESSOR, ALUNO |

---

### Token

| Método | Rota      | Descrição                                         | Permissão |
|--------|-----------|---------------------------------------------------|-----------|
| POST   | `/token`  | Gera um token do Keycloak a partir de credenciais | Pública (dev) |

#### Body (x-www-form-urlencoded):
- `client_id`
- `client_secret`
- `username`
- `password`

---

## Como rodar o projeto

1. Execute:
   ```bash
   docker-compose up -d
   ```
   no diretório onde está o arquivo `docker-compose.yml`.

2. O frontend estará disponível em `http://localhost:4200` (ou porta configurada).
3. O backend estará disponível em `http://localhost:8081`.
4. O Keycloak estará disponível em `http://localhost:8080`.

---

## Como parar o projeto

- Para parar e **remover volumes** (apaga dados):
  ```bash
  docker-compose down -v
  ```
- Para parar e **manter volumes** (mantém dados):
  ```bash
  docker-compose down
  ```

---

## Observações

- Todas as rotas (exceto `/token`) exigem autenticação JWT.
- Os papéis (roles) devem ser atribuídos no Keycloak.
- O token JWT deve ser enviado no header `Authorization`.
- O projeto já inclui roles: `ADMIN`, `COORDENADOR`, `PROFESSOR`, `ALUNO`.
- O frontend Angular faz uso das roles para proteger rotas e exibir funcionalidades conforme o perfil do usuário.
- Projeto frontend incompleto apenas com o CRUD de usuários implementado e segurança das rotas implementadas Roles do Keycloak.
- No backend todo o projeto está completo.
- Recomendo mudar a senha padrão do usuário admin padrão, através da interface do frontend ou localhos:8080(Selecionando a realm desafio, indo em users e editando a senha).
- Quando subir o sistema o usuário admin deve acessar localhos:8080, ir no realm desafio, clients, backend-service, services account roles, assign role, filter by cilents e buscar realm-management realm-admin e associar ao client, essa configuração só consegue ser feita através da interface do Keycloack e é necessária para criação de novos usuários através do admin.

---

## Dúvidas

Se tiver dúvidas sobre uso da API, autenticação ou configuração do ambiente, consulte o código-fonte ou abra uma issue.

---
