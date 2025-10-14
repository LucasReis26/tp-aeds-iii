<p align="center">
<img src="https://www.google.com/search?q=https://placehold.co/800x200/000000/FFFFFF%3Ftext%3DSistema%2Bde%2BGerenciamento%2Bde%2BFilmes" alt="Banner do Projeto">
</p>

<h1 align="center">🎬 Sistema de Gerenciamento de Filmes - AEDS III</h1>

<p align="center">
<!-- Badges de Status e Tecnologias -->
<img src="https://www.google.com/search?q=https://img.shields.io/badge/Status-Conclu%C3%ADdo-brightgreen%3Fstyle%3Dfor-the-badge" alt="Status do Projeto">
<img src="https://www.google.com/search?q=https://img.shields.io/badge/Java-21-blue.svg%3Fstyle%3Dfor-the-badge%26logo%3Dopenjdk" alt="Java">
<img src="https://www.google.com/search?q=https://img.shields.io/badge/Spring_Boot-3.3%2B-green.svg%3Fstyle%3Dfor-the-badge%26logo%3Dspring" alt="Spring Boot">
<img src="https://www.google.com/search?q=https://img.shields.io/badge/Maven-3.8%2B-red.svg%3Fstyle%3Dfor-the-badge%26logo%3Dapache-maven" alt="Maven">
<img src="https://www.google.com/search?q=https://img.shields.io/badge/Licen%C3%A7a-MIT-informational%3Fstyle%3Dfor-the-badge" alt="Licença MIT">
</p>
📝 Descrição do Projeto

Este projeto implementa um sistema completo de gerenciamento de filmes, usuários, avaliações e listas. O núcleo do sistema é um banco de dados de baixo nível construído em Java, utilizando arquivos de acesso aleatório para persistência.

A performance para buscas é otimizada através de uma estrutura de dados de Hash Extensível, que serve como motor para indexação primária (por ID) e secundária (para as relações entre as entidades).

Toda a lógica do banco de dados é exposta através de uma API RESTful, construída com o framework Spring Boot. Para facilitar os testes e a demonstração, o projeto oferece duas interfaces de teste:

    Swagger UI: Uma documentação interativa completa para desenvolvedores.

    Página HTML Simples: Uma interface de utilizador básica para demonstrar o consumo da API por um frontend.

✨ Funcionalidades Principais

O sistema oferece um gerenciamento completo (CRUD - Criar, Ler, Atualizar, Deletar) para as seguintes entidades:

    Usuários:

        Criação, busca por ID, alteração e exclusão de usuários.

    Filmes:

        Criação de filmes com atributos detalhados (título, data, diretores, atores, etc.).

        Busca de filmes por ID e por parte do título.

        Alteração e exclusão de filmes.

    Avaliações (Reviews):

        Criação de uma avaliação de um usuário para um filme.

        Busca de todas as avaliações de um usuário ou de um filme.

        Exclusão de uma avaliação.

    Listas:

        Criação de listas padrão ("Favoritos", etc.) e personalizadas para um usuário.

        Adição e remoção de filmes a uma lista.

        Busca de todas as listas de um usuário, com os respetivos filmes.

        Exclusão de uma lista.

🛠️ Tecnologias Utilizadas

    Backend: Java 21, Spring Boot

    Banco de Dados: Implementação customizada com Hash Extensível e Arquivos de Acesso Aleatório.

    Build & Dependências: Apache Maven

    Documentação da API: Springdoc OpenAPI 3 (Swagger UI)

👥 Integrantes do Grupo

    Ana Paula Natsumi Yuki

    Gabriel Henrique Pereira Carvalhaes

    Lucas Teixeira Reis

    Theo Diniz Viana

🚀 Como Executar a Aplicação

Siga os passos abaixo para compilar o código-fonte e iniciar o servidor da API.
Pré-requisitos

    JDK (Java Development Kit) - Versão 21 ou superior.

    Apache Maven - Versão 3.8 ou superior.

    Nota: O projeto inclui o Maven Wrapper, que garante a consistência do ambiente de build e é a forma recomendada de execução.

Passos para Execução

1. Clone o Repositório

Se ainda não o fez, clone o projeto para a sua máquina local:

git clone <URL_DO_SEU_REPOSITORIO>

2. Navegue para a Pasta Raiz do Projeto

Este é o passo mais importante. Todos os comandos a seguir devem ser executados a partir da pasta raiz do projeto, que é a pasta que contém o ficheiro pom.xml e a pasta src.

cd <NOME_DA_PASTA_DO_PROJETO>

3. Execute a Aplicação com o Maven Wrapper

O mvnw (Maven Wrapper) garante que a versão correta do Maven seja usada. Execute o comando apropriado para o seu sistema operacional:

    No Linux ou macOS:

    ./mvnw spring-boot:run

    (Se receber um erro de "Permissão negada", execute chmod +x mvnw primeiro para tornar o script executável.)

    No Windows (Prompt de Comando ou PowerShell):

    .\mvnw.cmd spring-boot:run

Após a execução, o Maven irá compilar o projeto e iniciar o servidor. A aplicação estará pronta quando vir a mensagem "Tomcat started on port(s): 8080 (http)" no final do log.
🧪 Como Testar a Aplicação

Existem duas formas de testar a API depois de a aplicação estar a rodar.
1. Interface de Teste Frontend (index.html)

Esta é uma interface de utilizador simples para demonstrar o consumo da API por um cliente web.

a) Localização do Ficheiro
O ficheiro index.html deve ser colocado na seguinte pasta do projeto:
src/main/resources/static/

b) Aceda no Navegador
Com a aplicação a rodar, abra o seu navegador e aceda ao endereço principal do servidor:

http://localhost:8080/

O Spring Boot servirá automaticamente o index.html como a página principal. Use os formulários na página para criar e buscar dados.
2. Documentação Interativa (Swagger UI)

Esta é a ferramenta completa para desenvolvedores, que documenta e permite testar todos os endpoints da API.

a) Aceda no Navegador
Com a aplicação a rodar, navegue para o seguinte endereço:

http://localhost:8080/swagger-ui.html

b) Como Usar a Interface

    A página mostrará todos os endpoints agrupados por categoria (Users, Filmes, Reviews, Listas).

    Clique em qualquer um dos endpoints para expandir os seus detalhes.

    Clique no botão "Try it out".

    Preencha os parâmetros necessários ou edite o corpo da requisição em JSON.

    Clique no botão azul "Execute".

    A página fará a chamada à sua API e exibirá o resultado completo logo abaixo.

📈 Fluxo de Teste Recomendado (Usando Swagger ou o index.html):

    Use a funcionalidade de criar utilizador (POST /api/users). Guarde o ID retornado.

    Use a funcionalidade de criar filme (POST /api/filmes). Guarde o ID.

    Use POST /api/listas/user/{userId}/init-padrao para criar as listas padrão para o seu utilizador.

    Use POST /api/listas/{listaId}/filmes/{filmeId} para adicionar um filme a uma lista.

    Use GET /api/listas/user/{userId} para verificar se o filme foi adicionado.

    Explore as outras rotas GET, DELETE e de alteração para validar todo o ciclo de vida dos dados.