package com.example.proyectofinal.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.proyectofinal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RecContrasena : Fragment() {
    lateinit var v : View
    lateinit var btnRecuperarContrasena : Button
    lateinit var editTextMailRecuperarContrasena : EditText
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_rec_contrasena, container, false)

        btnRecuperarContrasena=v.findViewById(R.id.btnRecuperarContrasena)
        editTextMailRecuperarContrasena=v.findViewById(R.id.editTextMailRecuperarContrasena)
        auth = Firebase.auth

        return v
    }

    override fun onStart() {
        super.onStart()

        btnRecuperarContrasena.setOnClickListener {
            var mail: String = editTextMailRecuperarContrasena.text.toString()
            auth.sendPasswordResetEmail(mail)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("LuchoRecContrasena", "Email sent.")
                    }
                    else{
                        Log.d("LuchoRecContrasena", "Email error.")
                    }
                }
        }

    }
}