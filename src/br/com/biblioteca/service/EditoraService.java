package br.com.biblioteca.service;

import br.com.biblioteca.dao.EditoraDAO;
import br.com.biblioteca.model.Editora;

import java.sql.SQLException;
import java.util.List;

//Serviço da editora - Validar a regra de negócio de não cadastrar ou atualizar uma editora sem informar o nome
public class EditoraService {

    private final EditoraDAO dao = new EditoraDAO();

    public void cadastrar(Editora e) throws Exception {
        if (e.getNome() == null || e.getNome().isBlank())
            throw new Exception("Nome da editora é obrigatório.");
        dao.inserir(e);
    }

    public void atualizar(Editora e) throws Exception {
        if (e.getNome() == null || e.getNome().isBlank())
            throw new Exception("Nome da editora é obrigatório.");
        dao.atualizar(e);
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }

    public List<Editora> listarTodos() throws SQLException {
        return dao.listarTodos();
    }
}
