package br.com.biblioteca.service;

import br.com.biblioteca.dao.LivroDAO;
import br.com.biblioteca.model.Livro;

import java.sql.SQLException;
import java.util.List;


//Serviço do Livro — valida regras de negócio antes de chamar o DAO
public class LivroService {

    private final LivroDAO dao = new LivroDAO();

    /** Regra: título e autor obrigatórios; cópias >= 1. */
    public void cadastrar(Livro l) throws Exception {
        validar(l);
        l.setCopiasDisponiveis(l.getTotalCopias());  // ao cadastrar, tudo disponível
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

    private void validar(Livro l) throws Exception {
        if (l.getTitulo() == null || l.getTitulo().isBlank())
            throw new Exception("Título é obrigatório.");
        if (l.getAutor() == null || l.getAutor().isBlank())
            throw new Exception("Autor é obrigatório.");
        if (l.getTotalCopias() < 1)
            throw new Exception("Total de cópias deve ser pelo menos 1.");
    }
}
