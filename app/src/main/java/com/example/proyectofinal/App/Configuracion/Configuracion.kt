package com.example.proyectofinal.App.Configuracion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.example.proyectofinal.R

class Configuracion : Fragment() {
    lateinit var v : View
    lateinit var btnC2Cu : Button
    lateinit var btnC2T : Button
    lateinit var btnC2N : Button
    lateinit var btnC2A : Button
    lateinit var btnC2ADN : Button
    lateinit var btnC2F : Button

    override fun onCreateView(
         inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_configuracion, container, false)
        btnC2Cu = v.findViewById(R.id.btnC2Cu)
        btnC2N = v.findViewById(R.id.btnC2N)
        btnC2T = v.findViewById(R.id.btnC2T)
        btnC2A = v.findViewById(R.id.btnC2A)
        btnC2ADN = v.findViewById(R.id.btnC2ADN)
        btnC2F = v.findViewById(R.id.btnC2F)
        return v
    }
    override fun onStart() {
        super.onStart()
        btnC2Cu.setOnClickListener {
            val aC2Cu = ConfiguracionDirections.actionConfiguracionToConfiguracionCuenta()
            v.findNavController().navigate(aC2Cu)
        }
        btnC2T.setOnClickListener {
            val aC2T = ConfiguracionDirections.actionConfiguracionToConfiguracionTema()
            v.findNavController().navigate(aC2T)
        }
        btnC2N.setOnClickListener {
            val aC2N = ConfiguracionDirections.actionConfiguracionToConfiguracionNotificaciones()
            v.findNavController().navigate(aC2N)
        }
        btnC2A.setOnClickListener {
            val aC2A = ConfiguracionDirections.actionConfiguracionToConfiguracionAyuda()
            v.findNavController().navigate(aC2A)
        }
        btnC2ADN.setOnClickListener {
            val aC2ADN = ConfiguracionDirections.actionConfiguracionToConfiguracionAcercaDeNosotros()
            v.findNavController().navigate(aC2ADN)
        }
        btnC2F.setOnClickListener {
            val aC2F = ConfiguracionDirections.actionConfiguracionToConfiguracionFeedback()
            v.findNavController().navigate(aC2F)
        }
    }
}