package com.example.proyectofinal.App

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.proyectofinal.Objetos.Usuarios
import com.example.proyectofinal.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentContainerTiendaHistorial : Fragment() {

    lateinit var v : View
    lateinit var viewPagerTiendaHistorial : ViewPager2
    lateinit var tabLayoutTiendaHistorial : TabLayout
    lateinit var txtMonedas : TextView
    val db = Firebase.firestore
    lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_container_tienda_historial, container, false)

        tabLayoutTiendaHistorial = v.findViewById(R.id.tabLayoutTiendaHistorial)
        viewPagerTiendaHistorial = v.findViewById(R.id.viewPagerTiendaHistorial)
        txtMonedas = v.findViewById(R.id.txtMonedas)

        return v

    }

    override fun onStart() {
        super.onStart()
        viewPagerTiendaHistorial.setAdapter(ViewPagerAdapterTiendaHistorial(requireActivity()))

        TabLayoutMediator(tabLayoutTiendaHistorial, viewPagerTiendaHistorial, TabLayoutMediator.TabConfigurationStrategy { tab , position ->
            when (position) {
                0 -> tab.text = "Tienda"
                1 -> tab.text = "Historial"
                else -> tab.text = "undefined"
            }
        }).attach()

        auth = Firebase.auth
        val user = auth.currentUser
        val docRef = db.collection("Usuarios").document(user?.uid.toString())
        docRef.get().addOnSuccessListener { documentSnapshot ->
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

        }
        val docRef2 = db.collection("Usuarios").document(user?.uid.toString())
        docRef2.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.d("LuchoUri", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("LuchoUri", "Current data: ${snapshot.data}")
                val usuario = snapshot.toObject(Usuarios::class.java)
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
            } else {
                Log.d("LuchoUri", "Current data: null")
            }
        }
    }

  //  private fun createCardAdapter(): ViewPagerAdapterTiendaHistorial?
  //  {
  //      return ViewPagerAdapterTiendaHistorial(requireActivity())
  //  }


    class ViewPagerAdapterTiendaHistorial(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
        override fun createFragment(position: Int): Fragment {
            return when (position){
                0 -> Tienda()
                1 -> Historial()
                else -> Tienda()
            }
        }

        override fun getItemCount(): Int {
            return TAB_COUNT
        }

        companion object{
            private const val TAB_COUNT = 2
        }
    }
}