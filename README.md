# tp-aeds-iii
ola to guardando aqui mais ou menos o que faz cada coisa por falta de um lugar melhor pra todo mundo ver
a arquitetura eh MVC + DAO
dentro do sgbd
mydatabase.java é a classe que coordena o sgbd e com que o resto do programa interage
parser interpreta os comandos da aplicação
catalog armazena os metadados das tabelas
storage faz o armazenametno fisico dos registros
buffer cache, carrega paginas do storage pra ram
query recebe a query gerada pelo parser e faz as coisas q ela manda
index faz buscas usando indices

a funcao do gitkeep eh ele commitar os diretorios