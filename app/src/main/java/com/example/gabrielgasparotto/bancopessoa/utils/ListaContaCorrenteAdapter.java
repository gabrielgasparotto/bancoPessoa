package com.example.gabrielgasparotto.bancopessoa.utils;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabrielgasparotto.bancopessoa.R;
import com.example.gabrielgasparotto.bancopessoa.model.ContaCorrente;
import com.example.gabrielgasparotto.bancopessoa.model.TipoConta;

import java.util.ArrayList;
import java.util.List;

public class ListaContaCorrenteAdapter extends RecyclerView.Adapter<ListaContaCorrenteAdapter.ExampleViewHolder> implements Filterable {

    private ArrayList<ContaCorrente> listaContaCorrentes;
    private OnItemLongClickListener onItemLongClickListener;
    private ArrayList<ContaCorrente> listaContaCorrenteInteira;

    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        onItemLongClickListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        private TextView textConta, textCodigo, textAgencia, textDescricao, textObs, textSaldo, textLimite, textInicial;
        public ExampleViewHolder(@NonNull View itemView, final OnItemLongClickListener listener) {
            super(itemView);
            textConta = itemView.findViewById(R.id.textConta);
            textCodigo = itemView.findViewById(R.id.textCodigoContaCorrente);
            textAgencia = itemView.findViewById(R.id.textAgenciaContaCorrente);
            textDescricao = itemView.findViewById(R.id.textDescricaoContaCorrente);
            textObs = itemView.findViewById(R.id.textObs);
            textSaldo = itemView.findViewById(R.id.limiteCredito);
            textLimite = itemView.findViewById(R.id.saldoAtual);
            textInicial = itemView.findViewById(R.id.salarioInicial);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener!= null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemLongClick(position);
                        }
                    }
                    return false;
                }
            });
        }
    }


    public ListaContaCorrenteAdapter(ArrayList<ContaCorrente> listaContaCorrente){
        listaContaCorrentes  = listaContaCorrente;
        listaContaCorrenteInteira = new ArrayList<>(listaContaCorrente);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exemplo_contacorrente, viewGroup, false);
        ExampleViewHolder exampleViewHolder =  new ExampleViewHolder(view, onItemLongClickListener);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {

        ContaCorrente contaCorrente = listaContaCorrentes.get(position);
        holder.textConta.setText(contaCorrente.getConta());
        holder.textCodigo.setText(contaCorrente.getCodigo());
        holder.textAgencia.setText(contaCorrente.getAgencia());
        holder.textDescricao.setText(contaCorrente.getDescricao());
        holder.textObs.setText(contaCorrente.getObs());
        holder.textSaldo.setText("R$"+contaCorrente.getVlSaldo());
        holder.textLimite.setText("R$"+contaCorrente.getVlLimiteCredito());
        holder.textInicial.setText("R$"+contaCorrente.getVlSaldoInicial());
    }

    @Override
    public int getItemCount() {
        return listaContaCorrentes.size();
    }

    @Override
    public Filter getFilter() {
        return contaCorrenteFiltro;
    }

    private Filter contaCorrenteFiltro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ContaCorrente> listaFiltrada = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                listaFiltrada.addAll(listaContaCorrenteInteira);
            } else {
                String filtro  = constraint.toString().toLowerCase().trim();

                for (ContaCorrente conta :
                        listaContaCorrenteInteira) {
                    if(conta.getConta().toLowerCase().contains(filtro) ||
                            conta.getAgencia().toLowerCase().contains(filtro) ||
                            String.valueOf(conta.getId()).toLowerCase().contains(filtro) ||
                            conta.getDescricao().toLowerCase().contains(filtro)
                            ){
                        listaFiltrada.add(conta);
                    }

                }
            }

            FilterResults results = new FilterResults();
            results.values = listaFiltrada;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listaContaCorrentes.clear();
            listaContaCorrentes.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
