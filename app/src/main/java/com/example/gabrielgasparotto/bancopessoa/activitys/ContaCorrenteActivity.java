package com.example.gabrielgasparotto.bancopessoa.activitys;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.gabrielgasparotto.bancopessoa.R;
import com.example.gabrielgasparotto.bancopessoa.dao.ContaCorrenteApi;
import com.example.gabrielgasparotto.bancopessoa.fragments.ContaCorrenteFragment;
import com.example.gabrielgasparotto.bancopessoa.fragments.InstituicaoFragment;
import com.example.gabrielgasparotto.bancopessoa.model.ContaCorrente;
import com.example.gabrielgasparotto.bancopessoa.model.Instituicao;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContaCorrenteActivity extends AppCompatActivity {

    private TextInputEditText editObs, editAgencia, editCodigo, editConta, editDescricao, editInstituicao, editTipoConta, editLimite, editSaldo, editInicial;
    private CheckBox checkResumo;
    private Button buttonContaCorrente;
    private Retrofit retrofit;
    private ContaCorrenteApi contaCorrenteApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta_corrente);

        editAgencia = findViewById(R.id.editAgencia);
        editCodigo = findViewById(R.id.editCodigo);
        editConta = findViewById(R.id.editConta);
        editDescricao = findViewById(R.id.editDescricao);
        editInstituicao = findViewById(R.id.editInstituicao);
        editTipoConta = findViewById(R.id.editTipoConta);
        editLimite = findViewById(R.id.editLimite);
        editSaldo = findViewById(R.id.editSaldo);
        editInicial = findViewById(R.id.editInicial);
        checkResumo = findViewById(R.id.checkResumo);
        editObs = findViewById(R.id.editObs);
        buttonContaCorrente = findViewById(R.id.buttonContaCorrente);

        if(ContaCorrenteFragment.contaCorrente != null) {
            editAgencia.setText(ContaCorrenteFragment.contaCorrente.getAgencia());
            editCodigo.setText(ContaCorrenteFragment.contaCorrente.getCodigo());
            editConta.setText(ContaCorrenteFragment.contaCorrente.getConta());
            editDescricao.setText(ContaCorrenteFragment.contaCorrente.getDescricao());
            checkResumo.setActivated(ContaCorrenteFragment.contaCorrente.isFlExcResumo());
            editInstituicao.setText(String.valueOf(ContaCorrenteFragment.contaCorrente.getInstituicaoId()));
            editObs.setText(ContaCorrenteFragment.contaCorrente.getObs());
            editTipoConta.setText(String.valueOf(ContaCorrenteFragment.contaCorrente.getTipoContaId()));
            editLimite.setText(String.valueOf(ContaCorrenteFragment.contaCorrente.getVlLimiteCredito()));
            editSaldo.setText(String.valueOf(ContaCorrenteFragment.contaCorrente.getVlSaldo()));
            editInicial.setText(String.valueOf(ContaCorrenteFragment.contaCorrente.getVlSaldoInicial()));
            //update
            buttonContaCorrente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContaCorrente cc = new ContaCorrente(
                            editAgencia.getText().toString(),
                            editCodigo.getText().toString(),
                            editConta.getText().toString(),
                            editDescricao.getText().toString(),
                            checkResumo.isChecked(),
                            Integer.parseInt(editInstituicao.getText().toString()),
                            editObs.getText().toString(),
                            Integer.parseInt(editTipoConta.getText().toString()),
                            Integer.parseInt(editLimite.getText().toString()),
                            Integer.parseInt(editSaldo.getText().toString()),
                            Integer.parseInt(editInicial.getText().toString())
                    );
                    cc.setId(ContaCorrenteFragment.contaCorrente.getId());
                    alterarContaCorrente(cc.getId(), cc);
                    Intent i = new Intent(ContaCorrenteActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }else{
            buttonContaCorrente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContaCorrente cc = new ContaCorrente(
                            editAgencia.getText().toString(),
                            editCodigo.getText().toString(),
                            editConta.getText().toString(),
                            editDescricao.getText().toString(),
                            checkResumo.isChecked(),
                            Integer.parseInt(editInstituicao.getText().toString()),
                            editObs.getText().toString(),
                            Integer.parseInt(editTipoConta.getText().toString()),
                            Integer.parseInt(editLimite.getText().toString()),
                            Integer.parseInt(editSaldo.getText().toString()),
                            Integer.parseInt(editInicial.getText().toString())
                    );
                    inserirContaCorrente(cc);
                    Intent i = new Intent(ContaCorrenteActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
    }

    private void inserirContaCorrente(ContaCorrente contaCorrente){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://apideexemplo.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        contaCorrenteApi = retrofit.create(ContaCorrenteApi.class);

        Call<ContaCorrente> call = contaCorrenteApi.criarContaCorrente(contaCorrente);

        call.enqueue(new Callback<ContaCorrente>() {
            @Override
            public void onResponse(Call<ContaCorrente> call, Response<ContaCorrente> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Inserido com sucesso!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<ContaCorrente> call, Throwable t) {
            }
        });
    }

    private void alterarContaCorrente(int id, ContaCorrente contaCorrente){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://apideexemplo.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        contaCorrenteApi = retrofit.create(ContaCorrenteApi.class);

        Call<ContaCorrente> call = contaCorrenteApi.updateContaCorrente(id, contaCorrente);

        call.enqueue(new Callback<ContaCorrente>() {
            @Override
            public void onResponse(Call<ContaCorrente> call, Response<ContaCorrente> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Alterado com sucesso!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<ContaCorrente> call, Throwable t) {
            }
        });

    }
}
