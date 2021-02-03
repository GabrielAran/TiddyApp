package com.example.proyectofinal.App

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectofinal.Adaptadores.ProductosTiendaAdaptador
import com.example.proyectofinal.Objetos.Productos
import com.example.proyectofinal.Objetos.Tareas
import com.example.proyectofinal.Objetos.Usuarios
import com.example.proyectofinal.R
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_container_tienda_historial.*
import kotlinx.coroutines.tasks.await


class Tienda : Fragment() {

    lateinit var v : View
    lateinit var recProductos: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var productosAdaptador: ProductosTiendaAdaptador
    var posicion : Int = 0
    lateinit var image : ImageView
    var productosDescargados: MutableList<Productos> = ArrayList<Productos>()
    lateinit var btnMostrarComida : Button
    lateinit var btnMostrarRopa : Button
    lateinit var btnMostrarPotenciadores : Button
    lateinit var btnMostrarCajas : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tienda, container, false)

        recProductos = v.findViewById(R.id.recyclerViewTienda)
        btnMostrarComida = v.findViewById(R.id.btnMostrarComida)
        btnMostrarRopa = v.findViewById(R.id.btnMostrarRopa)
        btnMostrarPotenciadores = v.findViewById(R.id.btnMostrarPotenciadores)
        btnMostrarCajas = v.findViewById(R.id.btnMostrarCajas)

        return v
    }

    override fun onStart() {
        super.onStart()
        val db = Firebase.firestore

        productosDescargados=ArrayList<Productos>()
        productosDescargados=ArrayList<Productos>()
        btnMostrarComida.isEnabled = false
        btnMostrarRopa.isEnabled = true
        btnMostrarPotenciadores.isEnabled = true
        btnMostrarCajas.isEnabled = true


        db.collection("Productos").whereEqualTo("tipo","Comida").get().addOnSuccessListener { snapshot ->

            if (snapshot != null) {
                for (productos in snapshot) {
                    productosDescargados.add(productos.toObject<Productos>())
                }
            }
            recProductos.setHasFixedSize(true)

            linearLayoutManager = LinearLayoutManager(context)
            recProductos.layoutManager = linearLayoutManager

            productosAdaptador = ProductosTiendaAdaptador(productosDescargados,requireContext()) { position -> onItemClick(position) }
            recProductos.adapter = productosAdaptador
        }

        btnMostrarComida.setOnClickListener {
            productosDescargados=ArrayList<Productos>()
            btnMostrarComida.isEnabled = false
            btnMostrarRopa.isEnabled = true
            btnMostrarPotenciadores.isEnabled = true
            btnMostrarCajas.isEnabled = true
            db.collection("Productos").whereEqualTo("tipo","Comida").get().addOnSuccessListener { snapshot ->

                if (snapshot != null) {
                    for (productos in snapshot) {
                        productosDescargados.add(productos.toObject<Productos>())
                    }
                }

                recProductos.setHasFixedSize(true)

                linearLayoutManager = LinearLayoutManager(context)
                recProductos.layoutManager = linearLayoutManager

                productosAdaptador = ProductosTiendaAdaptador(productosDescargados,requireContext()) { position -> onItemClick(position) }
                recProductos.adapter = productosAdaptador
            }

        }
        btnMostrarRopa.setOnClickListener {
            productosDescargados=ArrayList<Productos>()
            btnMostrarComida.isEnabled = true
            btnMostrarRopa.isEnabled = false
            btnMostrarPotenciadores.isEnabled = true
            btnMostrarCajas.isEnabled = true
            db.collection("Productos").whereEqualTo("tipo","Ropa").get().addOnSuccessListener { snapshot ->

                if (snapshot != null) {
                    for (productos in snapshot) {
                        productosDescargados.add(productos.toObject<Productos>())
                    }
                }

                recProductos.setHasFixedSize(true)

                linearLayoutManager = LinearLayoutManager(context)
                recProductos.layoutManager = linearLayoutManager

                productosAdaptador = ProductosTiendaAdaptador(productosDescargados,requireContext()) { position -> onItemClick(position) }
                recProductos.adapter = productosAdaptador
            }
        }
        btnMostrarPotenciadores.setOnClickListener {
            productosDescargados=ArrayList<Productos>()
            btnMostrarComida.isEnabled = true
            btnMostrarRopa.isEnabled = true
            btnMostrarPotenciadores.isEnabled = false
            btnMostrarCajas.isEnabled = true
            db.collection("Productos").whereEqualTo("tipo","Potenciadores").get().addOnSuccessListener { snapshot ->

                if (snapshot != null) {
                    for (productos in snapshot) {
                        productosDescargados.add(productos.toObject<Productos>())
                    }
                }

                recProductos.setHasFixedSize(true)

                linearLayoutManager = LinearLayoutManager(context)
                recProductos.layoutManager = linearLayoutManager

                productosAdaptador = ProductosTiendaAdaptador(productosDescargados,requireContext()) { position -> onItemClick(position) }
                recProductos.adapter = productosAdaptador
            }
        }
        btnMostrarCajas.setOnClickListener {
            productosDescargados=ArrayList<Productos>()
            btnMostrarComida.isEnabled = true
            btnMostrarRopa.isEnabled = true
            btnMostrarPotenciadores.isEnabled = true
            btnMostrarCajas.isEnabled = false
            db.collection("Productos").whereEqualTo("tipo","Cajas").get().addOnSuccessListener { snapshot ->

                if (snapshot != null) {
                    for (productos in snapshot) {
                        productosDescargados.add(productos.toObject<Productos>())
                    }
                }

                recProductos.setHasFixedSize(true)

                linearLayoutManager = LinearLayoutManager(context)
                recProductos.layoutManager = linearLayoutManager

                productosAdaptador = ProductosTiendaAdaptador(productosDescargados,requireContext()) { position -> onItemClick(position) }
                recProductos.adapter = productosAdaptador
            }
        }
        /*//val gsReference = storage.getReferenceFromUrl("gs://tiddyapp-5774e.appspot.com/Captura.JPG")

        val storage = Firebase.storage
        val listRef = storage.reference.child("Ropa")

        listRef.listAll()
            .addOnSuccessListener { listResult ->
                listResult.prefixes.forEach { prefix ->
                    // All the prefixes under listRef.
                    // You may call listAll() recursively on them.
                    Log.d("LuchoUri","listresult:" + listResult.prefixes)
                    Log.d("LuchoUri","estoy adentro del prefixes1")
                }

                listResult.items.forEach { item ->
                    // All the items under listRef.
                    Log.d("LuchoUri","estoy adentro del prefixes2")
                    productosTienda.add(Productos("chaqueta",listResult.items[posicion].toString(),5001))
                    Log.d("LuchoUri","listresult:" + listResult.items[posicion])
                    posicion = posicion+1
                    if(posicion.equals(listResult.items.size)){
                        posicion=0
                    }
                }
                Log.d("LuchoUri","listRef:" + listRef)

                recProductos.setHasFixedSize(true)

                linearLayoutManager = LinearLayoutManager(context)
                recProductos.layoutManager = linearLayoutManager

                productosAdaptador = ProductosTiendaAdaptador(productosTienda,requireContext()){position -> onItemClick(position)}
                recProductos.adapter = productosAdaptador

                val storage2 = FirebaseStorage.getInstance()
                val ref = storage2.getReferenceFromUrl(productosTienda[1].imagenUrl)
                Log.d("LuchoUri","ref:" + ref)


            }
            .addOnFailureListener {
                Log.d("LuchoUri","error")
            }*/


    }

    fun onItemClick ( position : Int ) {
        val db = Firebase.firestore
        var auth : FirebaseAuth = Firebase.auth
        val user = auth.currentUser
        val docRef = db.collection("Usuarios").document(user?.uid.toString())
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val usuario = documentSnapshot.toObject(Usuarios::class.java)
            usuario?.dinero= usuario?.dinero!! - productosDescargados[position].precio
            db.collection("Usuarios").document(user?.uid.toString()).set(usuario)

        }
        /*docRef.get().addOnSuccessListener { documentSnapshot ->
            val usuario = documentSnapshot.toObject(Usuarios::class.java)
            if(usuario?.dinero.toString().toInt() < 100)
            {
                txtMonedas.setText("      " + usuario?.dinero)
            }
            if(usuario?.dinero.toString().toInt() >= 100)
            {
                txtMonedas.setText("     " + usuario?.dinero)
            }
            if(usuario?.dinero.toString().toInt() >= 1000)
            {
                var dineroK = usuario?.dinero.toString().toInt()/1000
                txtMonedas.setText("     "  + dineroK + "K")
            }

        }*/
    }
}