package br.com.biblioteca.dao;

import br.com.biblioteca.connection.ConnectionFactory;
import br.com.biblioteca.model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CategoriaDAO {

    public void inserir(Categoria cat) throws SQLException {
        String sql = "INSERT INTO categorias (nome) VALUES (?)";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cat.getNome());
            ps.executeUpdate();
        }
    }

    public List<Categoria> listarTodos() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias ORDER BY nome";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(new Categoria(rs.getInt("id"), rs.getString("nome")));
        }
        return lista;
    }

    public void atualizar(Categoria cat) throws SQLException {
        String sql = "UPDATE categorias SET nome=? WHERE id=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cat.getNome());
            ps.setInt   (2, cat.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM categorias WHERE id=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
