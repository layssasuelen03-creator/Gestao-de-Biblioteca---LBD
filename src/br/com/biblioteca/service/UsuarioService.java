package br.com.biblioteca.service;

import br.com.biblioteca.dao.UsuarioDAO;
import br.com.biblioteca.model.Usuario;

import java.sql.SQLException;
import java.util.List;

//Serviço do Usuario — Validações das regras de negócio.
public class UsuarioService {

    private final UsuarioDAO dao = new UsuarioDAO();

    public void cadastrar(Usuario u) throws Exception {
        validar(u);
        if (u.getSenha() == null || u.getSenha().isBlank())
            throw new Exception("Senha é obrigatória.");
        u.setStatus("ativo");
        dao.inserir(u);
    }

    public void atualizar(Usuario u) throws Exception {
        validar(u);
        dao.atualizar(u);
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }

    public List<Usuario> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public int contarTotal() throws SQLException {
        return dao.contarTotal();
    }

    public int contarAtivos() throws SQLException {
        return dao.contarAtivos();
    }

    //Vai retorna o Usuario autenticado, ou null se credenciais for inválidas
    public Usuario autenticar(String email, String senha) throws SQLException {
        return dao.autenticar(email, senha);
    }

    private void validar(Usuario u) throws Exception {
        if (u.getNome() == null || u.getNome().isBlank())
            throw new Exception("Nome é obrigatório.");
        if (u.getEmail() == null || u.getEmail().isBlank())
            throw new Exception("E-mail é obrigatório.");
        if (!u.getEmail().contains("@"))
            throw new Exception("E-mail inválido.");
    }
}
