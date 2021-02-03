package com.example.proyectofinal.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.proyectofinal.AppActivity
import com.example.proyectofinal.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Confirmacion : Fragment() {

    private lateinit var v : View
    lateinit var textViewUsuarioConfirmacion : TextView
    lateinit var textViewTextoConfirmar : TextView
    lateinit var btnConfirmarEmail : Button



override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                          savedInstanceState: Bundle?): View? {
    v = inflater.inflate(R.layout.fragment_confirmacion, container, false)

    textViewUsuarioConfirmacion = v.findViewById(R.id.textViewUsuarioConfirmacion)
    textViewTextoConfirmar = v.findViewById(R.id.textViewTextoConfirmar)
    btnConfirmarEmail = v.findViewById(R.id.btnConfirmarEmail)

    InicializarFirebase()

    return v
}

    override fun onStart() {
        super.onStart()
        var idUsuario = ConfirmacionArgs.fromBundle(requireArguments()).idUsuario
        var db = FirebaseFirestore.getInstance()
        var nombre = ""
        var mail = ""
        db.collection("Usuarios").document(idUsuario).get().addOnSuccessListener { datos ->
            if (datos != null) {
                nombre = datos.data?.get("nombre").toString()
                mail = datos.data?.get("mail").toString()
                textViewUsuarioConfirmacion.setText("Hola " + nombre + "!")
                textViewTextoConfirmar.setText("Te enviamos un correo de confirmación a tu email: " + mail + ". Por favor, da click en el boton para confirmar tu cuenta")
            }
        }

        btnConfirmarEmail.setOnClickListener {
            var user = Firebase.auth.currentUser
            var emailVerified = user?.isEmailVerified
            Log.d("Lucho","mail verificado:" + emailVerified)
            val intent = Intent(requireContext(), AppActivity::class.java)
            startActivity(intent)
        }

    }



    private fun InicializarFirebase() {
        FirebaseApp.initializeApp(requireContext())
    }

}