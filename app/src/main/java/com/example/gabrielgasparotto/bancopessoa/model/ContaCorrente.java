package com.example.gabrielgasparotto.bancopessoa.model;

public class ContaCorrente {

    private String agencia;
    private String codigo;
    private String conta;
    private String descricao;
    private boolean flExcResumo;
    private int id;
    private int instituicaoId;
    private String obs;
    private int tipoContaId;
    private int vlLimiteCredito;
    private int vlSaldo;
    private int vlSaldoInicial;

    public ContaCorrente(String agencia, String codigo, String conta, String descricao, boolean flExcResumo, int instituicaoId, String obs, int tipoContaId, int vlLimiteCredito, int vlSaldo, int vlSaldoInicial) {
        this.agencia = agencia;
        this.codigo = codigo;
        this.conta = conta;
        this.descricao = descricao;
        this.flExcResumo = flExcResumo;
        this.instituicaoId = instituicaoId;
        this.obs = obs;
        this.tipoContaId = tipoContaId;
        this.vlLimiteCredito = vlLimiteCredito;
        this.vlSaldo = vlSaldo;
        this.vlSaldoInicial = vlSaldoInicial;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAgencia() {
        return agencia;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getConta() {
        return conta;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isFlExcResumo() {
        return flExcResumo;
    }

    public int getId() {
        return id;
    }

    public int getInstituicaoId() {
        return instituicaoId;
    }

    public String getObs() {
        return obs;
    }

    public int getTipoContaId() {
        return tipoContaId;
    }

    public int getVlLimiteCredito() {
        return vlLimiteCredito;
    }

    public int getVlSaldo() {
        return vlSaldo;
    }

    public int getVlSaldoInicial() {
        return vlSaldoInicial;
    }
}
