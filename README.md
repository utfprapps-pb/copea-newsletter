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
  - <b>POSTGRES_USER</b>: Usuário do banco postgres. (pode ser substituída por meio da env POSTGRES_USER)
  - <b>POSTGRES_PASSWORD</b>: Senha do banco postgres. (pode ser substituída por meio da env POSTGRES_PASSWORD)
  - <b>POSTGRES_DB</b>: Nome do banco. (pode ser substituída por meio da env POSTGRES_DB)
  - <b>TZ</b>: Timezone do banco. (pode ser substituída por meio da env TZ_DB) 
- newsletter-api:
  - <b>DB_JDBC_URL</b>: url de conexão com o banco. (pode ser substituída por meio da env DB_JDBC_URL)
  - <b>DB_JDBC_USER</b>: Usuário do banco. (pode ser substituída por meio da env DB_JDBC_USER)
  - <b>DB_JDBC_PASSWORD</b>: Senha do banco. (pode ser substituída por meio da env DB_JDBC_PASSWORD)
  - <b>PORT</b>: Porta que vai rodar a API. (pode ser substituída por meio da env PORT_API)
  - <b>TZ</b>: Timezone da API. (pode ser substituída por meio da env TZ_API) 
- newsletter-web:
  - <b>API_URL</b>: URL da API, sempre com /api no final. (pode ser substituída por meio da env API_URL)
  - <b>PORT</b>: Porta que vai rodar o servidor web. (pode ser substituída por meio da env PORT_WEB)
  - <b>TZ</b>: Timezone do WEB. (pode ser substituída por meio da env TZ_WEB)

## <img width="30px" src="https://github.com/devops-workflow/jenkins-icons/blob/master/icons/jenkins-logo-48x48.png?raw=true"> Jenkins
O Jenkins está configurado de 3 maneiras, na raiz do projeto possui o Jenkinsfile que roda o docker compose up do docker-compose.yaml da raiz do projeto, esse abrange tanto o banco de dados, API e WEB. Já na pasta de cada projeto tem o Jenkinsfile separado. Portanto, se quiser configurar um Job do Jenkins para a API e outro para WEB, basta apontar para o Jenkinsfile da pasta do projeto, exemplo api-java/Jenkinsfile ou web-angular/Jenkinsfile.
ATENÇÃO: Nos Jenkinsfiles citados acima é feito o docker compose down, e no da API, junto está o banco, então toda vez que executar o docker compose down vai apagar o banco e criar novamente no docker compose up. 
