package br.com.biblioteca.service;

import br.com.biblioteca.dao.CategoriaDAO;
import br.com.biblioteca.model.Categoria;

import java.sql.SQLException;
import java.util.List;

//Serviço da categoria - Validar a regra de negócio de não cadastrar ou atualizar uma categoria sem informar o nome
public class CategoriaService {

    private final CategoriaDAO dao = new CategoriaDAO();

    public void cadastrar(Categoria c) throws Exception {
        if (c.getNome() == null || c.getNome().isBlank())
            throw new Exception("Nome da categoria é obrigatório.");
        dao.inserir(c);
    }

    public void atualizar(Categoria c) throws Exception {
        if (c.getNome() == null || c.getNome().isBlank())
            throw new Exception("Nome da categoria é obrigatório.");
        dao.atualizar(c);
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }

    public List<Categoria> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public int contarTotal() throws SQLException {
        return dao.contarTotal();
    }
}