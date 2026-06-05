package br.com.biblioteca.dao;

import br.com.biblioteca.connection.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


//DAO exclusivo para as consultas do PainelRelatorios.
public class RelatorioDAO {

    //CARDS
    public int contarTitulosUnicos() throws SQLException {
        String sql = "SELECT COUNT(DISTINCT id_livro) FROM livro";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int contarCopiasTotais() throws SQLException {
        String sql = "SELECT COUNT(*) FROM livro";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int contarEmprestimosAtivos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM emprestimo WHERE status = 'ativo'";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int contarEmprestimosAtrasados() throws SQLException {
        String sql = "SELECT COUNT(*) FROM emprestimo "
                   + "WHERE status = 'ativo' "
                   + "  AND data_prevista_devolucao < CURDATE()";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    
    // GRÁFICO: livros por categoria - Vai retornar até 8 categorias com total de livros, ordem decrescente
    public List<Object[]> livrosPorCategoria() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql =
            "SELECT c.nome, COUNT(l.id_livro) AS total "
          + "FROM categoria c "
          + "LEFT JOIN livro l ON c.id_Categoria = l.id_categoria "
          + "GROUP BY c.id_Categoria, c.nome "
          + "ORDER BY total DESC "
          + "LIMIT 8";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("nome"),
                    rs.getInt("total")
                });
            }
        }
        return lista;
    }

    //Resumo de membros - Vai retornar os 10 usuários com mais empréstimos ativos
    public List<Object[]> resumoMembros() throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        String sql =
            "SELECT u.nome, u.status, "
          + "       COUNT(e.id_emprestimo) AS emprestimos_ativos "
          + "FROM usuario u "
          + "LEFT JOIN emprestimo e "
          + "       ON u.id_usuario = e.id_usuario AND e.status = 'ativo' "
          + "GROUP BY u.id_usuario, u.nome, u.status "
          + "ORDER BY emprestimos_ativos DESC, u.nome "
          + "LIMIT 10";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getString("nome"),
                    rs.getInt("emprestimos_ativos"),
                    rs.getString("status")
                });
            }
        }
        return lista;
    }
}