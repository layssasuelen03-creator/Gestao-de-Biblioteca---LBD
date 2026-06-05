package br.com.biblioteca.dao;

import br.com.biblioteca.connection.ConnectionFactory;
import br.com.biblioteca.model.Editora;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Tabela editora
public class EditoraDAO {

    public void inserir(Editora e) throws SQLException {
        String sql = "INSERT INTO editora (nome, cidade, pais) VALUES (?, ?, ?)";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.getNome());
            ps.setString(2, e.getCidade());
            ps.setString(3, e.getPais());
            ps.executeUpdate();
        }
    }

    public List<Editora> listarTodos() throws SQLException {
        List<Editora> lista = new ArrayList<>();
        String sql = "SELECT id_editora, nome, cidade, pais FROM editora ORDER BY nome";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(new Editora(rs.getInt("id_editora"), rs.getString("nome"),
                        rs.getString("cidade"), rs.getString("pais")));
        }
        return lista;
    }

    public void atualizar(Editora e) throws SQLException {
        String sql = "UPDATE editora SET nome = ?, cidade = ?, pais = ? WHERE id_editora = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.getNome());
            ps.setString(2, e.getCidade());
            ps.setString(3, e.getPais());
            ps.setInt   (4, e.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM editora WHERE id_editora = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}