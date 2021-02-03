package com.example.proyectofinal.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.example.proyectofinal.Objetos.Usuarios
import com.example.proyectofinal.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*


@Suppress("UNREACHABLE_CODE")
class Registro : Fragment() {

    lateinit var btnIrLogIn : Button
    lateinit var btnRegistro : Button
    lateinit var editTextMail : EditText
    lateinit var editTextNombre : EditText
    lateinit var editTextContrasena : EditText
    lateinit var editTextCContrasena : EditText
    private lateinit var  v : View
    lateinit var firebaseDatabase : FirebaseDatabase
    lateinit var databaseReference : DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_registro, container, false)
        ConfiguracionId()
        InicializarFirebase()
        return v
    }


    override fun onStart() {
        super.onStart()

        btnRegistro.setOnClickListener {

            val db = FirebaseFirestore.getInstance()

            var mail: String = editTextMail.text.toString()
            var nombre: String = editTextNombre.text.toString()
            var contrasena: String = editTextContrasena.text.toString()
            var ccontrasena: String = editTextCContrasena.text.toString()

            if (nombre.length < 20){
                if (contrasena.length < 6 || contrasena.length > 20) {
                    Toast.makeText(context,"La contraseña debe tener un mínimo de 6 caracteres y un máximo de 20",Toast.LENGTH_LONG).show()
                } else {
                    if (contrasena == ccontrasena) {
                        auth.createUserWithEmailAndPassword(mail, contrasena)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val user = auth.currentUser
                                                updateUI(user)
                                                verifyEmail(user)

                                    var nuevoUsuario = Usuarios(user?.uid.toString(), mail, nombre,0)
                                    db.collection("Usuarios").document(user?.uid.toString())
                                        .set(nuevoUsuario)

                                    val aConfirmacion: NavDirections =
                                        RegistroDirections.actionRegistroToConfirmacion(nuevoUsuario.uid)
                                    v.findNavController().navigate(aConfirmacion)

                                } else {
                                    //Log.d("luchoRegistro", "createUserWithEmail:failure" + task.exception)
                                    updateUI(null)
                                }
                            }
                    } else {
                        Toast.makeText(context, "Las contraseñas no son iguales", Toast.LENGTH_LONG).show()
                    }
                }
            }
            else{
                Toast.makeText(context,"El nombre de usuario debe tener un máximo de 20 caracteres",Toast.LENGTH_LONG).show()
            }
        }

        btnIrLogIn.setOnClickListener {
            val aLogIn: NavDirections = RegistroDirections.actionRegistroToLogIn()
            v.findNavController().navigate(aLogIn)
        }

    }


    private fun ConfiguracionId() {
        btnIrLogIn = v.findViewById(R.id.btnIrLogIn)
        btnRegistro = v.findViewById(R.id.btnRegistro)
        editTextMail = v.findViewById(R.id.editTextMail)
        editTextNombre = v.findViewById(R.id.editTextNombre)
        editTextContrasena = v.findViewById(R.id.editTextContrasena)
        editTextCContrasena = v.findViewById(R.id.editTextCContrasena)
    }
    private fun InicializarFirebase() {
        FirebaseApp.initializeApp(requireContext())
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference()
        auth = Firebase.auth
    }
    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(getContext(), "Usuario registrado correctamente", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(getContext(), "Fallo al registrarse", Toast.LENGTH_LONG).show()
        }
    }
    fun verifyEmail(user: FirebaseUser?) {

        user?.sendEmailVerification()
            ?.addOnCompleteListener() { task ->

                /*             if(task.isComplete) {

                                   Snackbar.make(v, "Email enviado", Snackbar.LENGTH_SHORT).show()

                               }
                               else {
                                   Snackbar.make(v, "Error", Snackbar.LENGTH_SHORT).show()

                               }*/
            }
    }



    /*private fun Fail(){
    btnRegistro.setOnClickListener {

        var mail : String = editTextMail.text.toString()
        var nombre : String = editTextNombre.text.toString()
        var contrasena : String = editTextContrasena.text.toString()
        var ccontrasena : String = editTextCContrasena.text.toString()
        var nuevoUsuario : Usuarios

        databaseReference.child("Usuarios").child(mail).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    Toast.makeText(requireContext(),"El mail ingresado ya fue utilizado",Toast.LENGTH_SHORT).show()
                }
                else{
                    if(contrasena.equals(ccontrasena)){
                        nuevoUsuario = Usuarios((UUID.randomUUID().toString()),mail,nombre)
                        databaseReference.child("Usuarios").child(nuevoUsuario.mail).setValue(nuevoUsuario)
                        Toast.makeText(requireContext(),"Usuario registrado con éxito",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }
    btnAtras.setOnClickListener {
        val aLogIn: NavDirections = RegistroDirections.actionRegistroToLogIn()
        v.findNavController().navigate(aLogIn)
    }
}*/
}