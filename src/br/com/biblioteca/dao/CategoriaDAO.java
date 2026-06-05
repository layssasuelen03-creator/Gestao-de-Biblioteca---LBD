package br.com.biblioteca.dao;

import br.com.biblioteca.connection.ConnectionFactory;
import br.com.biblioteca.model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Tabela categoria 
public class CategoriaDAO {

    public void inserir(Categoria cat) throws SQLException {
        String sql = "INSERT INTO categoria (nome, descricao) VALUES (?, ?)";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cat.getNome());
            ps.setString(2, cat.getDescricao());
            ps.executeUpdate();
        }
    }

    public List<Categoria> listarTodos() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_Categoria, nome, descricao FROM categoria ORDER BY nome";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(new Categoria(rs.getInt("id_Categoria"), rs.getString("nome"), rs.getString("descricao")));
        }
        return lista;
    }

    public Categoria buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_Categoria, nome, descricao FROM categoria WHERE id_Categoria = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return new Categoria(rs.getInt("id_Categoria"), rs.getString("nome"), rs.getString("descricao"));
            }
        }
        return null;
    }

    public void atualizar(Categoria cat) throws SQLException {
        String sql = "UPDATE categoria SET nome = ?, descricao = ? WHERE id_Categoria = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cat.getNome());
            ps.setString(2, cat.getDescricao());
            ps.setInt   (3, cat.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id_Categoria = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM categoria";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
}