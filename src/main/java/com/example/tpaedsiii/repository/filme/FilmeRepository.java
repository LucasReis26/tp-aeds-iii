package com.example.tpaedsiii.repository.filme;

import com.example.tpaedsiii.model.filme.Filme;
import com.example.tpaedsiii.repository.bd.indexes.base.HashExtensivel;
import com.example.tpaedsiii.repository.bd.indexes.base.ArvoreBMais;
import com.example.tpaedsiii.repository.bd.indexes.ParesArvoreB.ParIntInt;
import com.example.tpaedsiii.repository.bd.indexes.ParesArvoreB.ParStringInt;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.ArrayList;

@Repository
public class FilmeRepository {

    // Ferramenta 1: Armazenamento principal para acesso ultra-rápido por ID (PK).
    private HashExtensivel<Filme> hashFilmes;
    
    // Ferramenta 2: Índice secundário para busca otimizada por título.
    private ArvoreBMais<ParStringInt> idxTituloFilme;

    // Ferramenta 3: Índice secundário para busca por faixa de notas (score).
    private ArvoreBMais<ParIntInt> idxScoreFilme;

    public FilmeRepository() {
        // O construtor permanece vazio para o Spring.
    }
    
    @PostConstruct
    public void init() throws Exception {
        // Garante que o diretório de dados exista.
        new File("data").mkdirs();

        // Inicializa o armazenamento principal de filmes.
        hashFilmes = new HashExtensivel<>(
            Filme.class.getConstructor(), 
            4, 
            "data/filmes_d.db", 
            "data/filmes_c.db"
        );
        
        // Inicializa o índice secundário de Árvore B+ para os títulos.
        idxTituloFilme = new ArvoreBMais<>(
            ParStringInt.class.getConstructor(), 
            5, // Ordem da árvore
            "data/idx_titulo_filme.db"
        );

        // Inicializa o índice secundário de Árvore B+ para as notas.
        idxScoreFilme = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            5,
            "data/idx_score_filme.db"
        );
    }

    /**
     * Cria um novo filme, salvando-o no armazenamento principal e sincronizando todos os índices secundários.
     */
    public int incluirFilme(Filme filme) throws Exception {
        // 1. Cria o filme no hash principal para obter o ID gerado.
        int novoId = hashFilmes.create(filme);
        filme.setId(novoId); // Garante que o objeto em memória tenha o ID correto.
        
        // 2. Cria e insere a "ficha" no índice de títulos.
        //    Armazena o título em minúsculas para buscas case-insensitive.
        idxTituloFilme.create(new ParStringInt(filme.getTitle().toLowerCase(), novoId));
        
        // 3. Cria e insere a "ficha" no índice de notas.
        idxScoreFilme.create(new ParIntInt(filme.getScore(), novoId));
        
        return novoId;
    }

    /**
     * Busca um filme pelo seu ID usando o acesso direto do Hash.
     */
    public Filme buscarFilme(int id) throws Exception {
        return hashFilmes.read(id);
    }

    /**
     * Atualiza um filme.
     * Esta operação complexa requer a remoção e reinserção nos índices secundários
     * para garantir a consistência dos dados.
     */
    public boolean alterarFilme(Filme filme) throws Exception {
        Filme filmeAntigo = buscarFilme(filme.getId());
        if (filmeAntigo == null) {
            throw new Exception("Filme com ID " + filme.getId() + " não encontrado para alteração.");
        }

        // 1. Remove as entradas antigas dos índices secundários.
        idxTituloFilme.delete(new ParStringInt(filmeAntigo.getTitle().toLowerCase(), filme.getId()));
        idxScoreFilme.delete(new ParIntInt(filmeAntigo.getScore(), filme.getId()));

        // 2. Atualiza o registo principal no hash.
        boolean sucesso = hashFilmes.update(filme);

        // 3. Reinsere as novas entradas nos índices secundários.
        if (sucesso) {
            idxTituloFilme.create(new ParStringInt(filme.getTitle().toLowerCase(), filme.getId()));
            idxScoreFilme.create(new ParIntInt(filme.getScore(), filme.getId()));
        }
        return sucesso;
    }

    /**
     * Exclui um filme, removendo-o do armazenamento principal e de todos os índices secundários.
     */
    public boolean excluirFilme(int id) throws Exception {
        Filme filmeParaExcluir = buscarFilme(id);
        if (filmeParaExcluir == null) {
            return false;
        }
        
        // 1. Remove as "fichas" dos índices secundários.
        idxTituloFilme.delete(new ParStringInt(filmeParaExcluir.getTitle().toLowerCase(), id));
        idxScoreFilme.delete(new ParIntInt(filmeParaExcluir.getScore(), id));
        
        // 2. Remove o dado principal do hash.
        return hashFilmes.delete(id);
    }

    /**
     * MÉTODO OTIMIZADO: Busca filmes por título exato usando a Árvore B+.
     */
    public List<Filme> buscarPorTitulo(String termoBusca) throws Exception {
        ArrayList<ParStringInt> pares = idxTituloFilme.read(new ParStringInt(termoBusca.toLowerCase(), 0));
        
        List<Filme> resultados = new ArrayList<>();
        for (ParStringInt par : pares) {
            if (par.getChave().equals(termoBusca.toLowerCase())) {
                 Filme filme = buscarFilme(par.getValor());
                 if (filme != null) {
                    resultados.add(filme);
                 }
            }
        }
        return resultados;
    }

    /**
     * NOVO MÉTODO OTIMIZADO: Busca filmes por faixa de notas usando a Árvore B+.
     * @param notaMin A nota mínima (inclusiva).
     * @param notaMax A nota máxima (inclusiva).
     * @return Uma lista de filmes dentro da faixa de notas.
     */
    public List<Filme> buscarPorFaixaDeScore(int notaMin, int notaMax) throws Exception {
        List<ParIntInt> pares = idxScoreFilme.readRange(new ParIntInt(notaMin, 0), new ParIntInt(notaMax, Integer.MAX_VALUE));
        
        List<Filme> resultados = new ArrayList<>();
        for (ParIntInt par : pares) {
            Filme filme = buscarFilme(par.getValor());
            if (filme != null) {
                resultados.add(filme);
            }
        }
        
        return resultados;
    }
}

