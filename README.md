# Desafio Unifor

Este projeto é uma API REST desenvolvida em Java com Quarkus, integrada ao Keycloak para autenticação e autorização.

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

#### `GET /usuarios`
- **Descrição:** Lista todos os usuários cadastrados.
- **Permissão:** ADMIN

#### `POST /usuarios`
- **Descrição:** Cadastra um novo usuário (também cria no Keycloak).
- **Permissão:** ADMIN
- **Body Exemplo:**
  ```json
  {
    "nome": "João da Silva",
    "email": "joao@exemplo.com",
    "username": "joaosilva",
    "perfil": "ALUNO"
  }
  ```
- **Retorno:** Username e senha gerada.

#### `PUT /usuarios/{id}`
- **Descrição:** Atualiza um usuário existente (também atualiza no Keycloak).
- **Permissão:** ADMIN
- **Body Exemplo:**
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

#### `GET /cursos`
- **Descrição:** Lista todos os cursos.
- **Permissão:** COORDENADOR

#### `POST /cursos`
- **Descrição:** Adiciona um novo curso.
- **Permissão:** COORDENADOR
- **Body Exemplo:**
  ```json
  {
    "nome": "Engenharia de Software"
  }
  ```

#### `PUT /cursos/{id}`
- **Descrição:** Atualiza um curso existente.
- **Permissão:** COORDENADOR

---

### Matriz Curricular

#### `GET /matriz`
- **Descrição:** Lista a matriz curricular (cursos).
- **Permissão:** ALUNO, PROFESSOR

---

### Token

#### `POST /token`
- **Descrição:** Gera um token do Keycloak a partir de credenciais.
- **Permissão:** Pública (apenas para desenvolvimento)
- **Body (x-www-form-urlencoded):**
  - `client_id`
  - `client_secret`
  - `username`
  - `password`

---

## Observações

- Todas as rotas (exceto `/token`) exigem autenticação JWT.
- Os papéis (roles) devem ser atribuídos no Keycloak.
- Essa rota foi a saida encontrada para gerar o JWT onde o `iss` fosse compativel com o usado pelo quarkus para validação, pois os serviços estão em containers diferentes.
---

## Como rodar

1. Basta executar `docker-compose up -d` no diretório `docker-compose.yml`, através de um terminal.

## Como parar

1. Se desejar parar o container `apagando` os volumes que armazenam dados execute `docker-compose down -v` no diretório `docker-compose.yml`, através de um terminal.
2. Se desejar parar o container `não apangando` os volumes que armazenam dados execute `docker-compose down` no diretório `docker-compose.yml`, através de um terminal.

---