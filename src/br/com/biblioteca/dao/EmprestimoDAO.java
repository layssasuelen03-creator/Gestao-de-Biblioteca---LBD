package br.com.biblioteca.dao;

import br.com.biblioteca.connection.ConnectionFactory;
import br.com.biblioteca.model.Emprestimo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class EmprestimoDAO {

    //CREATE — registra empréstimo e decrementa cópia disponível 
    public void inserir(Emprestimo e) throws SQLException {
        String sql = "INSERT INTO emprestimos (livro_id, usuario_id, data_emprestimo, data_devolucao_prevista, status) "
                   + "VALUES (?,?,?,?,'ativo')";
        Connection c = ConnectionFactory.getConnection();
        try {
            c.setAutoCommit(false);

            //Verifica disponibilidade
            PreparedStatement check = c.prepareStatement(
                "SELECT copias_disponiveis FROM livros WHERE id=? FOR UPDATE");
            check.setInt(1, e.getLivroId());
            ResultSet rs = check.executeQuery();
            if (!rs.next() || rs.getInt(1) < 1) {
                c.rollback();
                throw new SQLException("Livro sem cópias disponíveis.");
            }

            //Insere empréstimo
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt   (1, e.getLivroId());
            ps.setInt   (2, e.getUsuarioId());
            ps.setDate  (3, Date.valueOf(e.getDataEmprestimo()));
            ps.setDate  (4, Date.valueOf(e.getDataDevolucaoPrevista()));
            ps.executeUpdate();

            //Decrementa disponível
            c.prepareStatement(
                "UPDATE livros SET copias_disponiveis = copias_disponiveis - 1 WHERE id=" + e.getLivroId()
            ).executeUpdate();

            c.commit();
        } catch (SQLException ex) {
            c.rollback(); throw ex;
        } finally {
            ConnectionFactory.closeConnection(c);
        }
    }

    //READ 
    public List<Emprestimo> listarTodos() throws SQLException {
        List<Emprestimo> lista = new ArrayList<>();
        String sql =
            "SELECT e.*, l.titulo AS titulo_livro, u.nome AS nome_usuario " +
            "FROM emprestimos e " +
            "JOIN livros l ON e.livro_id = l.id " +
            "JOIN usuarios u ON e.usuario_id = u.id " +
            "ORDER BY e.data_emprestimo DESC";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    //UPDATE — atualiza status e incrementa cópia 
    public void registrarDevolucao(int id) throws SQLException {
        Connection c = ConnectionFactory.getConnection();
        try {
            c.setAutoCommit(false);

            //Busca por livro_id
            PreparedStatement sel = c.prepareStatement(
                "SELECT livro_id FROM emprestimos WHERE id=?");
            sel.setInt(1, id);
            ResultSet rs = sel.executeQuery();
            if (!rs.next()) { c.rollback(); throw new SQLException("Empréstimo não encontrado."); }
            int livroId = rs.getInt("livro_id");

            //Marca devolvido
            PreparedStatement upEmp = c.prepareStatement(
                "UPDATE emprestimos SET data_devolucao_real=CURDATE(), status='devolvido' WHERE id=?");
            upEmp.setInt(1, id);
            upEmp.executeUpdate();

            //Incrementa disponível
            c.prepareStatement(
                "UPDATE livros SET copias_disponiveis = copias_disponiveis + 1 WHERE id=" + livroId
            ).executeUpdate();

            c.commit();
        } catch (SQLException ex) {
            c.rollback(); throw ex;
        } finally {
            ConnectionFactory.closeConnection(c);
        }
    }

    //DELETE 
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM emprestimos WHERE id=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id); ps.executeUpdate();
        }
    }

    //ontagens 
    public int contarAtivos() throws SQLException {
        return contar("SELECT COUNT(*) FROM emprestimos WHERE status='ativo'");
    }
    public int contarAtrasados() throws SQLException {
        return contar(
            "SELECT COUNT(*) FROM emprestimos WHERE status='ativo' AND data_devolucao_prevista < CURDATE()");
    }
    private int contar(String sql) throws SQLException {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    //Mapper 
    private Emprestimo mapear(ResultSet rs) throws SQLException {
        Emprestimo e = new Emprestimo();
        e.setId           (rs.getInt   ("id"));
        e.setLivroId      (rs.getInt   ("livro_id"));
        e.setUsuarioId    (rs.getInt   ("usuario_id"));
        e.setTituloLivro  (rs.getString("titulo_livro"));
        e.setNomeUsuario  (rs.getString("nome_usuario"));
        e.setDataEmprestimo(rs.getDate ("data_emprestimo").toLocalDate());
        e.setDataDevolucaoPrevista(rs.getDate("data_devolucao_prevista").toLocalDate());
        Date devReal = rs.getDate("data_devolucao_real");
        if (devReal != null) e.setDataDevolucaoReal(devReal.toLocalDate());
        e.setStatus       (rs.getString("status"));
        return e;
    }
}
