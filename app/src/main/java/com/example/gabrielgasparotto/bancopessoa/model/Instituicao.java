package com.example.gabrielgasparotto.bancopessoa.model;

public class Instituicao {

    private int codigo;
    private String descricao;
    private int id;
    private String image;

    public Instituicao(int codigo, String descricao, String image) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.id = id;
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }
}
