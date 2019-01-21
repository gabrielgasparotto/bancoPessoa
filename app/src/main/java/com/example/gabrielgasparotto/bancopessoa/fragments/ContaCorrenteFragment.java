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
import com.example.gabrielgasparotto.bancopessoa.activitys.ContaCorrenteActivity;
import com.example.gabrielgasparotto.bancopessoa.activitys.InstituicaoActivity;
import com.example.gabrielgasparotto.bancopessoa.activitys.MainActivity;
import com.example.gabrielgasparotto.bancopessoa.dao.ContaCorrenteApi;
import com.example.gabrielgasparotto.bancopessoa.model.ContaCorrente;
import com.example.gabrielgasparotto.bancopessoa.model.Instituicao;
import com.example.gabrielgasparotto.bancopessoa.utils.ListaContaCorrenteAdapter;
import com.example.gabrielgasparotto.bancopessoa.utils.ListaInstituicoesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContaCorrenteFragment extends Fragment {

    private Retrofit retrofit;
    private ContaCorrenteApi contaCorrenteApi;
    private RecyclerView recyclerView;
    private ListaContaCorrenteAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ContaCorrente> listaContaCorrente;
    public static ContaCorrente contaCorrente;
    private View v;
    private AlertDialog.Builder alert;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.conta_corrente_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerContaCorrente);
        listaContaCorrente = listarContaCorrente();
        return v;
    }

    private ArrayList<ContaCorrente> listarContaCorrente() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://apideexemplo.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        contaCorrenteApi = retrofit.create(ContaCorrenteApi.class);

        Call<ArrayList<ContaCorrente>> call = contaCorrenteApi.listarContaCorrente();
        final ArrayList<ContaCorrente> contaCorrenteFinais = new ArrayList<>();

        call.enqueue(new Callback<ArrayList<ContaCorrente>>() {
            @Override
            public void onResponse(Call<ArrayList<ContaCorrente>> call, Response<ArrayList<ContaCorrente>> response) {
                try {
                    List<ContaCorrente> contasCorrente = response.body();

                    for (ContaCorrente contaCorrente : contasCorrente) {
                        contaCorrenteFinais.add(contaCorrente);
                    }
                }catch (Exception e){
                    if(response.code() == 204){
                        Toast.makeText(getContext(), "Lista carregada, porém vazia", Toast.LENGTH_LONG).show();
                    }
                }

                if (response.code() == 200) {
                    recyclerView = v.findViewById(R.id.recyclerContaCorrente);
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(getContext());
                    adapter = new ListaContaCorrenteAdapter(listaContaCorrente);
                    if(MainActivity.textoPesquisado != null){
                        adapter.getFilter().filter(MainActivity.textoPesquisado);
                    }

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemLongClickListener(new ListaContaCorrenteAdapter.OnItemLongClickListener() {
                        @Override
                        public void onItemLongClick(int position) {
                            //Pegar todos os valores de cada um dos recycles
                            contaCorrente = contaCorrenteFinais.get(position);
                            criarAlerta();
                        }
                    });
                }
            }


            @Override
            public void onFailure(Call<ArrayList<ContaCorrente>> call, Throwable t) {
                Log.d("ERRO" ,  t.getMessage());
            }
        });

        return contaCorrenteFinais;
    }

    private void criarAlerta(){
        alert = new AlertDialog.Builder(v.getContext());
        alert.setTitle("Alterar ou Excluir item");
        alert.setMessage("Escolha o que deseja fazer o item "+contaCorrente.getConta());
        alert.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getContext(), ContaCorrenteActivity.class));
            }
        });
        alert.setNegativeButton("Deletar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteContaCorrente(contaCorrente.getId());
            }
        });

        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                contaCorrente = null;
            }
        });

        alert.create();
        alert.show();
    }

    private void deleteContaCorrente(int id){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://apideexemplo.herokuapp.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        contaCorrenteApi = retrofit.create(ContaCorrenteApi.class);

        Call<ContaCorrente> call = contaCorrenteApi.deleteContaCorrente(id);

        call.enqueue(new Callback<ContaCorrente>() {
            @Override
            public void onResponse(Call<ContaCorrente> call, Response<ContaCorrente> response) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(ContaCorrenteFragment.this).attach(ContaCorrenteFragment.this).commit();
            }
            @Override
            public void onFailure(Call<ContaCorrente> call, Throwable t) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(ContaCorrenteFragment.this).attach(ContaCorrenteFragment.this).commit();
            }
        });
    }
}
