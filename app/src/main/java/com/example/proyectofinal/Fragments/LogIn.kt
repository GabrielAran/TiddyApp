package com.example.proyectofinal.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.example.proyectofinal.AppActivity
import com.example.proyectofinal.Objetos.Usuarios
import com.example.proyectofinal.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class LogIn : Fragment() {


    lateinit var btnLogIn : Button
    lateinit var btnIrRegistro : Button
    lateinit var btnRecContrasena : Button
    lateinit var editTextMailLogIn : EditText
    lateinit var editTextContrasenaLogIn : EditText
    lateinit var v : View
    lateinit var CboxMantenerSesion : CheckBox
    lateinit var firebaseDatabase : FirebaseDatabase
    lateinit var databaseReference : DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var btnLoginGoogle: Button
    private lateinit var googleSignInClient: GoogleSignInClient
    private var RC_SIGN_IN : Int = 1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_log_in, container, false)
        ConfiguracionId()
        InicializarFirebase()
        ConfiguracionGoogleSignIn()
        return v
    }


    override fun onStart() {
        super.onStart()
        val sharedPreferences : SharedPreferences
        sharedPreferences = this.requireActivity().getSharedPreferences("Archivos", Context.MODE_PRIVATE)
        var uidAchivado : String = sharedPreferences.getString("uid","0").toString()
        if (uidAchivado=="0") {
            btnLogIn.setOnClickListener {
                var mail: String = editTextMailLogIn.text.toString()
                var contrasenaIngresada: String = editTextContrasenaLogIn.text.toString()

                auth.signInWithEmailAndPassword(mail, contrasenaIngresada)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //Log.d("luchoLogIn", "signInWithEmail:success")
                            val user = auth.currentUser
                            updateUI(user)
                            Toast.makeText(getContext(), "Usuario ingresado", Toast.LENGTH_SHORT).show()

                        } else {
                            Toast.makeText(getContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }


            }
            btnIrRegistro.setOnClickListener {
                val aRegistro: NavDirections = LogInDirections.actionLogInToRegistro()
                v.findNavController().navigate(aRegistro)
            }

            btnLoginGoogle.setOnClickListener {
                SignInGoogle()
            }

            btnRecContrasena.setOnClickListener {
                val aRecContrasena: NavDirections = LogInDirections.actionLogInToRecContrasena()
                v.findNavController().navigate(aRecContrasena)
            }
        }
        else{
            val aIntroduccionCuestionario: NavDirections = LogInDirections.actionLogInToIntrotuccionCuestionario(uidAchivado)
            v.findNavController().navigate(aIntroduccionCuestionario)
        }

    }


    private fun ConfiguracionId() {
        btnLogIn = v.findViewById(R.id.btnLogIn)
        btnIrRegistro = v.findViewById(R.id.btnIrRegistro)
        editTextMailLogIn = v.findViewById(R.id.editTextMailLogIn)
        editTextContrasenaLogIn = v.findViewById(R.id.editTextContrasenaLogIn)
        CboxMantenerSesion = v.findViewById(R.id.CboxMantenerSesion)
        btnRecContrasena = v.findViewById(R.id.btnRecContrasena)

        btnLoginGoogle = v.findViewById(R.id.btnLoginGoogle)
    }
    private fun InicializarFirebase() {
        FirebaseApp.initializeApp(requireContext())
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference()
        auth = Firebase.auth
    }
    fun ConfiguracionGoogleSignIn(){

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext() , gso)

    }
    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(getContext(), "Usuario logeado correctamente", Toast.LENGTH_LONG).show()
            if (CboxMantenerSesion.isChecked==true){
                GuardarDatos(account)
            }
           // val aIntroduccionCuestionario: NavDirections = LogInDirections.actionLogInToIntrotuccionCuestionario(account.uid.toString())
           // v.findNavController().navigate(aIntroduccionCuestionario)

            startActivity(Intent(requireContext(), AppActivity::class.java))

        } else {
            Toast.makeText(getContext(), "Fallo al loggearse", Toast.LENGTH_LONG).show()
        }
    }

    //funciones exclusivas de Log In con Google
    private fun SignInGoogle() {val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                FirebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(requireContext(),"Algo salió mal al iniciar sesión con Google",Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun FirebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    updateUIGoogle(user)
                } else {
                    // If sign in fails, display a message to the user.
                    // ...
                    Toast.makeText(requireContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show()
                    updateUIGoogle(null)
                }

                // ...
            }
    }
    fun updateUIGoogle(account: FirebaseUser?) {
        if (account != null) {
            val db = FirebaseFirestore.getInstance()
            var personName = account?.displayName.toString().substringBefore(" ")
            var personEmail = account?.email.toString()
            Toast.makeText(getContext(), "Usuario logeado correctamente", Toast.LENGTH_LONG).show()
            var nuevoUsuario = Usuarios(account.uid.toString(), personEmail, personName,0)
            db.collection("Usuarios").document(account.uid.toString()).set(nuevoUsuario)
            if (CboxMantenerSesion.isChecked==true){
                GuardarDatos(account)
            }
            val aIntroduccionCuestionario: NavDirections = LogInDirections.actionLogInToIntrotuccionCuestionario(account.uid.toString())
            v.findNavController().navigate(aIntroduccionCuestionario)

        } else {
            Toast.makeText(getContext(), "Fallo al loggearse", Toast.LENGTH_LONG).show()
        }
    }

    fun GuardarDatos(account: FirebaseUser?){
        val sharedPreferences : SharedPreferences
        sharedPreferences = this.requireActivity().getSharedPreferences("Archivos", Context.MODE_PRIVATE)
        var editorPreferencias : SharedPreferences.Editor = sharedPreferences.edit()
        editorPreferencias.putString("uid", account?.uid.toString())
        editorPreferencias.commit()

    }

    fun CheckearDatos(){
        val sharedPreferences : SharedPreferences
        sharedPreferences = this.requireActivity().getSharedPreferences("Archivos", Context.MODE_PRIVATE)
        var uidAchivado : String? = sharedPreferences.getString("uid","0")

    }


    /*private fun Fail(){
        databaseReference.child("Usuarios").child(mail).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var contrasenaGuardada : String = snapshot.child("contrasena").getValue().toString()
                    if (contrasenaGuardada.equals(contrasenaIngresada))
                    {
                        Toast.makeText(requireContext(),"Has ingresado con éxito", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(),"Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(requireContext(),"El mail con el que desea ingresar no existe", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }*/
}



