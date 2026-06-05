package br.com.biblioteca.service;

import br.com.biblioteca.dao.AutorDAO;
import br.com.biblioteca.model.Autor;

import java.sql.SQLException;
import java.util.List;

//Serviço do autor - Validar a regra de negócio de não cadastrar ou atualizar um autor sem informar o nome
public class AutorService {

    private final AutorDAO dao = new AutorDAO();

    public void cadastrar(Autor a) throws Exception {
        validar(a);
        dao.inserir(a);
    }

    public void atualizar(Autor a) throws Exception {
        validar(a);
        dao.atualizar(a);
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }

    public List<Autor> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public int contarTotal() throws SQLException {
        return dao.contarTotal();
    }

    private void validar(Autor a) throws Exception {
        if (a.getNome() == null || a.getNome().isBlank())
            throw new Exception("Nome do autor é obrigatório.");
    }
}