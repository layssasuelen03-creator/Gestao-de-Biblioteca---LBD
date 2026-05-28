package br.com.biblioteca.dao;

import br.com.biblioteca.connection.ConnectionFactory;
import br.com.biblioteca.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UsuarioDAO {

    //CREATE 
    public void inserir(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, telefone, senha, status) VALUES (?,?,?,?,?)";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getTelefone());
            ps.setString(4, u.getSenha());
            ps.setString(5, u.getStatus() != null ? u.getStatus() : "ativo");
            ps.executeUpdate();
        }
    }

    //READ 
    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
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
    public void atualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuarios SET nome=?, email=?, telefone=?, status=? WHERE id=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getTelefone());
            ps.setString(4, u.getStatus());
            ps.setInt   (5, u.getId());
            ps.executeUpdate();
        }
    }

    //DELETE 
    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    //Contagem 
    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    //Mapper 
    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt   ("id"),
            rs.getString("nome"),
            rs.getString("email"),
            rs.getString("telefone"),
            rs.getString("status")
        );
    }
}
