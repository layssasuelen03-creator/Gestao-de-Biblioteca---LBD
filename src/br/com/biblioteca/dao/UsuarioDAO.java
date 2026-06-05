package br.com.biblioteca.dao;

import br.com.biblioteca.connection.ConnectionFactory;
import br.com.biblioteca.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Tabela usuario 
public class UsuarioDAO {

    public void inserir(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, telefone, senha, status) VALUES (?,?,?,?,?)";
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

    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nome, email, telefone, senha, status FROM usuario ORDER BY nome";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_usuario, nome, email, telefone, senha, status FROM usuario WHERE id_usuario = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    //Responsável por buscar um usuário com base no email e senha fornecidos e onde status = ativo
    public Usuario autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT id_usuario, nome, email, telefone, senha, status " +
                     "FROM usuario WHERE email = ? AND senha = ? AND status = 'ativo'";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, senha);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public void atualizar(Usuario u) throws SQLException {
        String sql = "UPDATE usuario SET nome = ?, email = ?, telefone = ?, status = ? WHERE id_usuario = ?";
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

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int contarAtivos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE status = 'ativo'";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId      (rs.getInt   ("id_usuario"));
        u.setNome    (rs.getString("nome"));
        u.setEmail   (rs.getString("email"));
        u.setTelefone(rs.getString("telefone"));
        u.setSenha   (rs.getString("senha"));
        u.setStatus  (rs.getString("status"));
        return u;
    }
}
