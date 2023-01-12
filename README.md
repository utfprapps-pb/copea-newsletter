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

## <img src="https://miro.medium.com/max/640/1*CTuO-w7wiq_yhLh9plnkCw.webp" width="28px"> Docker

## <img src="https://github.com/devops-workflow/jenkins-icons/blob/master/icons/docker-logo-32x18.png?raw=true"> Docker
Comandos para rodar o angular e java no docker:
```
1. Abrir o projeto do angular (/web-angular) e digitar o comando ng build (se for a primeira vez que abriu o projeto, antes executar npm install)

2. Abrir o projeto do java (/api-java) e executar o comando mvn clean package

3. Exectar na raiz do projeto o comando docker compose build

4. Exectar na raiz do projeto o comando docker compose up

5. Se por acaso alguma alteração não estiver na imagem, remova a imagem com o comando 'docker image rm nome_imagem' ou delete pelo docker desktop e execute os comandos novamente.
```
