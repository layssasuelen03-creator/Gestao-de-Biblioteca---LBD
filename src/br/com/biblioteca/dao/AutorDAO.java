package br.com.biblioteca.dao;

import br.com.biblioteca.connection.ConnectionFactory;
import br.com.biblioteca.model.Autor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AutorDAO {

    public void inserir(Autor a) throws SQLException {
        String sql = "INSERT INTO autores (nome, especialidade) VALUES (?,?)";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getEspecialidade());
            ps.executeUpdate();
        }
    }

    public List<Autor> listarTodos() throws SQLException {
        List<Autor> lista = new ArrayList<>();
        String sql = "SELECT * FROM autores ORDER BY nome";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                lista.add(new Autor(rs.getInt("id"), rs.getString("nome"), rs.getString("especialidade")));
        }
        return lista;
    }

    public void atualizar(Autor a) throws SQLException {
        String sql = "UPDATE autores SET nome=?, especialidade=? WHERE id=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getEspecialidade());
            ps.setInt   (3, a.getId());
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM autores WHERE id=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
