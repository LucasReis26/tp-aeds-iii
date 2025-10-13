package com.example.tpaedsiii.repository;

import com.example.tpaedsiii.repository.bd.filme.Filme;
import com.example.tpaedsiii.repository.bd.lista.Lista;
import com.example.tpaedsiii.repository.bd.review.Review;
import com.example.tpaedsiii.repository.bd.user.User;
import com.example.tpaedsiii.repository.dao.filme.FilmeDAO;
import com.example.tpaedsiii.repository.dao.lista.ListaDAO;
import com.example.tpaedsiii.repository.dao.review.ReviewDAO;
import com.example.tpaedsiii.repository.dao.user.UserDAO;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ConsoleTestRunner {

    private static FilmeDAO filmeDAO;
    private static ReviewDAO reviewDAO;
    private static ListaDAO listaDAO;
    private static UserDAO userDAO;
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
            filmeDAO = new FilmeDAO();
            reviewDAO = new ReviewDAO();
            listaDAO = new ListaDAO();
            userDAO = new UserDAO();
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
            case 16: verListasCompletasDoUsuario(); break;
            case 17: excluirLista(); break;
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
        System.out.println("16. Ver todas as Listas de um Usuário");
        System.out.println("17. Excluir Lista");
        System.out.println("------------------------------------");
        System.out.println("auto. Executar Teste Automático Completo");
        System.out.println("0. Sair");
    }

    // --- MÉTODOS DE TESTE MANUAIS COMPLETOS ---
    
    private static void criarUser() { /* ... Código das respostas anteriores ... */ }
    private static void buscarUserPorId() { /* ... Código das respostas anteriores ... */ }
    private static void alterarUser() { /* ... Código das respostas anteriores ... */ }
    private static void excluirUser() { /* ... Código das respostas anteriores ... */ }
    private static void criarFilme() { /* ... Código das respostas anteriores ... */ }
    private static void buscarFilmePorId() { /* ... Código das respostas anteriores ... */ }
    private static void alterarFilme() { /* ... Código das respostas anteriores ... */ }
    private static void excluirFilme() { /* ... Código das respostas anteriores ... */ }
    private static void criarReview() { /* ... Código das respostas anteriores ... */ }
    private static void buscarReviewPorId() { /* ... Código das respostas anteriores ... */ }
    private static void excluirReview() { /* ... Código das respostas anteriores ... */ }
    private static void buscarReviewsPorUsuario() { /* ... Código das respostas anteriores ... */ }
    private static void buscarReviewsPorFilme() { /* ... Código das respostas anteriores ... */ }
    private static void criarListaPersonalizada() { /* ... Código das respostas anteriores ... */ }
    private static void adicionarFilmeNaLista() { /* ... Código das respostas anteriores ... */ }
    private static void verListasCompletasDoUsuario() { /* ... Código das respostas anteriores ... */ }
    private static void excluirLista() { /* ... Código das respostas anteriores ... */ }


    // --- MÉTODO DE TESTE AUTOMÁTICO COMPLETO E DETALHADO ---
    
    private static void runAutomaticTests() {
        System.out.println("\n\n--- INICIANDO BATERIA DE TESTES AUTOMÁTICOS ---");
        boolean allTestsPassed = true;
        
        try {
            System.out.println("\n-- ETAPA 0: PREPARAÇÃO DO AMBIENTE --");
            cleanupDatabaseFiles();
            if (!initializeDAOs()) throw new Exception("Falha ao reinicializar DAOs.");
            System.out.println("-> Ambiente limpo e DAOs reinicializados.");

            // --- CRIAÇÃO E LEITURA ---
            System.out.println("\n-- ETAPA 1: TESTANDO CRIAÇÃO E LEITURA (CREATE & READ) --");
            User alice = new User(0, "alice", "Gosta de drama", "123");
            System.out.println("[AÇÃO] Criando User: " + alice);
            int aliceId = userDAO.incluirUser(alice);
            alice.setId(aliceId);
            System.out.println(" -> ID gerado: " + aliceId);
            User aliceLida = userDAO.buscarUser(aliceId);
            System.out.println("[VERIFICAÇÃO] Buscando User pelo ID " + aliceId + " -> Encontrado: " + aliceLida);
            if (aliceLida != null && aliceLida.getId() == aliceId) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }

            Filme inception = new Filme(); inception.setTitle("A Origem"); inception.setReleaseDate(LocalDate.parse("2010-07-16"));
            System.out.println("[AÇÃO] Criando Filme: " + inception.getTitle());
            int inceptionId = filmeDAO.incluirFilme(inception);
            inception.setId(inceptionId);
            System.out.println(" -> ID gerado: " + inceptionId);
            Filme inceptionLido = filmeDAO.buscarFilme(inceptionId);
            System.out.println("[VERIFICAÇÃO] Buscando Filme pelo ID " + inceptionId + " -> Encontrado: " + inceptionLido);
            if (inceptionLido != null && inceptionLido.getId() == inceptionId) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }
            
            // --- ATUALIZAÇÃO ---
            System.out.println("\n-- ETAPA 2: TESTANDO ATUALIZAÇÃO (UPDATE) --");
            System.out.println("[AÇÃO] Alterando username de Alice para 'alice_v2'");
            alice.setUsername("alice_v2");
            userDAO.alterarUser(alice);
            User aliceAtualizada = userDAO.buscarUser(aliceId);
            System.out.println("[VERIFICAÇÃO] Buscando User ID " + aliceId + " novamente -> Encontrado: " + aliceAtualizada);
            if (aliceAtualizada != null && aliceAtualizada.getUsername().equals("alice_v2")) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }

            // --- CRIAÇÃO DE RELAÇÕES ---
            System.out.println("\n-- ETAPA 3: TESTANDO CRIAÇÃO DE RELAÇÕES (1-N & N-N) --");
            System.out.println("[AÇÃO] Criando listas padrão para 'alice' (ID: " + aliceId + ")");
            listaDAO.criarListasPadraoParaUsuario(aliceId, filmeDAO);
            List<Lista> listasDaAlice = listaDAO.buscarListasPorUsuario(aliceId, filmeDAO);
            System.out.println("[VERIFICAÇÃO] Buscando listas de 'alice' -> Encontradas " + listasDaAlice.size() + " listas.");
            if (listasDaAlice.size() >= 3) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }

            Lista favoritosDaAlice = listasDaAlice.stream().filter(l -> l.getNome().equalsIgnoreCase("Favoritos")).findFirst().orElse(null);
            if (favoritosDaAlice == null) throw new Exception("Lista 'Favoritos' não encontrada para Alice");
            
            System.out.println("[AÇÃO] Adicionando filme 'A Origem' (ID: " + inceptionId + ") à lista 'Favoritos' (ID: " + favoritosDaAlice.getId() + ")");
            listaDAO.adicionarFilmeEmLista(favoritosDaAlice.getId(), inceptionId);
            
            System.out.println("[AÇÃO] Criando uma Review de 'alice' para 'A Origem'");
            Review reviewAlice = new Review(); reviewAlice.setUserId(aliceId); reviewAlice.setFilmeId(inceptionId); reviewAlice.setNota(9.5f);
            int reviewAliceId = reviewDAO.create(reviewAlice);
            System.out.println(" -> ID da Review gerado: " + reviewAliceId);

            // --- VERIFICAÇÃO DE RELAÇÕES ---
            System.out.println("\n-- ETAPA 4: VERIFICANDO BUSCAS POR RELAÇÕES --");
            System.out.println("[VERIFICAÇÃO] Buscando a lista 'Favoritos' de 'alice' com seus filmes...");
            Lista favoritosVerificada = listaDAO.buscarListaCompleta(favoritosDaAlice.getId(), filmeDAO);
            System.out.println(" -> Lista encontrada: " + favoritosVerificada);
            System.out.println(" -> Filmes dentro da lista: " + favoritosVerificada.getFilmes().size());
            if (favoritosVerificada.getFilmes().size() == 1 && favoritosVerificada.getFilmes().get(0).getId() == inceptionId) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }
            
            System.out.println("[VERIFICAÇÃO] Buscando reviews do usuário 'alice' (ID: " + aliceId + ")...");
            List<Review> reviewsDaAlice = reviewDAO.buscarAvaliacoesPorUsuario(aliceId);
            System.out.println(" -> Encontradas " + reviewsDaAlice.size() + " reviews.");
            if (reviewsDaAlice.size() == 1 && reviewsDaAlice.get(0).getId() == reviewAliceId) { System.out.println("  -> PASSOU"); }
            else { System.out.println("  -> FALHOU"); allTestsPassed = false; }

            // --- EXCLUSÃO ---
            System.out.println("\n-- ETAPA 5: TESTANDO EXCLUSÃO (DELETE) --");
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
            
            // Verificação final da lista, que agora deve estar vazia
            Lista favoritosFinal = listaDAO.buscarListaCompleta(favoritosDaAlice.getId(), filmeDAO);
            System.out.println("[VERIFICAÇÃO FINAL] Buscando lista 'Favoritos' novamente. Filmes encontrados: " + favoritosFinal.getFilmes().size());
            if (favoritosFinal.getFilmes().isEmpty()) { System.out.println("  -> PASSOU (Filme removido da lista implicitamente após exclusão)"); }
            else { System.out.println("  -> FALHOU (Filme ainda consta na lista)"); allTestsPassed = false; }

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