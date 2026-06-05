package br.com.biblioteca.dao;

import br.com.biblioteca.connection.ConnectionFactory;
import br.com.biblioteca.model.Autor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Tabela autor 
public class AutorDAO {

    public void inserir(Autor a) throws SQLException {
        String sql = "INSERT INTO autor (nome, nacionalidade) VALUES (?, ?)";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getNacionalidade());
            ps.executeUpdate();
        }
    }

    public List<Autor> listarTodos() throws SQLException {
        List<Autor> lista = new ArrayList<>();
        String sql = "SELECT id_autor, nome, nacionalidade FROM autor ORDER BY nome";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(new Autor(rs.getInt("id_autor"), rs.getString("nome"), rs.getString("nacionalidade")));
        }
        return lista;
    }

    public Autor buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_autor, nome, nacionalidade FROM autor WHERE id_autor = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return new Autor(rs.getInt("id_autor"), rs.getString("nome"), rs.getString("nacionalidade"));
            }
        }
        return null;
    }

    public void atualizar(Autor a) throws SQLException {
        String sql = "UPDATE autor SET nome = ?, nacionalidade = ? WHERE id_autor = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getNacionalidade());
            ps.setInt   (3, a.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM autor WHERE id_autor = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM autor";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
}