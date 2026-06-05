package br.com.biblioteca.model;

//Entidade Editora — Mapeia a tabela editora 
public class Editora {

    private int    id;
    private String nome;
    private String cidade;
    private String pais;

    public Editora() {}

    public Editora(int id, String nome, String cidade, String pais) {
        this.id     = id;
        this.nome   = nome;
        this.cidade = cidade;
        this.pais   = pais;
    }

    public int    getId()              { return id; }
    public void   setId(int id)        { this.id = id; }

    public String getNome()            { return nome; }
    public void   setNome(String n)    { this.nome = n; }

    public String getCidade()          { return cidade; }
    public void   setCidade(String c)  { this.cidade = c; }

    public String getPais()            { return pais; }
    public void   setPais(String p)    { this.pais = p; }

    @Override public String toString() { return nome; }
}