package com.example.proyectofinal.Adaptadores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.Objetos.Tareas
import com.example.proyectofinal.R

class TareasCalendarioAdaptador(private var tareasCalendarioDiarioList: MutableList<Tareas>, var context: Context, val onCheckBoxClick: (Int) -> Unit, val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<TareasCalendarioAdaptador.TareasHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareasHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tareas_calendario_diario,parent,false)
        return (TareasHolder(view))
    }

    override fun getItemCount(): Int {
        return tareasCalendarioDiarioList.size
    }

    fun setData(newData: ArrayList<Tareas>) {
        this.tareasCalendarioDiarioList = newData
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TareasHolder, position: Int) {

        holder.setNombre(tareasCalendarioDiarioList[position].nombreTarea)
        holder.setNotas(tareasCalendarioDiarioList[position].notas)
        holder.setColor(tareasCalendarioDiarioList[position].color)
        holder.getCheckBoxLayout().setOnClickListener {
            onCheckBoxClick(position)
        }
        holder.getItemLayout().setOnClickListener {
            onItemClick(position)
        }

    }
    class TareasHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var v: View

        init {
            this.v = v
        }

        fun setNombre(nombre : String) {
            val tareaNombreCalendarioDiario : TextView = v.findViewById(R.id.tareaNombreCalendarioDiario)
            tareaNombreCalendarioDiario.text = nombre
        }

        fun setNotas(notas : String) {
            val tareaNotasCalendarioDiario : TextView = v.findViewById(R.id.tareaNotasCalendarioDiario)
            tareaNotasCalendarioDiario.text = notas
        }

        fun setColor(color : Int) {
            val cardLayoutItemCalendarioDiario : ConstraintLayout = v.findViewById(R.id.cardLayoutItemCalendarioDiario)
            cardLayoutItemCalendarioDiario.setBackgroundColor(color)
        }

        fun getCheckBoxLayout (): CheckBox {
            return v.findViewById(R.id.checkBoxCalendarioDiario)
        }

        fun getItemLayout (): CardView {
            return v.findViewById(R.id.itemCalendarioDiario)
        }
    }
}