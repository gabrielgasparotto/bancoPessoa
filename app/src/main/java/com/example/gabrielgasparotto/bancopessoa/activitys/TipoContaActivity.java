package com.example.gabrielgasparotto.bancopessoa.activitys;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gabrielgasparotto.bancopessoa.R;
import com.example.gabrielgasparotto.bancopessoa.dao.ContaCorrenteApi;
import com.example.gabrielgasparotto.bancopessoa.fragments.InstituicaoFragment;
import com.example.gabrielgasparotto.bancopessoa.fragments.TipoContaFragment;
import com.example.gabrielgasparotto.bancopessoa.model.Instituicao;
import com.example.gabrielgasparotto.bancopessoa.model.TipoConta;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TipoContaActivity extends AppCompatActivity {

    private TextInputEditText editDescricao;
    private Button buttonTipoConta;
    private Retrofit retrofit;
    private ContaCorrenteApi contaCorrenteApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_conta);

        editDescricao = findViewById(R.id.editDescricaoTipoConta);
        buttonTipoConta = findViewById(R.id.buttonTipoConta);

        if(TipoContaFragment.tipoConta != null ){
            editDescricao.setText(TipoContaFragment.tipoConta.getDescricao());
            buttonTipoConta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TipoConta tipo = new TipoConta(editDescricao.getText().toString());
                    tipo.setId(TipoContaFragment.tipoConta.getId());
                    alterarTipoConta(tipo.getId(), tipo);
                    Intent i = new Intent(TipoContaActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }else{
            buttonTipoConta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TipoConta tipo = new TipoConta(editDescricao.getText().toString());
                    inserirTipoConta(tipo);
                    Intent i = new Intent(TipoContaActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
    }

    private void inserirTipoConta(TipoConta tipoConta){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://apideexemplo.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        contaCorrenteApi = retrofit.create(ContaCorrenteApi.class);

        Call<TipoConta> call = contaCorrenteApi.criarTipoConta(tipoConta);

        call.enqueue(new Callback<TipoConta>() {
            @Override
            public void onResponse(Call<TipoConta> call, Response<TipoConta> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Inserido com sucesso!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<TipoConta> call, Throwable t) {
            }
        });
    }

    private void alterarTipoConta(int id, TipoConta tipoConta){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://apideexemplo.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        contaCorrenteApi = retrofit.create(ContaCorrenteApi.class);

        Call<TipoConta> call = contaCorrenteApi.updateTipoConta (id, tipoConta);

        call.enqueue(new Callback<TipoConta>() {
            @Override
            public void onResponse(Call<TipoConta> call, Response<TipoConta> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Alterado com sucesso!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<TipoConta> call, Throwable t) {
            }
        });

    }
}
