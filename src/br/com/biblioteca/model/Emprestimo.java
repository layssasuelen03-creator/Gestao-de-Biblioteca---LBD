package br.com.biblioteca.model;

import java.time.LocalDate;

//Entidade Emprestimo — Mapeia a tabela emprestimo
public class Emprestimo {

    private int       id;
    private int       livroId;
    private int       usuarioId;
    private String    tituloLivro;           
    private String    nomeUsuario;          
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;         
    private String    status;               //ativo | devolvido

    public Emprestimo() {}

    public int       getId()                           { return id; }
    public void      setId(int id)                     { this.id = id; }

    public int       getLivroId()                      { return livroId; }
    public void      setLivroId(int livroId)           { this.livroId = livroId; }

    public int       getUsuarioId()                    { return usuarioId; }
    public void      setUsuarioId(int usuarioId)       { this.usuarioId = usuarioId; }

    public String    getTituloLivro()                  { return tituloLivro; }
    public void      setTituloLivro(String t)          { this.tituloLivro = t; }

    public String    getNomeUsuario()                  { return nomeUsuario; }
    public void      setNomeUsuario(String n)          { this.nomeUsuario = n; }

    public LocalDate getDataEmprestimo()               { return dataEmprestimo; }
    public void      setDataEmprestimo(LocalDate d)    { this.dataEmprestimo = d; }

    public LocalDate getDataPrevistaDevolucao()        { return dataPrevistaDevolucao; }

    public void setDataPrevistaDevolucao(LocalDate d)  { this.dataPrevistaDevolucao = d; }

    public LocalDate getDataDevolucao()                { return dataDevolucao; }
    public void      setDataDevolucao(LocalDate d)     { this.dataDevolucao = d; }

    public String    getStatus()                       { return status; }
    public void      setStatus(String s)               { this.status = s; }
}