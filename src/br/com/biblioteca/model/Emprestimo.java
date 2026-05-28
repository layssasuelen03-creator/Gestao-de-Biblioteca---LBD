package br.com.biblioteca.model;

import java.time.LocalDate;

//Entidade Emprestimo — mapeia a tabela emprestimo e relaciona com Livro e Usuario
public class Emprestimo {

    private int       id;
    private int       livroId;
    private int       usuarioId;
    private String    tituloLivro;    
    private String    nomeUsuario;    
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private String    status;          //ativo | atrasado | devolvido

    public Emprestimo() {}

    //Getters / Setters 
    public int       getId()                               { return id; }
    public void      setId(int id)                         { this.id = id; }

    public int       getLivroId()                          { return livroId; }
    public void      setLivroId(int livroId)               { this.livroId = livroId; }

    public int       getUsuarioId()                        { return usuarioId; }
    public void      setUsuarioId(int usuarioId)           { this.usuarioId = usuarioId; }

    public String    getTituloLivro()                      { return tituloLivro; }
    public void      setTituloLivro(String t)              { this.tituloLivro = t; }

    public String    getNomeUsuario()                      { return nomeUsuario; }
    public void      setNomeUsuario(String n)              { this.nomeUsuario = n; }

    public LocalDate getDataEmprestimo()                   { return dataEmprestimo; }
    public void      setDataEmprestimo(LocalDate d)        { this.dataEmprestimo = d; }

    public LocalDate getDataDevolucaoPrevista()            { return dataDevolucaoPrevista; }
    public void      setDataDevolucaoPrevista(LocalDate d) { this.dataDevolucaoPrevista = d; }

    public LocalDate getDataDevolucaoReal()                { return dataDevolucaoReal; }
    public void      setDataDevolucaoReal(LocalDate d)     { this.dataDevolucaoReal = d; }

    public String    getStatus()                           { return status; }
    public void      setStatus(String s)                   { this.status = s; }
}
