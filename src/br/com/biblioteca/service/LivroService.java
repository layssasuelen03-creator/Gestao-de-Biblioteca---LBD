package br.com.biblioteca.service;

import br.com.biblioteca.dao.LivroDAO;
import br.com.biblioteca.model.Livro;

import java.sql.SQLException;
import java.util.List;

//Serviço do Livro — Vai validar as regras de negócio antes de chamar o DAO
public class LivroService {

    private final LivroDAO dao = new LivroDAO();

    public void cadastrar(Livro l) throws Exception {
        validar(l);
        l.setStatus("disponível");
        dao.inserir(l);
    }

    public void atualizar(Livro l) throws Exception {
        validar(l);
        dao.atualizar(l);
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }

    public List<Livro> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public List<Livro> buscar(String termo) throws SQLException {
        return dao.buscar(termo);
    }

    public int contarTotal() throws SQLException {
        return dao.contarTotal();
    }

    public int contarDisponiveis() throws SQLException {
        return dao.contarDisponiveis();
    }

    private void validar(Livro l) throws Exception {
        if (l.getTitulo() == null || l.getTitulo().isBlank())
            throw new Exception("Título é obrigatório.");
        if (l.getIdAutor() <= 0)
            throw new Exception("Autor é obrigatório.");
    }
}