
<h1 align="center">🎬 Sistema de Gerenciamento de Filmes - AEDS III</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Status-Em%20Andamento-yellow?style=for-the-badge" alt="Status do Projeto">
  <img src="https://img.shields.io/badge/Java-21-blue?style=for-the-badge&logo=openjdk" alt="Java">
  <img src="https://img.shields.io/badge/Spring_Boot-3.3+-green?style=for-the-badge&logo=spring" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Maven-3.8+-red?style=for-the-badge&logo=apache-maven" alt="Maven">
  <img src="https://img.shields.io/badge/Licença-MIT-informational?style=for-the-badge" alt="Licença MIT">
</p>

## 📝 Descrição do Projeto

Este projeto implementa um sistema completo de gerenciamento de filmes, usuários, avaliações e listas. O núcleo do sistema é um banco de dados de baixo nível construído em Java, utilizando **arquivos de acesso aleatório** para persistência.

A performance para buscas é otimizada através de uma estrutura de dados de **Hash Extensível**, que serve como motor para indexação primária (por ID) e secundária (para as relações entre as entidades).

Toda a lógica do banco de dados é exposta através de uma **API RESTful**, construída com o framework Spring Boot. Para facilitar os testes e a demonstração, o projeto oferece duas interfaces de teste:

-   **Swagger UI**: Uma documentação interativa completa para desenvolvedores.
-   **Página HTML Simples**: Uma interface de usuário básica para demonstrar o consumo da API por um frontend.

## ✨ Funcionalidades Principais

O sistema oferece um gerenciamento completo (CRUD - Criar, Ler, Atualizar, Deletar) para as seguintes entidades:

-   **Usuários**:
    -   Criação, busca por ID, alteração e exclusão de usuários.

-   **Filmes**:
    -   Criação de filmes com atributos detalhados (título, data, diretores, atores, etc.).
    -   Busca de filmes por ID e por parte do título.
    -   Alteração e exclusão de filmes.

-   **Avaliações (Reviews)**:
    -   Criação de uma avaliação de um usuário para um filme.
    -   Busca de todas as avaliações de um usuário ou de um filme.
    -   Exclusão de uma avaliação.

-   **Listas**:
    -   Criação de listas padrão ("Favoritos", etc.) e personalizadas para um usuário.
    -   Adição e remoção de filmes a uma lista.
    -   Busca de todas as listas de um usuário, com os respectivos filmes.
    -   Exclusão de uma lista.

## 🛠️ Tecnologias Utilizadas

-   **Backend**: Java 21, Spring Boot
-   **Banco de Dados**: Implementação customizada com Hash Extensível e Arquivos de Acesso Aleatório.
-   **Build & Dependências**: Apache Maven
-   **Documentação da API**: Springdoc OpenAPI 3 (Swagger UI)

## 👥 Integrantes do Grupo

-   Ana Paula Natsumi Yuki
-   Gabriel Henrique Pereira Carvalhaes
-   Lucas Teixeira Reis
-   Theo Diniz Viana

## 🚀 Como Executar a Aplicação

Siga os passos abaixo para compilar o código-fonte e iniciar o servidor da API.

### Pré-requisitos

-   **JDK (Java Development Kit)** - Versão 21 ou superior.
-   **Apache Maven** - Versão 3.8 ou superior.
    > **Nota:** O projeto inclui o Maven Wrapper (`mvnw`), que garante a consistência do ambiente de build e é a forma recomendada de execução.

### Passos para Execução

1.  **Clone o Repositório**

    Se ainda não o fez, clone o projeto para a sua máquina local:
    ```bash
    git clone <URL_DO_SEU_REPOSITORIO>
    ```

2.  **Navegue para a Pasta Raiz do Projeto**

    Este é o passo mais importante. Todos os comandos a seguir devem ser executados a partir da pasta raiz, que contém o arquivo `pom.xml`.
    ```bash
    cd <NOME_DA_PASTA_DO_PROJETO>
    ```

3.  **Execute a Aplicação com o Maven Wrapper**

    O `mvnw` (Maven Wrapper) garante que a versão correta do Maven seja usada. Execute o comando apropriado para o seu sistema operacional:

    -   **No Linux ou macOS:**
        ```bash
        ./mvnw spring-boot:run
        ```
        > Se receber um erro de "Permissão negada", execute `chmod +x mvnw` primeiro para tornar o script executável.

    -   **No Windows (Prompt de Comando ou PowerShell):**
        ```bash
        .\mvnw.cmd spring-boot:run
        ```

    Após a execução, o Maven irá compilar o projeto e iniciar o servidor. A aplicação estará pronta quando vir a mensagem `Tomcat started on port(s): 8080 (http)` no final do log.

## 🧪 Como Testar a Aplicação

Existem duas formas de testar a API depois que a aplicação estiver rodando.

### 1. Interface de Teste Frontend (`index.html`)

Abra o navegador e acesse o seguinte endereço para interagir com a interface gráfica simples:
[http://localhost:8080/index.html](http://localhost:8080/index.html)

### 2. Documentação Interativa (Swagger UI)

Abra o navegador e acesse o seguinte endereço para visualizar a documentação completa e testar todos os endpoints da API:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Fluxo de Teste Recomendado

Para validar o ciclo de vida dos dados, siga os passos abaixo (seja pelo Swagger ou pelo `index.html`):

1.  Crie um usuário (`POST /api/users`). Guarde o ID retornado.
2.  Crie um filme (`POST /api/filmes`). Guarde o ID retornado.
3.  Crie as listas padrão para o seu usuário (`POST /api/listas/user/{userId}/init-padrao`).
4.  Adicione o filme a uma das listas (`POST /api/listas/{listaId}/filmes/{filmeId}`).
5.  Verifique se o filme foi adicionado corretamente (`GET /api/listas/user/{userId}`).
6.  Explore as outras rotas `GET`, `DELETE` e de alteração para validar todo o sistema.
