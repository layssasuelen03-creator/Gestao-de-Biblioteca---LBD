package br.com.biblioteca.model;

import java.time.LocalDate;

//Entidade Livro — Mapeia a tabela livro
public class Livro {

    private int       id;
    private String    titulo;
    private String    isbn;
    private LocalDate anoPublicacao;
    private String    status;        //disponível | emprestado
    private int       idCategoria;
    private int       idAutor;
    private int       idEditora;


    private String nomeAutor;
    private String nomeCategoria;
    private String nomeEditora;

    public Livro() {}

    public int       getId()                   { return id; }
    public void      setId(int id)             { this.id = id; }

    public String    getTitulo()               { return titulo; }
    public void      setTitulo(String t)       { this.titulo = t; }

    public String    getIsbn()                 { return isbn; }
    public void      setIsbn(String isbn)      { this.isbn = isbn; }

    public LocalDate getAnoPublicacao()        { return anoPublicacao; }
    public void      setAnoPublicacao(LocalDate a) { this.anoPublicacao = a; }

    public String    getStatus()               { return status; }
    public void      setStatus(String s)       { this.status = s; }

    public int       getIdCategoria()          { return idCategoria; }
    public void      setIdCategoria(int c)     { this.idCategoria = c; }

    public int       getIdAutor()              { return idAutor; }
    public void      setIdAutor(int a)         { this.idAutor = a; }

    public int       getIdEditora()            { return idEditora; }
    public void      setIdEditora(int e)       { this.idEditora = e; }

    public String    getNomeAutor()            { return nomeAutor; }
    public void      setNomeAutor(String n)    { this.nomeAutor = n; }

    public String    getNomeCategoria()        { return nomeCategoria; }
    public void      setNomeCategoria(String n){ this.nomeCategoria = n; }

    public String    getNomeEditora()          { return nomeEditora; }
    public void      setNomeEditora(String n)  { this.nomeEditora = n; }

    @Override public String toString()         { return titulo; }
}