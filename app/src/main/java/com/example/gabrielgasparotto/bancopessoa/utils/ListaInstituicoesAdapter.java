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
import com.example.gabrielgasparotto.bancopessoa.model.Instituicao;
import com.example.gabrielgasparotto.bancopessoa.model.TipoConta;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListaInstituicoesAdapter extends RecyclerView.Adapter<ListaInstituicoesAdapter.ExampleViewHolder> implements Filterable {

    private ArrayList<Instituicao> listaInstituicao;
    private ArrayList<Instituicao> listaInstituicaoInteira;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        onItemLongClickListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imagemRecycle;
        public TextView textoRecycle;

        public ExampleViewHolder(@NonNull View itemView, final OnItemLongClickListener listener) {
            super(itemView);
            imagemRecycle = itemView.findViewById(R.id.imagemRecycle);
            textoRecycle = itemView.findViewById(R.id.textoRecycle);

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


    public ListaInstituicoesAdapter(ArrayList<Instituicao> listaInstituicaos){
        listaInstituicao  = listaInstituicaos;
        listaInstituicaoInteira = new ArrayList<>(listaInstituicaos);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exemplo_item, viewGroup, false);
        ExampleViewHolder exampleViewHolder =  new ExampleViewHolder(view, onItemLongClickListener);
        return exampleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {

        Instituicao instituicao = listaInstituicao.get(position);
       //holder.imagemRecycle.setImageResource(R.drawable.ic_radio_button_checked_black_24dp);
        Picasso
                .get()
                .load(instituicao.getImage())
                .into(holder.imagemRecycle);
        holder.textoRecycle.setText(instituicao.getDescricao());
    }

    @Override
    public int getItemCount() {
        return listaInstituicao.size();
    }


    @Override
    public Filter getFilter() {
        return instituicaoFiltro;
    }

    private Filter instituicaoFiltro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Instituicao> listaFiltrada = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                listaFiltrada.addAll(listaInstituicaoInteira);
            } else {
                String filtro  = constraint.toString().toLowerCase().trim();

                for (Instituicao ins:
                        listaInstituicaoInteira) {
                    if(ins.getDescricao().toLowerCase().contains(filtro)){
                        listaFiltrada.add(ins);
                    }

                }
            }

            FilterResults results = new FilterResults();
            results.values = listaFiltrada;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listaInstituicao.clear();
            listaInstituicao.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


}
