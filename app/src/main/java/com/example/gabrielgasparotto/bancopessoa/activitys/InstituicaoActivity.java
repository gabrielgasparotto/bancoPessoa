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
import com.example.gabrielgasparotto.bancopessoa.model.Instituicao;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InstituicaoActivity extends AppCompatActivity {

    private TextInputEditText editImage, editDescricao, editCodigo;
    private Button buttonInstituicao;
    private Retrofit retrofit;
    private ContaCorrenteApi contaCorrenteApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instituicao);

        editImage = findViewById(R.id.editImagem);
        editCodigo = findViewById(R.id.editCodigo);
        editDescricao = findViewById(R.id.editDescricao);
        buttonInstituicao = findViewById(R.id.buttonInstituicao);

        if(InstituicaoFragment.instituicaoGeral != null ){
            editDescricao.setText(InstituicaoFragment.instituicaoGeral.getDescricao());
            editCodigo.setText(String.valueOf(InstituicaoFragment.instituicaoGeral.getCodigo()));
            editImage.setText(InstituicaoFragment.instituicaoGeral.getImage());
            buttonInstituicao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Instituicao ins = new Instituicao(Integer.parseInt(editCodigo.getText().toString()), editDescricao.getText().toString(), editImage.getText().toString());
                    ins.setId(InstituicaoFragment.instituicaoGeral.getId());
                    alterarInstituicao(ins.getId(), ins);
                    Intent i = new Intent(InstituicaoActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }else{
            buttonInstituicao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Instituicao ins = new Instituicao(Integer.parseInt(editCodigo.getText().toString()), editDescricao.getText().toString(), editImage.getText().toString());
                    inserirInstituicao(ins);
                    Intent i = new Intent(InstituicaoActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
    }

    private void inserirInstituicao(Instituicao instituicao){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://apideexemplo.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        contaCorrenteApi = retrofit.create(ContaCorrenteApi.class);

        Call<Instituicao> call = contaCorrenteApi.criarInstituicao(instituicao);

        call.enqueue(new Callback<Instituicao>() {
            @Override
            public void onResponse(Call<Instituicao> call, Response<Instituicao> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Inserido com sucesso!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<Instituicao> call, Throwable t) {
            }
        });
    }

    private void alterarInstituicao(int id, Instituicao instituicao){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://apideexemplo.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        contaCorrenteApi = retrofit.create(ContaCorrenteApi.class);

        Call<Instituicao> call = contaCorrenteApi.updateInstituicao(id, instituicao);

        call.enqueue(new Callback<Instituicao>() {
            @Override
            public void onResponse(Call<Instituicao> call, Response<Instituicao> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Code: "+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<Instituicao> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
