<h1 align="center">:file_cabinet: Newsletter!</h1>

## :memo: Descrição
Sistema para criação e envio por email de notícias.

## :wrench: Versões utilizadas
* Angular: 14.2.9
* npm: 8.19.2.
* Java: 17.
* Maven: 3.8.1.
* Quarkus: 2.15.3.Final.

## :runner: Rodar projeto
Para rodar o projeto, basta utilizar o comando <b>quarkus dev</b>, mais informações é possível verificar na documentação (https://quarkus.io/get-started/) do Quarkus.

## <img src="https://miro.medium.com/max/640/1*CTuO-w7wiq_yhLh9plnkCw.webp" width="32px" height="32px"> Documentação da API
Para a documentação foi utilizado o openapi do próprio Quarkus (https://quarkus.io/guides/openapi-swaggerui), a mesma se encontra no caminho .../api/swagger, exemplo http://localhost:8080/api/swagger

## <img src="https://github.com/devops-workflow/jenkins-icons/blob/master/icons/docker-logo-32x18.png?raw=true"> Docker
Neste projeto existem duas opções para fazer o deploy no Docker:
1. Utilizando o /api-java/Dockerfile.build para a API e o /web-angular/Dockerfile.build para o WEB, esses dois arquivos fazem o build do projeto (mvn clean package e ng build) e depois o deploy. Portanto, não é necessário realizar o build dos projetos, isso será feito dentro do Docker, é útil pois não precisa do Java e o Angular instalado na máquina para fazer o build dos projetos, basta ter o docker que funcionará. No docker-compose.yaml já está configurado para o caminho desses Dockerfiles.
2. Utilizando o /api-java/src/main/docker/Dockerfile.jvm para a API e o /web-angular/Dockerfile para o WEB, esses dois arquivos fazem somente o deploy, sem fazer o build do projeto, por exemplo, sem fazer o mvn clean package ou o ng build. Se quiser utilizar dessa maneira é necessário alterar no docker-compose-up o caminho dos Dockerfiles, pois estão apontando para a primeira opção. 

Comandos para rodar o angular e java no docker utilizando o docker compose:
```
1. (Ignorar se estiver utilizando a 1ª opção) Abrir o projeto do angular (/web-angular) e digitar o comando ng build (se for a primeira vez que abriu o projeto, antes executar npm install)

2. (Ignorar se estiver utilizando a 1ª opção) Abrir o projeto do java (/api-java) e executar o comando mvn clean package

3. Executar na raiz do projeto o comando docker compose build

4. Executar na raiz do projeto o comando docker compose up

5. Se por acaso alguma alteração não estiver na imagem, tente rodar o docker compose down e fazer o build e up novamente, caso não funcione, remova a imagem com o comando 'docker image rm nome_imagem' ou delete pelo docker desktop e execute os comandos novamente.
```

Segue as principais configurações que podem ser alteradas no arquivo docker-compose.yaml que se encontra na raiz do projeto:
- newsletter-db:
  - <b>POSTGRES_USER</b>: Usuário do banco postgres.
  - <b>POSTGRES_PASSWORD</b>: Senha do banco postgres.
  - <b>POSTGRES_DB</b>: Nome do banco.
- newsletter-api:
  - <b>DB_JDBC_URL</b>: url de conexão com o banco.
  - <b>DB_JDBC_USER</b>: Usuário do banco.
  - <b>DB_JDBC_PASSWORD</b>: Senha do banco.
  - <b>PORT</b>: Porta que vai rodar a API.
- newsletter-web:
  - <b>API_URL</b>: URL da API, sempre com /api no final.
  - <b>PORT</b>: Porta que vai rodar o servidor web.
