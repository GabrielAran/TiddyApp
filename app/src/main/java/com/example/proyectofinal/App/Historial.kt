package com.example.proyectofinal.App

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.Adaptadores.TareasHistorialAdaptador
import com.example.proyectofinal.Objetos.Tareas
import com.example.proyectofinal.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class Historial : Fragment() {

    lateinit var v : View
    lateinit var recTareas: RecyclerView
    var tareasHistorial: MutableList<Tareas> = ArrayList<Tareas>()
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var tareasAdaptador: TareasHistorialAdaptador
    private lateinit var auth : FirebaseAuth
    val db = Firebase.firestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_historial, container, false)
        recTareas = v.findViewById(R.id.recTareasHistorial)
        Log.d("LuchoUri","estoy en el on create")
        return v
    }

    override fun onStart() {
        super.onStart()
        Log.d("LuchoUri","estoy en el on start")

        auth = Firebase.auth
        val user = auth.currentUser
        db.collection("Tareas").whereEqualTo("duenio",user?.uid.toString()).whereEqualTo("hecha",true).get().addOnSuccessListener {snapshot ->
            Log.d("LuchoUri","estoy antes del if")
            if (snapshot != null)
            {
                Log.d("LuchoUri","estoy antes del for")

                for (tareas in snapshot) {

                    tareasHistorial.add(tareas.toObject<Tareas>())

                    Log.d("LuchoUri","estoy en el for")

                }
            }

            recTareas.setHasFixedSize(true)

            linearLayoutManager = LinearLayoutManager(context)
            recTareas.layoutManager = linearLayoutManager

            tareasAdaptador = TareasHistorialAdaptador(tareasHistorial,requireContext()){position -> onItemClick(position)}
            recTareas.adapter = tareasAdaptador

            Log.d("LuchoUri","pasé lo del recycler view")

        }

        /*recTareas.setHasFixedSize(true)

        linearLayoutManager = LinearLayoutManager(context)
        recTareas.layoutManager = linearLayoutManager

        tareasAdaptador = TareasHistorialAdaptador(tareasHistorial,requireContext()){position -> onItemClick(position)}
        recTareas.adapter = tareasAdaptador*/
    }

    fun onItemClick ( position : Int ) {
       // Snackbar.make(v,productos[position].nombre, Snackbar.LENGTH_SHORT).show()
       // val a43 = Fragment4Directions.actionFragment4ToFragment3(productos[position])
       // v.findNavController().navigate(a43)
    }

    /*
    val db = Firebase.firestore
    private lateinit var auth : FirebaseAuth
    auth = Firebase.auth
    val user = auth.currentUser
    val docRef = db.collection("Usuarios").document(user?.uid)
            docRef.get().addOnSuccessListener { documentSnapshot ->
                val usuario = documentSnapshot.toObject(Usuarios::class.java)
                textview.setText(usuario.dinero)
                }
     */

}