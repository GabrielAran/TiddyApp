package com.example.proyectofinal.Adaptadores

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.Objetos.Productos
import com.example.proyectofinal.R
import com.squareup.picasso.Picasso

class ProductosTiendaAdaptador (private var productosTiendaList : MutableList<Productos>, var context : Context, val onItemClick : (Int) -> Unit) : RecyclerView.Adapter<ProductosTiendaAdaptador.ProductosHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductosHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tienda,parent,false)
        return (ProductosHolder(view))
    }

    override fun getItemCount(): Int {
        return productosTiendaList.size
    }

    fun setData(newData: ArrayList<Productos>) {
        this.productosTiendaList = newData
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ProductosHolder, position: Int) {

        holder.setPrecio(productosTiendaList[position].precio)
        holder.setNombre(productosTiendaList[position].nombre)
        holder.setImagen(productosTiendaList[position].imagenUrl,context)

        holder.getCardLayout().setOnClickListener {
            onItemClick(position)
        }

    }
    class ProductosHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var v: View

        init {
            this.v = v
        }

        fun setPrecio(precio : Int) {
            val productoPrecioTienda : TextView = v.findViewById(R.id.textViewItemTiendaPrecio)
            productoPrecioTienda.text = precio.toString()
        }

        fun setNombre(nombre : String) {
            val productoPrecioTienda : TextView = v.findViewById(R.id.textViewItemTiendaNombre)
            productoPrecioTienda.text = nombre
        }

        fun setImagen(imagenUrl : String,context : Context) {
            //var storageRef: StorageReference? = null
            val imagen : ImageView = v.findViewById(R.id.imageViewItemTienda)
            //storageRef = Firebase.storage.getReferenceFromUrl(imagenUrl)
            //Glide.with(context).load(imagenUrl).into(imagen)
            Picasso.get().load(imagenUrl).resize(250,250).into(imagen)
            Log.d("LuchoUri","holderimagen:"+imagenUrl)
            //Log.d("LuchoUri","holderstorage:"+storageRef)
        }


        fun getCardLayout (): CardView {
            return v.findViewById(R.id.itemTienda)
        }
    }

}