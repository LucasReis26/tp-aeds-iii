package com.example.tpaedsiii.repository;

import com.example.tpaedsiii.model.filme.Filme;
import com.example.tpaedsiii.model.lista.Lista;
import com.example.tpaedsiii.model.review.Review;
import com.example.tpaedsiii.model.user.User;
import com.example.tpaedsiii.repository.filme.FilmeRepository;
import com.example.tpaedsiii.repository.lista.ListaRepository;
import com.example.tpaedsiii.repository.review.ReviewRepository;
import com.example.tpaedsiii.repository.user.UserRepository;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ConsoleTestRunner {

    private static FilmeRepository filmeDAO;
    private static ReviewRepository reviewDAO;
    private static ListaRepository listaDAO;
    private static UserRepository userDAO;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        if (!initializeDAOs()) return;

        while (true) {
            exibirMenu();
            System.out.print("\n> Escolha uma opção: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("auto")) {
                runAutomaticTests();
                System.out.println("\nTeste automático concluído. Pressione Enter para voltar ao menu...");
                scanner.nextLine();
                continue;
            }

            int opcao = -1;
            try {
                opcao = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite um número ou 'auto'.");
                continue;
            }

            if (opcao == 0) {
                System.out.println("Encerrando a aplicação...");
                scanner.close();
                break;
            }

            handleMenuOption(opcao);

            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }
    }

    private static boolean initializeDAOs() {
        try {
            System.out.println("Inicializando os DAOs e abrindo os arquivos...");
            filmeDAO = new FilmeRepository();
            reviewDAO = new ReviewRepository();
            listaDAO = new ListaRepository();
            userDAO = new UserRepository();
            System.out.println("Sistema de banco de dados pronto.\n");
            return true;
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO AO INICIALIZAR OS DAOS: A aplicação não pode continuar.");
            e.printStackTrace();
            return false;
        }
    }
    
    private static void handleMenuOption(int opcao) {
        switch (opcao) {
            case 1: criarUser(); break;
            case 2: buscarUserPorId(); break;
            case 3: alterarUser(); break;
            case 4: excluirUser(); break;
            case 5: criarFilme(); break;
            case 6: buscarFilmePorId(); break;
            case 7: alterarFilme(); break;
            case 8: excluirFilme(); break;
            case 9: criarReview(); break;
            case 10: buscarReviewPorId(); break;
            case 11: buscarReviewsPorUsuario(); break;
            case 12: buscarReviewsPorFilme(); break;
            case 13: excluirReview(); break;
            case 14: criarListaPersonalizada(); break;
            case 15: adicionarFilmeNaLista(); break;
            case 16: removerFilmeDaLista(); break;
            case 17: verListasCompletasDoUsuario(); break;
            case 18: excluirLista(); break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
    }

    private static void exibirMenu() {
        System.out.println("\n--- MENU DE TESTES CRUD COMPLETO ---");
        System.out.println("--- Usuários ---");
        System.out.println("1. Criar User");
        System.out.println("2. Buscar User por ID");
        System.out.println("3. Alterar User");
        System.out.println("4. Excluir User");
        System.out.println("--- Filmes ---");
        System.out.println("5. Criar Filme");
        System.out.println("6. Buscar Filme por ID");
        System.out.println("7. Alterar Filme");
        System.out.println("8. Excluir Filme");
        System.out.println("--- Reviews ---");
        System.out.println("9. Criar Review");
        System.out.println("10. Buscar Review por ID");
        System.out.println("11. Buscar Reviews de um Usuário");
        System.out.println("12. Buscar Reviews de um Filme");
        System.out.println("13. Excluir Review");
        System.out.println("--- Listas ---");
        System.out.println("14. Criar Lista Personalizada");
        System.out.println("15. Adicionar Filme a uma Lista");
        System.out.println("16. Remover Filme de uma Lista");
        System.out.println("17. Ver todas as Listas de um Usuário");
        System.out.println("18. Excluir Lista");
        System.out.println("------------------------------------");
        System.out.println("auto. Executar Teste Automático Completo");
        System.out.println("0. Sair");
    }

    private static void criarUser() {
        try {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Descrição: ");
            String desc = scanner.nextLine();
            System.out.print("Senha: ");
            String pass = scanner.nextLine();
            User user = new User(0, username, desc, pass);
            int novoId = userDAO.incluirUser(user);
            System.out.println("SUCESSO: User criado com ID " + novoId + ": " + user);
        } catch (Exception e) {
            System.err.println("ERRO ao criar user: " + e.getMessage());
        }
    }

    private static void buscarUserPorId() {
        try {
            System.out.print("ID do User a buscar: ");
            int id = Integer.parseInt(scanner.nextLine());
            User user = userDAO.buscarUser(id);
            if (user != null) System.out.println("User encontrado: " + user);
            else System.out.println("User com ID " + id + " não encontrado.");
        } catch (Exception e) {
            System.err.println("ERRO ao buscar user: " + e.getMessage());
        }
    }
    
    private static void alterarUser() {
        try {
            System.out.print("ID do User a ser alterado: ");
            int id = Integer.parseInt(scanner.nextLine());
            User user = userDAO.buscarUser(id);
            if (user == null) {
                System.out.println("User com ID " + id + " não encontrado.");
                return;
            }
            System.out.println("Dados atuais: " + user);
            System.out.print("Novo nome de usuário (deixe em branco para não alterar): ");
            String novoUsername = scanner.nextLine();
            if (!novoUsername.isEmpty()) user.setUsername(novoUsername);
            
            userDAO.alterarUser(user);
            System.out.println("SUCESSO: User alterado: " + user);
        } catch (Exception e) {
            System.err.println("ERRO ao alterar user: " + e.getMessage());
        }
    }

    private static void excluirUser() {
        try {
            System.out.print("ID do User a ser excluído: ");
            int id = Integer.parseInt(scanner.nextLine());
            if (userDAO.excluirUser(id)) {
                System.out.println("SUCESSO: User com ID " + id + " foi excluído.");
            } else {
                System.out.println("FALHA: User com ID " + id + " não encontrado.");
            }
        } catch (Exception e) {
            System.err.println("ERRO ao excluir user: " + e.getMessage());
        }
    }

    private static void criarFilme() {
        try {
            System.out.print("Título: ");
            String titulo = scanner.nextLine();
            System.out.print("Data de Lançamento (AAAA-MM-DD): ");
            LocalDate releaseDate = LocalDate.parse(scanner.nextLine());
            System.out.print("Score (0-100): ");
            int score = Integer.parseInt(scanner.nextLine());
            System.out.print("Duração (em minutos): ");
            long duration = Long.parseLong(scanner.nextLine());
            System.out.print("Classificação Indicativa (rating): ");
            int rating = Integer.parseInt(scanner.nextLine());

            ArrayList<String> directors = new ArrayList<>();
            while (true) {
                System.out.print("Adicionar diretor (ou deixe em branco para terminar): ");
                String director = scanner.nextLine();
                if (director.isEmpty()) break;
                directors.add(director);
            }

            ArrayList<String> actors = new ArrayList<>();
            while (true) {
                System.out.print("Adicionar ator (ou deixe em branco para terminar): ");
                String actor = scanner.nextLine();
                if (actor.isEmpty()) break;
                actors.add(actor);
            }
            
            Filme filme = new Filme(score, titulo, releaseDate, directors, actors, rating, duration);

            int novoId = filmeDAO.incluirFilme(filme);
            System.out.println("SUCESSO: Filme criado com o ID gerado automaticamente: " + novoId);
            System.out.println("Dados: " + filme);

        } catch (Exception e) {
            System.err.println("ERRO ao criar filme: " + e.getMessage());
        }
    }

    private static void buscarFilmePorId() {
        try {
            System.out.print("ID do Filme a buscar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Filme filme = filmeDAO.buscarFilme(id);
            if (filme != null) System.out.println("Filme encontrado: " + filme);
            else System.out.println("Filme com ID " + id + " não encontrado.");
        } catch (Exception e) {
            System.err.println("ERRO ao buscar filme: " + e.getMessage());
        }
    }

    private static void alterarFilme() {
        try {
            System.out.print("ID do Filme a ser alterado: ");
            int id = Integer.parseInt(scanner.nextLine());
            Filme filme = filmeDAO.buscarFilme(id);
            if (filme == null) {
                System.out.println("Filme com ID " + id + " não encontrado.");
                return;
            }
            System.out.println("Dados atuais: " + filme);
            System.out.print("Novo título (deixe em branco para não alterar): ");
            String novoTitulo = scanner.nextLine();
            if (!novoTitulo.isEmpty()) filme.setTitle(novoTitulo);

            filmeDAO.alterarFilme(filme);
            System.out.println("SUCESSO: Filme alterado: " + filme);
        } catch (Exception e) {
            System.err.println("ERRO ao alterar filme: " + e.getMessage());
        }
    }

    private static void excluirFilme() {
        try {
            System.out.print("ID do Filme a ser excluído: ");
            int id = Integer.parseInt(scanner.nextLine());
            if (filmeDAO.excluirFilme(id)) {
                System.out.println("SUCESSO: Filme com ID " + id + " foi excluído.");
            } else {
                System.out.println("FALHA: Filme com ID " + id + " não encontrado.");
            }
        } catch (Exception e) {
            System.err.println("ERRO ao excluir filme: " + e.getMessage());
        }
    }
    
    private static void criarReview() {
        try {
            Review review = new Review();
            System.out.print("ID do Usuário que está avaliando: ");
            review.setUserId(Integer.parseInt(scanner.nextLine()));
            System.out.print("ID do Filme a ser avaliado: ");
            review.setFilmeId(Integer.parseInt(scanner.nextLine()));
            System.out.print("Nota (0.0 a 10.0): ");
            review.setNota(Float.parseFloat(scanner.nextLine()));
            System.out.print("Comentário: ");
            review.setComentario(scanner.nextLine());
            review.setData(new Date());
            int novoId = reviewDAO.create(review);
            System.out.println("SUCESSO: Review criada com ID " + novoId + ": " + review);
        } catch (Exception e) {
            System.err.println("ERRO ao criar review: " + e.getMessage());
        }
    }
    
    private static void buscarReviewPorId() {
         try {
            System.out.print("ID da Review a buscar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Review review = reviewDAO.read(id);
            if (review != null) System.out.println("Review encontrada: " + review);
            else System.out.println("Review com ID " + id + " não encontrada.");
        } catch (Exception e) {
            System.err.println("ERRO ao buscar review: " + e.getMessage());
        }
    }

    private static void buscarReviewsPorUsuario() {
        try {
            System.out.print("ID do Usuário para buscar as reviews: ");
            int userId = Integer.parseInt(scanner.nextLine());
            List<Review> reviews = reviewDAO.buscarAvaliacoesPorUsuario(userId);
            if (reviews.isEmpty()) {
                System.out.println("Nenhuma review encontrada para o usuário com ID " + userId);
            } else {
                System.out.println("Reviews encontradas para o usuário " + userId + ":");
                reviews.forEach(r -> System.out.println(" - " + r));
            }
        } catch (Exception e) {
            System.err.println("ERRO ao buscar reviews: " + e.getMessage());
        }
    }

    private static void buscarReviewsPorFilme() {
        try {
            System.out.print("ID do Filme para buscar as reviews: ");
            int filmeId = Integer.parseInt(scanner.nextLine());
            List<Review> reviews = reviewDAO.buscarAvaliacoesPorFilme(filmeId);
            if (reviews.isEmpty()) {
                System.out.println("Nenhuma review encontrada para o filme com ID " + filmeId);
            } else {
                System.out.println("Reviews encontradas para o filme " + filmeId + ":");
                reviews.forEach(r -> System.out.println(" - " + r));
            }
        } catch (Exception e) {
            System.err.println("ERRO ao buscar reviews: " + e.getMessage());
        }
    }
    
    private static void excluirReview() {
        try {
            System.out.print("ID da Review a ser excluída: ");
            int id = Integer.parseInt(scanner.nextLine());
            if (reviewDAO.delete(id)) {
                System.out.println("SUCESSO: Review com ID " + id + " foi excluída.");
            } else {
                System.out.println("FALHA: Review com ID " + id + " não encontrada.");
            }
        } catch (Exception e) {
            System.err.println("ERRO ao excluir review: " + e.getMessage());
        }
    }
    
    private static void criarListaPersonalizada() {
        try {
            System.out.print("ID do Usuário que vai criar a lista: ");
            int userId = Integer.parseInt(scanner.nextLine());
            System.out.print("Nome da nova lista: ");
            String nome = scanner.nextLine();
            Lista lista = new Lista(0, userId, nome, false);
            int novoId = listaDAO.incluirLista(lista);
            System.out.println("SUCESSO: Lista personalizada '" + nome + "' criada com ID " + novoId);
        } catch (Exception e) {
            System.err.println("ERRO ao criar lista: " + e.getMessage());
        }
    }
    
    private static void adicionarFilmeNaLista() {
        try {
            System.out.print("ID da Lista onde o filme será adicionado: ");
            int listaId = Integer.parseInt(scanner.nextLine());
            System.out.print("ID do Filme a ser adicionado: ");
            int filmeId = Integer.parseInt(scanner.nextLine());
            listaDAO.adicionarFilmeEmLista(listaId, filmeId);
            System.out.println("SUCESSO: Filme " + filmeId + " adicionado à lista " + listaId);
        } catch (Exception e) {
            System.err.println("ERRO ao adicionar filme na lista: " + e.getMessage());
        }
    }
    
    private static void removerFilmeDaLista() {
        try {
            System.out.print("ID da Lista de onde o filme será removido: ");
            int listaId = Integer.parseInt(scanner.nextLine());
            System.out.print("ID do Filme a ser removido: ");
            int filmeId = Integer.parseInt(scanner.nextLine());

            if (listaDAO.removerFilmeDaLista(listaId, filmeId)) {
                System.out.println("SUCESSO: Filme " + filmeId + " removido da lista " + listaId);
            } else {
                System.out.println("FALHA: Relação entre filme " + filmeId + " e lista " + listaId + " não encontrada.");
            }
        } catch (Exception e) {
            System.err.println("ERRO ao remover filme da lista: " + e.getMessage());
        }
    }
    
    private static void verListasCompletasDoUsuario() {
        try {
            System.out.print("ID do Usuário para ver as listas: ");
            int userId = Integer.parseInt(scanner.nextLine());
            List<Lista> listas = listaDAO.buscarListasPorUsuario(userId, filmeDAO);
            if (listas.isEmpty()) {
                System.out.println("Nenhuma lista encontrada para o usuário " + userId);
            } else {
                System.out.println("--- Listas do Usuário " + userId + " ---");
                for (Lista lista : listas) {
                    System.out.println("\nLISTA: " + lista.getNome() + " (ID: " + lista.getId() + ", Padrão: " + lista.isPadrao() + ")");
                    if (lista.getFilmes().isEmpty()) {
                        System.out.println("  (Esta lista está vazia)");
                    } else {
                        System.out.println("  Filmes nesta lista:");
                        for (Filme filme : lista.getFilmes()) {
                            System.out.println("    - " + filme.getTitle() + " (ID: " + filme.getId() + ")");
                        }
                    }
                }
                System.out.println("--------------------------");
            }
        } catch (Exception e) {
            System.err.println("ERRO ao buscar listas do usuário: " + e.getMessage());
        }
    }
    
    private static void excluirLista() {
        try {
            System.out.print("ID da Lista a ser excluída: ");
            int id = Integer.parseInt(scanner.nextLine());
            if (listaDAO.excluirLista(id)) {
                System.out.println("SUCESSO: Lista com ID " + id + " foi excluída.");
            } else {
                System.out.println("FALHA: Lista com ID " + id + " não encontrada.");
            }
        } catch (Exception e) {
            System.err.println("ERRO ao excluir lista: " + e.getMessage());
        }
    }
    
    private static void runAutomaticTests() {
        System.out.println("\n\n--- INICIANDO BATERIA DE TESTES AUTOMÁTICOS ---");
        boolean allTestsPassed = true;
        
        try {
            System.out.println("\n-- ETAPA 0: PREPARAÇÃO DO AMBIENTE --");
            cleanupDatabaseFiles();
            if (!initializeDAOs()) throw new Exception("Falha ao reinicializar DAOs.");
            System.out.println("-> Ambiente limpo e DAOs reinicializados.");

            System.out.println("\n-- ETAPA 1: TESTANDO CREATE & READ --");
            User alice = new User(0, "alice", "Gosta de drama", "123");
            int aliceId = userDAO.incluirUser(alice);
            User aliceLida = userDAO.buscarUser(aliceId);
            System.out.println("[VERIFICAÇÃO] Criação do User 'alice' (ID: " + aliceId + ") -> Buscando pelo ID -> Encontrado: " + aliceLida);
            if (aliceLida != null && aliceLida.getId() == aliceId) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }

            ArrayList<String> nolan = new ArrayList<>(List.of("Christopher Nolan"));
            ArrayList<String> inceptionActors = new ArrayList<>(List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"));
            Filme inception = new Filme(88, "A Origem", LocalDate.parse("2010-07-16"), nolan, inceptionActors, 14, 148);
            int inceptionId = filmeDAO.incluirFilme(inception);
            Filme inceptionLido = filmeDAO.buscarFilme(inceptionId);
            System.out.println("[VERIFICAÇÃO] Criação do Filme 'A Origem' (ID: " + inceptionId + ") -> Buscando pelo ID -> Encontrado: " + inceptionLido);
            if (inceptionLido != null && inceptionLido.getId() == inceptionId) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }
            
            System.out.println("\n-- ETAPA 2: TESTANDO UPDATE --");
            System.out.println("[AÇÃO] Alterando username de Alice para 'alice_v2'");
            if (aliceLida != null) {
                aliceLida.setUsername("alice_v2");
                userDAO.alterarUser(aliceLida);
            } else {
                System.out.println("  -> FALHOU: aliceLida é null");
                allTestsPassed = false;
            }
            User aliceAtualizada = userDAO.buscarUser(aliceId);
            System.out.println("[VERIFICAÇÃO] Buscando User ID " + aliceId + " novamente -> Encontrado: " + aliceAtualizada);
            if (aliceAtualizada != null && aliceAtualizada.getUsername().equals("alice_v2")) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }

            System.out.println("\n-- ETAPA 3: TESTANDO CRIAÇÃO DE RELAÇÕES --");
            System.out.println("[AÇÃO] Criando listas padrão para 'alice' (ID: " + aliceId + ")");
            listaDAO.criarListasPadraoParaUsuario(aliceId, filmeDAO);
            List<Lista> listasDaAlice = listaDAO.buscarListasPorUsuario(aliceId, filmeDAO);
            System.out.println("[VERIFICAÇÃO] Buscando listas de 'alice' -> Encontradas " + listasDaAlice.size() + " listas.");
            if (listasDaAlice.size() >= 3) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }

            Lista favoritosDaAlice = listasDaAlice.stream().filter(l -> l.getNome().equalsIgnoreCase("Favoritos")).findFirst().orElseThrow(() -> new Exception("Lista 'Favoritos' não encontrada"));
            
            System.out.println("[AÇÃO] Adicionando filme 'A Origem' à lista 'Favoritos'");
            listaDAO.adicionarFilmeEmLista(favoritosDaAlice.getId(), inceptionId);
            
            System.out.println("[AÇÃO] Criando uma Review de 'alice' para 'A Origem'");
            Review reviewAlice = new Review(); reviewAlice.setUserId(aliceId); reviewAlice.setFilmeId(inceptionId); reviewAlice.setNota(9.5f);
            int reviewAliceId = reviewDAO.create(reviewAlice);
            System.out.println(" -> ID da Review gerado: " + reviewAliceId);

            System.out.println("\n-- ETAPA 4: VERIFICANDO BUSCAS POR RELAÇÕES --");
            Lista favoritosVerificada = listaDAO.buscarListaCompleta(favoritosDaAlice.getId(), filmeDAO);
            System.out.println("[VERIFICAÇÃO] Buscando a lista 'Favoritos' de 'alice' com seus filmes. Filmes encontrados: " + favoritosVerificada.getFilmes().size());
            if (favoritosVerificada.getFilmes().size() == 1) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }
            
            List<Review> reviewsDaAlice = reviewDAO.buscarAvaliacoesPorUsuario(aliceId);
            System.out.println("[VERIFICAÇÃO] Buscando reviews do usuário 'alice'. Reviews encontradas: " + reviewsDaAlice.size());
            if (reviewsDaAlice.size() == 1) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }

            System.out.println("\n-- ETAPA 5: TESTANDO EXCLUSÃO DE RELAÇÃO --");
            System.out.println("[AÇÃO] Removendo filme 'A Origem' (ID: " + inceptionId + ") da lista 'Favoritos' (ID: " + favoritosDaAlice.getId() + ")");
            boolean relacaoRemovida = listaDAO.removerFilmeDaLista(favoritosDaAlice.getId(), inceptionId);
            Lista favoritosAposRemocao = listaDAO.buscarListaCompleta(favoritosDaAlice.getId(), filmeDAO);
            System.out.println("[VERIFICAÇÃO] Buscando a lista 'Favoritos' novamente. Filmes encontrados: " + favoritosAposRemocao.getFilmes().size());
            if (relacaoRemovida && favoritosAposRemocao.getFilmes().isEmpty()) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }

            System.out.println("\n-- ETAPA 6: TESTANDO EXCLUSÃO DE ENTIDADE --");
            System.out.println("[AÇÃO] Deletando a Review com ID " + reviewAliceId + "...");
            boolean reviewDeleted = reviewDAO.delete(reviewAliceId);
            Review reviewAposDelete = reviewDAO.read(reviewAliceId);
            System.out.println("[VERIFICAÇÃO] Tentando buscar a review deletada -> Encontrado: " + reviewAposDelete);
            if (reviewDeleted && reviewAposDelete == null) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }
            
            System.out.println("[AÇÃO] Deletando o Filme com ID " + inceptionId + "...");
            boolean filmeDeleted = filmeDAO.excluirFilme(inceptionId);
            Filme filmeAposDelete = filmeDAO.buscarFilme(inceptionId);
            System.out.println("[VERIFICAÇÃO] Tentando buscar o filme deletado -> Encontrado: " + filmeAposDelete);
            if (filmeDeleted && filmeAposDelete == null) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }

        } catch (Exception e) {
            System.err.println("\n--- UM ERRO INESPERADO OCORREU DURANTE OS TESTES ---");
            e.printStackTrace();
            allTestsPassed = false;
        } finally {
            System.out.println("\n--- RESULTADO FINAL DOS TESTES AUTOMÁTICOS ---");
            if (allTestsPassed) {
                System.out.println(">>>> SUCESSO: TODOS OS TESTES PASSARAM <<<<");
            } else {
                System.out.println(">>>> FALHA: UM OU MAIS TESTES FALHARAM <<<<");
            }
            System.out.println("----------------------------------------------");
        }
    }

    private static void cleanupDatabaseFiles() {
        System.out.println("-> Deletando arquivos de banco de dados antigos...");
        File dataDir = new File("data");
        if (dataDir.exists() && dataDir.isDirectory()) {
            File[] files = dataDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().endsWith(".db")) {
                        f.delete();
                    }
                }
            }
        }
    }
}