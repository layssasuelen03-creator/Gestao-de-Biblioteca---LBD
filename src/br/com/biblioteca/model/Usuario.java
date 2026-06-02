package br.com.biblioteca.model;

//Entidade Usuario — mapeia a tabela usuario
public class Usuario {

    private int    id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private String status;   //ativo | inativo

    public Usuario() {}

    public Usuario(int id, String nome, String email, String telefone, String status) {
        this.id       = id;
        this.nome     = nome; 
        this.email    = email;
        this.telefone = telefone;
        this.status   = status;
    }

    public int    getId()               { return id; }
    public void   setId(int id)         { this.id = id; }

    public String getNome()             { return nome; }
    public void   setNome(String n)     { this.nome = n; }

    public String getEmail()            { return email; }
    public void   setEmail(String e)    { this.email = e; }

    public String getTelefone()         { return telefone; }
    public void   setTelefone(String t) { this.telefone = t; }

    public String getSenha()            { return senha; }
    public void   setSenha(String s)    { this.senha = s; }

    public String getStatus()           { return status; }
    public void   setStatus(String s)   { this.status = s; }

    @Override public String toString() { return nome; }
}
