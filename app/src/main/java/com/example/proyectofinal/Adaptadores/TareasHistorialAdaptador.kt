package com.example.proyectofinal.Adaptadores

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.Objetos.Tareas
import com.example.proyectofinal.R

class TareasHistorialAdaptador (private var taresHistorialList : MutableList<Tareas>, var context : Context, val onItemClick : (Int) -> Unit) : RecyclerView.Adapter<TareasHistorialAdaptador.TareasHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareasHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tareas_historial,parent,false)
        return (TareasHolder(view))
    }

    override fun getItemCount(): Int {
        return taresHistorialList.size
    }

    fun setData(newData: ArrayList<Tareas>) {
        this.taresHistorialList = newData
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TareasHolder, position: Int) {

        holder.setNombre(taresHistorialList[position].nombreTarea)
        holder.setNotas(taresHistorialList[position].notas)
        holder.setColor(taresHistorialList[position].color)
        holder.setGanancia(taresHistorialList[position].ganancia)

        holder.getCardLayout().setOnClickListener {
            onItemClick(position)
        }

    }
    class TareasHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var v: View

        init {
            this.v = v
        }

        fun setNombre(nombre : String) {
            val tareaNombreHistorial : TextView = v.findViewById(R.id.tareaNombreHistorial)
            tareaNombreHistorial.text = nombre
        }

        fun setNotas(notas : String) {
            val tareaNotasHistorial : TextView = v.findViewById(R.id.tareaNotasHistorial)
            tareaNotasHistorial.text = notas
        }

        fun setColor(color : Int) {
            val cardLayoutItemHistorial : ConstraintLayout = v.findViewById(R.id.cardLayoutItemHistorial)
            cardLayoutItemHistorial.setBackgroundColor(color)
        }

        fun setGanancia(ganancia : Int){
            val monedasObtenidasHistorial : TextView = v.findViewById(R.id.monedasObtenidasHistorial)
            monedasObtenidasHistorial.text = ganancia.toString()
        }


        fun getCardLayout (): CardView {
            return v.findViewById(R.id.itemHistorial)
        }
    }
}