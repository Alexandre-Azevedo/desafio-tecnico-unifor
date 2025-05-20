# Etapa 1: Build da aplicação Angular
FROM node:18-alpine AS build

WORKDIR /app
COPY . .

# Instala dependências e compila o app para produção
RUN npm install && npm run build --prod

# Etapa 2: Servir com Nginx
FROM nginx:alpine

# Copia arquivos do build para a pasta padrão do Nginx
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

# Substitui a config padrão pelo nosso roteamento SPA
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expõe a porta do container (por padrão o nginx usa 80)
EXPOSE 80
