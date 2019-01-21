package com.example.gabrielgasparotto.bancopessoa.utils;

import android.support.annotation.NonNull;
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

public class ListaTipoContaAdapter extends RecyclerView.Adapter<ListaTipoContaAdapter.ExampleViewHolder> implements Filterable {

    private ArrayList<TipoConta> listaTiposContas;
    private ArrayList<TipoConta> listaTiposContaInteira;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        onItemLongClickListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView textId;
        public TextView textDescicao;

        public ExampleViewHolder(@NonNull View itemView, final OnItemLongClickListener listener) {
            super(itemView);
            textId = itemView.findViewById(R.id.textId);
            textDescicao = itemView.findViewById(R.id.textDescricao);

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


    public ListaTipoContaAdapter(ArrayList<TipoConta> listaTiposConta){
        listaTiposContas  = listaTiposConta;
        listaTiposContaInteira = new ArrayList<>(listaTiposConta);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exemplo_tipoconta, viewGroup, false);
        ExampleViewHolder exampleViewHolder =  new ExampleViewHolder(view, onItemLongClickListener);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {

        TipoConta tipoConta = listaTiposContas.get(position);
       // holder.imagemRecycle.setImageResource(R.drawable.ic_radio_button_checked_black_24dp);
        holder.textId.setText(tipoConta.getId()+" -");
        holder.textDescicao.setText(tipoConta.getDescricao());
    }

    @Override
    public int getItemCount() {
        return listaTiposContas.size();
    }

    @Override
    public Filter getFilter() {
        return tipoContaFiltro;
    }

    private Filter tipoContaFiltro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<TipoConta> listaFiltrada = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                listaFiltrada.addAll(listaTiposContaInteira);
            } else {
                String filtro  = constraint.toString().toLowerCase().trim();

                for (TipoConta tipoConta:
                        listaTiposContaInteira) {
                    if(tipoConta.getDescricao().toLowerCase().contains(filtro)){
                        listaFiltrada.add(tipoConta);
                    }

                }
            }

            FilterResults results = new FilterResults();
            results.values = listaFiltrada;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listaTiposContas.clear();
            listaTiposContas.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

}
