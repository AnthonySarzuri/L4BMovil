package com.example.clinicaserenidad.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicaserenidad.R
import com.example.clinicaserenidad.data.model.CitaModel

class CitaAdapter(private val citas: List<CitaModel>) :
    RecyclerView.Adapter<CitaAdapter.CitaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cita, parent, false)
        return CitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citas[position]
        holder.bind(cita)
    }

    override fun getItemCount(): Int = citas.size

    class CitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewFechaCita: TextView = itemView.findViewById(R.id.textViewFechaCita)
        private val textViewMotivo: TextView = itemView.findViewById(R.id.textViewMotivo)
        private val textViewEstado: TextView = itemView.findViewById(R.id.textViewEstado)

        fun bind(cita: CitaModel) {
            textViewFechaCita.text = "Fecha: ${cita.fechaCita}"
            textViewMotivo.text = "Motivo: ${cita.motivo}"
            textViewEstado.text = "Estado: ${cita.estado}"
        }
    }
}

