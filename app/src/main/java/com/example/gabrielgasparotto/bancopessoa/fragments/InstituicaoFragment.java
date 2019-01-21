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

public class InstituicaoFragment extends Fragment {

    private Retrofit retrofit;
    private ContaCorrenteApi contaCorrenteApi;
    private RecyclerView recyclerView;
    private ListaInstituicoesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Instituicao> listaInstituicao;
    public static Instituicao instituicaoGeral;
    private View v;
    private AlertDialog.Builder alert;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.instituicao_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerInstituicao);

        listaInstituicao = listarInstituicoes();

        return v;
    }

    private ArrayList<Instituicao> listarInstituicoes(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://apideexemplo.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        contaCorrenteApi = retrofit.create(ContaCorrenteApi.class);

        Call<ArrayList<Instituicao>> call = contaCorrenteApi.listaInstituicoes();
        final ArrayList<Instituicao> instituicoesFinal = new ArrayList<>();

        call.enqueue(new Callback<ArrayList<Instituicao>>() {
            @Override
            public void onResponse(Call<ArrayList<Instituicao>> call, Response<ArrayList<Instituicao>> response) {

                try {
                    List<Instituicao> instituicoes = response.body();

                    for (Instituicao instituicao : instituicoes) {
                        instituicoesFinal.add(instituicao);
                    }
                }catch (Exception e){
                    if(response.code() == 204){
                        Toast.makeText(getContext(), "Lista carregada, porém vazia", Toast.LENGTH_LONG).show();
                    }
                }

                if (response.code() == 200) {
                    recyclerView = v.findViewById(R.id.recyclerInstituicao);
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(getContext());
                    adapter = new ListaInstituicoesAdapter(listaInstituicao);
                    if(MainActivity.textoPesquisado != null){
                        adapter.getFilter().filter(MainActivity.textoPesquisado);
                    }
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemLongClickListener(new ListaInstituicoesAdapter.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(int position) {
                            //Pegar todos os valores de cada um dos recycles
                            instituicaoGeral = instituicoesFinal.get(position);
                            criarAlerta();
                        }
                    });
                }
            }


            @Override
            public void onFailure(Call<ArrayList<Instituicao>> call, Throwable t) {
                Log.d("ERRO" ,  t.getMessage());
            }
        });
        return instituicoesFinal;
    }

    private void criarAlerta(){
        alert = new AlertDialog.Builder(v.getContext());
        alert.setTitle("Alterar ou Excluir item");
        alert.setMessage("Escolha o que deseja fazer o item "+instituicaoGeral.getDescricao());
        alert.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getContext(), InstituicaoActivity.class));
            }
        });
        alert.setNegativeButton("Deletar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteInstituicao(instituicaoGeral.getId());
            }
        });
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                instituicaoGeral = null;
            }
        });

        alert.create();
        alert.show();
    }

    private void deleteInstituicao(int id){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://apideexemplo.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        contaCorrenteApi = retrofit.create(ContaCorrenteApi.class);

        Call<Instituicao> call = contaCorrenteApi.deleteInstituicao(id);

        call.enqueue(new Callback<Instituicao>() {
            @Override
            public void onResponse(Call<Instituicao> call, Response<Instituicao> response) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(InstituicaoFragment.this).attach(InstituicaoFragment.this).commit();
            }
            @Override
            public void onFailure(Call<Instituicao> call, Throwable t) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(InstituicaoFragment.this).attach(InstituicaoFragment.this).commit();
            }
        });
    }
}
