package com.example.gabrielgasparotto.bancopessoa.model;

public class TipoConta {

    private String descricao;
    private int id;

    public TipoConta(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descicao) {
        this.descricao = descicao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
