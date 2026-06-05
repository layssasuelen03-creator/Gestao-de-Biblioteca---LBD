package br.com.biblioteca.dao;

import br.com.biblioteca.connection.ConnectionFactory;
import br.com.biblioteca.model.Emprestimo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Tabela emprestimo | PK: id_emprestimo
public class EmprestimoDAO {

    //Registra o empréstimo e marca como emprestado o livros
    public void inserir(Emprestimo e) throws SQLException {
        Connection c = ConnectionFactory.getConnection();
        try {
            c.setAutoCommit(false);

            //Verifica se o livro está disponível
            PreparedStatement chk = c.prepareStatement(
                "SELECT status FROM livro WHERE id_livro = ? FOR UPDATE");
            chk.setInt(1, e.getLivroId());
            ResultSet rs = chk.executeQuery();
            if (!rs.next() || !"disponível".equalsIgnoreCase(rs.getString("status"))) {
                c.rollback();
                throw new SQLException("Livro não disponível para empréstimo.");
            }
            chk.close();

            PreparedStatement ps = c.prepareStatement(
                "INSERT INTO emprestimo (" +
                "data_emprestimo, " +
                "data_prevista_devolucao, " +
                "data_devolucao, " +
                "status, " +
                "id_livro, " +
                "id_usuario" +
                ") VALUES (?, ?, ?, ?, ?, ?)");

            ps.setDate(1, Date.valueOf(e.getDataEmprestimo()));
            ps.setDate(2, Date.valueOf(e.getDataPrevistaDevolucao())); // prevista
            ps.setNull(3, Types.DATE); // devolução
            ps.setString(4, "ativo");
            ps.setInt(5, e.getLivroId());
            ps.setInt(6, e.getUsuarioId());

            System.out.println("Livro: " + e.getLivroId());
            System.out.println("Usuario: " + e.getUsuarioId());
            System.out.println("Data Emprestimo: " + e.getDataEmprestimo());
            System.out.println("Data Prevista: " + e.getDataPrevistaDevolucao());
            System.out.println(ps);
           

            ps.executeUpdate();
            ps.close();

            PreparedStatement upd = c.prepareStatement(
                "UPDATE livro SET status = 'emprestado' WHERE id_livro = ?");
            upd.setInt(1, e.getLivroId());
            upd.executeUpdate();
            upd.close();

            c.commit();
        } catch (SQLException ex) {
            c.rollback();
            throw ex;
        } finally {
            ConnectionFactory.closeConnection(c);
        }
    }

    public List<Emprestimo> listarTodos() throws SQLException {
        List<Emprestimo> lista = new ArrayList<>();
        String sql =
            "SELECT e.id_emprestimo, " +
            "e.data_emprestimo, " +
            "e.data_prevista_devolucao, " +
            "e.data_devolucao, " +
            "e.status, " +
            "e.id_livro, " +
            "e.id_usuario, " +
            "l.titulo AS titulo_livro, " +
            "u.nome AS nome_usuario " +
            "FROM emprestimo e " +
            "JOIN livro l ON e.id_livro = l.id_livro " +
            "JOIN usuario u ON e.id_usuario = u.id_usuario " +
            "ORDER BY e.data_emprestimo DESC";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    //Registra a devolução e restaura livro para disponível
    public void registrarDevolucao(int id) throws SQLException {
        Connection c = ConnectionFactory.getConnection();
        try {
            c.setAutoCommit(false);

            PreparedStatement sel = c.prepareStatement(
                "SELECT id_livro FROM emprestimo WHERE id_emprestimo = ?");
            sel.setInt(1, id);
            ResultSet rs = sel.executeQuery();
            if (!rs.next()) {
                c.rollback();
                throw new SQLException("Empréstimo não encontrado.");
            }
            int livroId = rs.getInt("id_livro");
            sel.close();

            PreparedStatement del = c.prepareStatement(
                "DELETE FROM emprestimo WHERE id_emprestimo = ?");
            del.setInt(1, id);
            del.executeUpdate();
            del.close();

            //Restaura status do livro
            PreparedStatement upd = c.prepareStatement(
                "UPDATE livro SET status = 'disponível' WHERE id_livro = ?");
            upd.setInt(1, livroId);
            upd.executeUpdate();
            upd.close();

            c.commit();
        } catch (SQLException ex) {
            c.rollback();
            throw ex;
        } finally {
            ConnectionFactory.closeConnection(c);
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM emprestimo WHERE id_emprestimo = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int contarAtivos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM emprestimo";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int contarAtrasados() throws SQLException {
        String sql = "SELECT COUNT(*) FROM emprestimo WHERE data_devolucao < CURDATE()";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    
    private Emprestimo mapear(ResultSet rs) throws SQLException {
        Emprestimo e = new Emprestimo();
        e.setId(rs.getInt("id_emprestimo"));
        e.setLivroId(rs.getInt("id_livro"));
        e.setUsuarioId(rs.getInt("id_usuario"));
        e.setTituloLivro(rs.getString("titulo_livro"));
        e.setNomeUsuario(rs.getString("nome_usuario"));
        e.setDataEmprestimo(
              rs.getDate("data_emprestimo").toLocalDate()
        );
        e.setDataPrevistaDevolucao(
             rs.getDate("data_prevista_devolucao").toLocalDate()
        );

       Date dataDev = rs.getDate("data_devolucao");
           if (dataDev != null) {
                e.setDataDevolucao(dataDev.toLocalDate());
            }
       e.setStatus(rs.getString("status"));
       return e;
    }
}