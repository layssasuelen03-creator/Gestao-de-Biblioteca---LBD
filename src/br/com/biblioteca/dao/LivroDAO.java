package br.com.biblioteca.dao;

import br.com.biblioteca.connection.ConnectionFactory;
import br.com.biblioteca.model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class LivroDAO {

    //CREATE 
    public void inserir(Livro l) throws SQLException {
        String sql = "INSERT INTO livros (titulo, autor, genero, ano, total_copias, copias_disponiveis) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getGenero());
            ps.setInt   (4, l.getAno());
            ps.setInt   (5, l.getTotalCopias());
            ps.setInt   (6, l.getCopiasDisponiveis());
            ps.executeUpdate();
        }
    }

    //READ 
    public List<Livro> listarTodos() throws SQLException {
        List<Livro> lista = new ArrayList<>();
        String sql = "SELECT * FROM livros ORDER BY titulo";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Livro> buscar(String termo) throws SQLException {
        List<Livro> lista = new ArrayList<>();
        String sql = "SELECT * FROM livros "
                   + "WHERE titulo LIKE ? OR autor LIKE ? OR genero LIKE ? "
                   + "ORDER BY titulo";
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
        String sql = "SELECT * FROM livros WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    //UPDATE 
    public void atualizar(Livro l) throws SQLException {
        String sql = "UPDATE livros SET titulo=?, autor=?, genero=?, ano=?, "
                   + "total_copias=?, copias_disponiveis=? WHERE id=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getGenero());
            ps.setInt   (4, l.getAno());
            ps.setInt   (5, l.getTotalCopias());
            ps.setInt   (6, l.getCopiasDisponiveis());
            ps.setInt   (7, l.getId());
            ps.executeUpdate();
        }
    }

    //DELETE 
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM livros WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    //Contagem 
    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM livros";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    //Mapper 
    private Livro mapear(ResultSet rs) throws SQLException {
        return new Livro(
            rs.getInt   ("id"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getString("genero"),
            rs.getInt   ("ano"),
            rs.getInt   ("total_copias"),
            rs.getInt   ("copias_disponiveis")
        );
    }
}
