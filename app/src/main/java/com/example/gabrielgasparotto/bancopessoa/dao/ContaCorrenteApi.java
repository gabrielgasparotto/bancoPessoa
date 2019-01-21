package com.example.gabrielgasparotto.bancopessoa.dao;

import com.example.gabrielgasparotto.bancopessoa.model.ContaCorrente;
import com.example.gabrielgasparotto.bancopessoa.model.Instituicao;
import com.example.gabrielgasparotto.bancopessoa.model.TipoConta;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ContaCorrenteApi {

    //Todas as funcionalidades que buscam na API Instituição
    @GET("instituicao")
    Call<ArrayList<Instituicao>> listaInstituicoes();

    @POST("instituicao")
    Call<Instituicao>  criarInstituicao(@Body Instituicao instituicao);

    @PUT( "instituicao/{id}" )
    Call<Instituicao> updateInstituicao(@Path("id") int id, @Body Instituicao instituicao);

    @DELETE("instituicao/{id}")
    Call<Instituicao> deleteInstituicao(@Path("id") int id);

    //Todas as funcionalidades que buscam na API Tipo Conta
    @GET("tipoConta")
    Call<ArrayList<TipoConta>> listarTipoConta();

    @DELETE("tipoConta/{id}")
    Call<TipoConta> deleteTipoConta(@Path("id") int id);

    @POST("tipoConta")
    Call<TipoConta>  criarTipoConta(@Body TipoConta tipoConta);

    @PUT( "tipoConta/{id}" )
    Call<TipoConta> updateTipoConta(@Path("id") int id, @Body TipoConta tipoConta);

    //Todas as funcionalidades que buscam na API Conta Corrente
    @POST("contacorrente")
    Call<ContaCorrente>  criarContaCorrente(@Body ContaCorrente contaCorrente);

    @PUT( "contacorrente/{id}" )
    Call<ContaCorrente> updateContaCorrente(@Path("id") int id, @Body ContaCorrente contaCorrente);

    @GET("contacorrente")
    Call<ArrayList<ContaCorrente>> listarContaCorrente();

    @DELETE("contacorrente/{id}")
    Call<ContaCorrente> deleteContaCorrente(@Path("id") int id);
}
