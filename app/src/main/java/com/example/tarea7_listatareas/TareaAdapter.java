package com.example.tarea7_listatareas;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    ArrayList<Tarea> coleccion;
    OnClickTarea listener;

    public TareaAdapter(ArrayList<Tarea> coleccion, OnClickTarea listener) {
        this.coleccion = coleccion;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TareaAdapter.TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarea_view, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaAdapter.TareaViewHolder holder, int position) {

        if (position >= coleccion.size()) {
            return;
        }

        Tarea tarea = coleccion.get(position);
        holder.tv_asignatura.setText(tarea.getAsignatura());
        holder.tv_descripcion.setText(tarea.getDescripcion());
        holder.tv_fecha.setText(tarea.getFecha());
        holder.tv_hora.setText(tarea.getHora());
        if (tarea.getEstado()) {
            holder.tv_estado.setText("COMPLETADA");
            holder.tv_estado.setTextColor(ContextCompat.getColor(holder.tv_estado.getContext(), R.color.green));
        } else {
            holder.tv_estado.setText("PENDIENTE");
            holder.tv_estado.setTextColor(ContextCompat.getColor(holder.tv_estado.getContext(), R.color.red));
        }

        holder.itemView.setTag(position);


        holder.itemView.setOnClickListener(v -> {
            listener.onClickTarea(v, holder.getAdapterPosition());
        });


    }

    @Override
    public int getItemCount() {
        return coleccion.size();
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder {

        TextView tv_asignatura;
        TextView tv_descripcion;
        TextView tv_fecha;
        TextView tv_hora;
        TextView tv_estado;


        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_asignatura =  itemView.findViewById(R.id.textAsignatura);
            tv_descripcion =  itemView.findViewById(R.id.textDescripcion);
            tv_fecha =  itemView.findViewById(R.id.textFecha);
            tv_hora =  itemView.findViewById(R.id.textHora);
            tv_estado =  itemView.findViewById(R.id.textEstado);

        }

    }

    public interface OnClickTarea {
        void onClickTarea(View view, int position);

    }

}
