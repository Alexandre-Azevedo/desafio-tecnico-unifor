#!/bin/sh

# Aguarda o Postgres estar disponível na porta 5432
echo "Aguardando Postgres..."
/wait-for-it.sh postgres:5432 --timeout=60 --strict -- echo "Postgres está disponível."

# Aguarda o Keycloak estar disponível na porta 8080
echo "Aguardando Keycloak..."
/wait-for-it.sh keycloak:8080 --timeout=60 --strict -- echo "Keycloak está disponível."

# Inicia a aplicação Java
echo "Iniciando backend..."
java -jar app.jar
