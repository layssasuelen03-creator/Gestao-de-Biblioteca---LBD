package br.com.biblioteca.service;

import br.com.biblioteca.dao.RelatorioDAO;

import java.sql.SQLException;
import java.util.List;


//Responsável por organizar as chamadas ao RelatorioDAO e encapsular regras de negócio em relatórios
public class RelatorioService {

    private final RelatorioDAO dao = new RelatorioDAO();

  
    //CARDS
    public int contarTitulosUnicos() throws SQLException {
        return dao.contarTitulosUnicos();
    }

    public int contarCopiasTotais() throws SQLException {
        return dao.contarCopiasTotais();
    }

    public int contarEmprestimosAtivos() throws SQLException {
        return dao.contarEmprestimosAtivos();
    }

    public int contarEmprestimosAtrasados() throws SQLException {
        return dao.contarEmprestimosAtrasados();
    }

    
    // GRÁFICO: livros por categoria - Vai retornar a lista de categorias com total de livros
    public List<Object[]> livrosPorCategoria() throws SQLException {
        return dao.livrosPorCategoria();
    }

  
    //CARD: resumo de membros - Vai retornar os 10 usuários com mais empréstimos ativos
    public List<Object[]> resumoMembros() throws SQLException {
        return dao.resumoMembros();
    }
}