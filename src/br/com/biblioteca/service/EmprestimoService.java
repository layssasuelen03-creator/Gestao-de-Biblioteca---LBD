package br.com.biblioteca.service;

import br.com.biblioteca.dao.EmprestimoDAO;
import br.com.biblioteca.model.Emprestimo;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

//Serviço do emprestimo — As regras de negócio: prazo mínimo de 1 dia, máximo de 30 dias 
public class EmprestimoService {

    private final EmprestimoDAO dao = new EmprestimoDAO();

    public void realizarEmprestimo(Emprestimo e) throws Exception {
        if (e.getLivroId()   <= 0) throw new Exception("Livro inválido.");
        if (e.getUsuarioId() <= 0) throw new Exception("Usuário inválido.");
        if (e.getDataPrevistaDevolucao() == null)
            throw new Exception("Data prevista de devolução obrigatória.");

        LocalDate hoje = LocalDate.now();
        e.setDataEmprestimo(hoje);

        long dias = ChronoUnit.DAYS.between(hoje, e.getDataPrevistaDevolucao());
        if (dias < 1)  throw new Exception("Data de devolução deve ser futura.");
        if (dias > 30) throw new Exception("Prazo máximo de empréstimo: 30 dias.");

        e.setStatus("ativo");
        dao.inserir(e);
    }

    public void registrarDevolucao(int id) throws SQLException {
        try {
            dao.registrarDevolucao(id);
        } catch (SQLException e) {
            throw new SQLException("Erro ao registrar a devolução no banco de dados. Tente novamente.");
        }
    }


    public List<Emprestimo> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public int contarAtivos()    throws SQLException { return dao.contarAtivos();    }
    public int contarAtrasados() throws SQLException { return dao.contarAtrasados(); }
}
