package com.example.proyectofinal.CuestionarioFragments


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.example.proyectofinal.AppActivity
import com.example.proyectofinal.Fragments.ConfirmacionArgs
import com.example.proyectofinal.Fragments.LogInDirections
import com.example.proyectofinal.MainActivity
import com.example.proyectofinal.Objetos.Tareas
import com.example.proyectofinal.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_introtuccion_cuestionario.*

class IntrotuccionCuestionario : Fragment() {

    private lateinit var v : View
    lateinit var textView10 : TextView
    lateinit var btnLogOut : Button
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_introtuccion_cuestionario, container, false)

        textView10 = v.findViewById(R.id.textView10)
        btnLogOut = v.findViewById(R.id.btnLogOut)

        return v
    }

    override fun onStart() {
        super.onStart()

        val idUsuario = ConfirmacionArgs.fromBundle(requireArguments()).idUsuario
        val db = FirebaseFirestore.getInstance()
        var nombre = ""
        db.collection("Usuarios").document(idUsuario).get().addOnSuccessListener { datos ->
            if (datos != null) {
                nombre = datos.data?.get("nombre").toString()
                textView10.setText("Hola " + nombre + "!")
            }
        }


        btnLogOut.setOnClickListener {
            val sharedPreferences : SharedPreferences
            sharedPreferences = this.requireActivity().getSharedPreferences("Archivos", Context.MODE_PRIVATE)
            var editorPreferencias : SharedPreferences.Editor = sharedPreferences.edit()
            editorPreferencias.clear()
            editorPreferencias.commit()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(requireContext() , gso)
            googleSignInClient.signOut()

            val aLogIn: NavDirections = IntrotuccionCuestionarioDirections.actionIntrotuccionCuestionarioToLogIn()
            v.findNavController().navigate(aLogIn)

        }

        startActivity(Intent(requireContext(), AppActivity::class.java))

    }

}