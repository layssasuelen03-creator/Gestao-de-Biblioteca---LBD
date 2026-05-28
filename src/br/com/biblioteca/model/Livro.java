package br.com.biblioteca.model;


//Entidade Livro — mapeia a tabela livro do banco de dados.
public class Livro {

    private int    id;
    private String titulo;
    private String autor;
    private String genero;
    private int    ano;
    private int    totalCopias;
    private int    copiasDisponiveis;

    public Livro() {}

    public Livro(int id, String titulo, String autor, String genero,
                 int ano, int totalCopias, int copiasDisponiveis) {
        this.id                = id;
        this.titulo            = titulo;
        this.autor             = autor;
        this.genero            = genero;
        this.ano               = ano;
        this.totalCopias       = totalCopias;
        this.copiasDisponiveis = copiasDisponiveis;
    }

    //Getters e Setters 
    public int    getId()                 { return id; }
    public void   setId(int id)           { this.id = id; }

    public String getTitulo()             { return titulo; }
    public void   setTitulo(String t)     { this.titulo = t; }

    public String getAutor()              { return autor; }
    public void   setAutor(String a)      { this.autor = a; }

    public String getGenero()             { return genero; }
    public void   setGenero(String g)     { this.genero = g; }

    public int    getAno()                { return ano; }
    public void   setAno(int ano)         { this.ano = ano; }

    public int    getTotalCopias()        { return totalCopias; }
    public void   setTotalCopias(int t)   { this.totalCopias = t; }

    public int    getCopiasDisponiveis()  { return copiasDisponiveis; }
    public void   setCopiasDisponiveis(int c) { this.copiasDisponiveis = c; }

    @Override public String toString() { return titulo; }
}
