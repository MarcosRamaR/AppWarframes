package com.example.warframes.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.warframes.R;
import com.example.warframes.databinding.ItemProductBinding;
import com.example.warframes.models.Warframe;

import java.util.List;


public class WarframeAdapter extends RecyclerView.Adapter<WarframeAdapter.WarframeViewHolder> {
    //Lista de Warframes para el recycler
    private List<Warframe> warframes;
    private OnWarframeClickListener listener;

    //Interfaz para controlar si se hace click en los elementos de Warframe
    public interface OnWarframeClickListener{
        void onWarframeClick(Warframe warframe);

    }

    public WarframeAdapter(List<Warframe> warframes, OnWarframeClickListener listener) {
        this.warframes = warframes;
        this.listener = listener;
    }

    //Esto actualiza la lista de warframes y notificará al adapter
    public void setWarframe(List<Warframe> warframes) {
        this.warframes = warframes;
        notifyDataSetChanged();

    }

    //Esta parte define una vista para cada elemento de la lista
    @NonNull
    @Override
    public WarframeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_product,
                parent,
                false
        );
        return new WarframeViewHolder(binding);
    }

    //Une cada vista con los datos de su warframe correspondiente
    @Override
    public void onBindViewHolder(@NonNull WarframeViewHolder holder, int position) {
        Warframe warframe = warframes.get(position); //Warframe de la posición actual
        holder.bind(warframe,listener); //Une el warframe con el listener para el click
    }

    //cuenta cuantos elementos habrá en la lista
    @Override
    public int getItemCount() {
        return warframes != null ? warframes.size() : 0;
    }

    //Esto servirá para manejar la vista de cada elemento
    static class WarframeViewHolder extends RecyclerView.ViewHolder {
        //Binding es necesario para acceder a los elementos de la lista
        private final ItemProductBinding binding;

        //Constructor para binding de la vista
        public WarframeViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        //Enlaza un warframe con su vista
        public void bind(Warframe warframe, OnWarframeClickListener listener) {
            //Establece el warframe en el binding
            binding.setWarframe(warframe);
            //Crea el listener para la vista
            binding.getRoot().setOnClickListener(v -> listener.onWarframeClick(warframe));
            //Ejecutta los binding pendientes
            binding.executePendingBindings();
        }
    }
}
