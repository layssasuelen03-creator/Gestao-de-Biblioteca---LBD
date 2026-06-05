package br.com.biblioteca.dao;

import br.com.biblioteca.connection.ConnectionFactory;
import br.com.biblioteca.model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Tabela livro 
public class LivroDAO {

    public void inserir(Livro l) throws SQLException {
        String sql = "INSERT INTO livro (titulo, ISBN, ano_publicacao, status, id_categoria, id_autor, id_editora) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getIsbn());
            ps.setDate  (3, l.getAnoPublicacao() != null ? Date.valueOf(l.getAnoPublicacao()) : null);
            ps.setString(4, l.getStatus() != null ? l.getStatus() : "disponível");
            ps.setInt   (5, l.getIdCategoria());
            ps.setInt   (6, l.getIdAutor());
            ps.setInt   (7, l.getIdEditora());
            ps.executeUpdate();
        }
    }

    public List<Livro> listarTodos() throws SQLException {
        List<Livro> lista = new ArrayList<>();
        String sql =
            "SELECT l.id_livro, l.titulo, l.ISBN, l.ano_publicacao, l.status, " +
            "       l.id_categoria, l.id_autor, l.id_editora, " +
            "       a.nome AS nome_autor, cat.nome AS nome_categoria, e.nome AS nome_editora " +
            "FROM livro l " +
            "LEFT JOIN autor    a   ON l.id_autor     = a.id_autor " +
            "LEFT JOIN categoria cat ON l.id_categoria = cat.id_Categoria " +
            "LEFT JOIN editora  e   ON l.id_editora   = e.id_editora " +
            "ORDER BY l.titulo";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Livro> buscar(String termo) throws SQLException {
        List<Livro> lista = new ArrayList<>();
        String sql =
            "SELECT l.id_livro, l.titulo, l.ISBN, l.ano_publicacao, l.status, " +
            "       l.id_categoria, l.id_autor, l.id_editora, " +
            "       a.nome AS nome_autor, cat.nome AS nome_categoria, e.nome AS nome_editora " +
            "FROM livro l " +
            "LEFT JOIN autor    a   ON l.id_autor     = a.id_autor " +
            "LEFT JOIN categoria cat ON l.id_categoria = cat.id_Categoria " +
            "LEFT JOIN editora  e   ON l.id_editora   = e.id_editora " +
            "WHERE l.titulo LIKE ? OR a.nome LIKE ? OR cat.nome LIKE ? " +
            "ORDER BY l.titulo";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String t = "%" + termo + "%";
            ps.setString(1, t); ps.setString(2, t); ps.setString(3, t);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Livro buscarPorId(int id) throws SQLException {
        String sql =
            "SELECT l.id_livro, l.titulo, l.ISBN, l.ano_publicacao, l.status, " +
            "       l.id_categoria, l.id_autor, l.id_editora, " +
            "       a.nome AS nome_autor, cat.nome AS nome_categoria, e.nome AS nome_editora " +
            "FROM livro l " +
            "LEFT JOIN autor    a   ON l.id_autor     = a.id_autor " +
            "LEFT JOIN categoria cat ON l.id_categoria = cat.id_Categoria " +
            "LEFT JOIN editora  e   ON l.id_editora   = e.id_editora " +
            "WHERE l.id_livro = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public void atualizar(Livro l) throws SQLException {
        String sql = "UPDATE livro SET titulo = ?, ISBN = ?, ano_publicacao = ?, status = ?, " +
                     "id_categoria = ?, id_autor = ?, id_editora = ? WHERE id_livro = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getIsbn());
            ps.setDate  (3, l.getAnoPublicacao() != null ? Date.valueOf(l.getAnoPublicacao()) : null);
            ps.setString(4, l.getStatus());
            ps.setInt   (5, l.getIdCategoria());
            ps.setInt   (6, l.getIdAutor());
            ps.setInt   (7, l.getIdEditora());
            ps.setInt   (8, l.getId());
            ps.executeUpdate();
        }
    }

    public void atualizarStatus(int id, String status) throws SQLException {
        String sql = "UPDATE livro SET status = ? WHERE id_livro = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt   (2, id);
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM livro WHERE id_livro = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM livro";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int contarDisponiveis() throws SQLException {
        String sql = "SELECT COUNT(*) FROM livro WHERE status = 'disponível'";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private Livro mapear(ResultSet rs) throws SQLException {
        Livro l = new Livro();
        l.setId           (rs.getInt   ("id_livro"));
        l.setTitulo       (rs.getString("titulo"));
        l.setIsbn         (rs.getString("ISBN"));
        Date d = rs.getDate("ano_publicacao");
        if (d != null) l.setAnoPublicacao(d.toLocalDate());
        l.setStatus       (rs.getString("status"));
        l.setIdCategoria  (rs.getInt   ("id_categoria"));
        l.setIdAutor      (rs.getInt   ("id_autor"));
        l.setIdEditora    (rs.getInt   ("id_editora"));
        l.setNomeAutor    (rs.getString("nome_autor"));
        l.setNomeCategoria(rs.getString("nome_categoria"));
        l.setNomeEditora  (rs.getString("nome_editora"));
        return l;
    }
}