package com.example.gabrielgasparotto.bancopessoa.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gabrielgasparotto.bancopessoa.R;
import com.example.gabrielgasparotto.bancopessoa.activitys.InstituicaoActivity;
import com.example.gabrielgasparotto.bancopessoa.activitys.MainActivity;
import com.example.gabrielgasparotto.bancopessoa.activitys.TipoContaActivity;
import com.example.gabrielgasparotto.bancopessoa.dao.ContaCorrenteApi;
import com.example.gabrielgasparotto.bancopessoa.model.Instituicao;
import com.example.gabrielgasparotto.bancopessoa.model.TipoConta;
import com.example.gabrielgasparotto.bancopessoa.utils.ListaInstituicoesAdapter;
import com.example.gabrielgasparotto.bancopessoa.utils.ListaTipoContaAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TipoContaFragment extends Fragment {

    private Retrofit retrofit;
    private ContaCorrenteApi contaCorrenteApi;
    private RecyclerView recyclerView;
    private ListaTipoContaAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static TipoConta tipoConta;
    private View v;
    private AlertDialog.Builder alert;
    private ArrayList<TipoConta> listaTipoConta;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tipo_conta_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerTipoConta);
        listaTipoConta = listarTipoConta();
        return v;
    }

    private ArrayList<TipoConta> listarTipoConta() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://apideexemplo.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        contaCorrenteApi = retrofit.create(ContaCorrenteApi.class);

        Call<ArrayList<TipoConta>> call = contaCorrenteApi.listarTipoConta();
        final ArrayList<TipoConta> tipoContaFinal = new ArrayList<>();

        call.enqueue(new Callback<ArrayList<TipoConta>>() {
            @Override
            public void onResponse(Call<ArrayList<TipoConta>> call, Response<ArrayList<TipoConta>> response) {

                try {
                    List<TipoConta> tipoContas = response.body();

                    for (TipoConta tipoConta : tipoContas) {
                        tipoContaFinal.add(tipoConta);
                    }
                } catch (Exception e) {
                    if(response.code() == 204){
                        Toast.makeText(getContext(), "Lista carregada, porém vazia", Toast.LENGTH_LONG).show();
                    }
                }

                if (response.code() == 200) {
                    recyclerView = v.findViewById(R.id.recyclerTipoConta);
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(getContext());
                    adapter = new ListaTipoContaAdapter(listaTipoConta);
                    if(MainActivity.textoPesquisado != null){
                        adapter.getFilter().filter(MainActivity.textoPesquisado);
                    }
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemLongClickListener(new ListaTipoContaAdapter.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(int position) {
                            //Pegar todos os valores de cada um dos recycles
                            tipoConta = tipoContaFinal.get(position);
                            criarAlerta();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TipoConta>> call, Throwable t) {
                Log.d("ERRO", t.getMessage());
            }
        });
        return tipoContaFinal;
    }

    private void criarAlerta() {
        alert = new AlertDialog.Builder(v.getContext());
        alert.setTitle("Alterar ou Excluir item");
        alert.setMessage("Escolha o que deseja fazer o item " + tipoConta.getDescricao());
        alert.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getContext(), TipoContaActivity.class));
            }
        });
        alert.setNegativeButton("Deletar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTipoConta(tipoConta.getId());
            }
        });
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                tipoConta = null;
            }
        });

        alert.create();
        alert.show();
    }

    private void deleteTipoConta(int id) {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://apideexemplo.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        contaCorrenteApi = retrofit.create(ContaCorrenteApi.class);

        Call<TipoConta> call = contaCorrenteApi.deleteTipoConta(id);

        call.enqueue(new Callback<TipoConta>() {
            @Override
            public void onResponse(Call<TipoConta> call, Response<TipoConta> response) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(TipoContaFragment.this).attach(TipoContaFragment.this).commit();
            }

            @Override
            public void onFailure(Call<TipoConta> call, Throwable t) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(TipoContaFragment.this).attach(TipoContaFragment.this).commit();
            }
        });
    }
}