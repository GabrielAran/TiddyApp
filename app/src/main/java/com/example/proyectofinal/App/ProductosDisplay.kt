package com.example.proyectofinal.App

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.proyectofinal.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_splash.*

class ProductosDisplay : Fragment() {

    lateinit var v : View
    private lateinit var textViewNombre: TextView
    private lateinit var textViewPrecio: TextView
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_productos_display, container, false)

        textViewNombre = v.findViewById(R.id.textViewNombreDisplay)
        textViewPrecio = v.findViewById(R.id.textViewPrecioDisplay)
        imageView = v.findViewById(R.id.imageViewDisplay)

        return v
    }

    override fun onStart() {
        super.onStart()

        val nombre = ProductosDisplayArgs.fromBundle(requireArguments()).nombreProducto
        val url = ProductosDisplayArgs.fromBundle(requireArguments()).urlProducto
        val precio = ProductosDisplayArgs.fromBundle(requireArguments()).precioProducto

        textViewNombre.setText(nombre)
        textViewPrecio.setText(precio)
        Picasso.get().load(url).resize(350,350).into(imageView)
    }

}