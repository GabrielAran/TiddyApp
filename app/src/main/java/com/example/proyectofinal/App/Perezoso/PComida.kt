package com.example.proyectofinal.App.Perezoso

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.example.proyectofinal.R
import pl.droidsonroids.gif.GifImageView

class PComida : Fragment() {

    lateinit var v: View
    lateinit var btnIncrementoC: Button
    lateinit var btnDecrementoC: Button
    lateinit var gifETiddyC: GifImageView
    var hambreprogressC: Int = 0
    var felicidadprogressC: Int = 0
    lateinit var btnComida2Principal : Button
    lateinit var felicidadprogressCString : String
    lateinit var hambreprogressCString : String

    /*ONCREATE*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_p_comida, container, false)
        btnIncrementoC = v.findViewById(R.id.btnIncrementoC)
        btnDecrementoC = v.findViewById(R.id.btnDecrementoC)
        gifETiddyC = v.findViewById(R.id.gifETiddyC)
        btnComida2Principal = v.findViewById(R.id.btnComida2Principal)
        return v
    }

    /*ONCREATE*/
    override fun  onStart() {
        super.onStart()


        felicidadprogressC = PComidaArgs.fromBundle(requireArguments()).felicidadProgress
        hambreprogressC = PComidaArgs.fromBundle(requireArguments()).hambreProgress

        btnIncrementoC.setOnClickListener {
            felicidadprogressC = felicidadprogressC + 10
            hambreprogressC = hambreprogressC + 5
            felicidadprogressCString = felicidadprogressC.toString()
            hambreprogressCString = hambreprogressC.toString()
            if (felicidadprogressC >= 0 && felicidadprogressC < 40) {
                gifETiddyC.setBackgroundResource(R.drawable.tiddy_triste)
            }

            if (felicidadprogressC >= 40 && felicidadprogressC < 60) {
                gifETiddyC.setBackgroundResource(R.drawable.tiddy_neutro)
            }
            if (felicidadprogressC >= 60) {
                gifETiddyC.setBackgroundResource(R.drawable.tiddy_feliz)
            }
            if (hambreprogressC >= 70) {
                gifETiddyC.setBackgroundResource(R.drawable.tiddy_triste)
            }

            /* limito los progresos*/
            if (felicidadprogressC >= 100) {
                felicidadprogressC = 100
            }
            if (hambreprogressC >= 100) {
                hambreprogressC = 100
            }
        }
        btnDecrementoC.setOnClickListener {
            felicidadprogressC = felicidadprogressC - 10
            hambreprogressC = hambreprogressC - 5
            hambreprogressCString = hambreprogressC.toString()
            felicidadprogressCString = felicidadprogressC.toString()

            if (felicidadprogressC >= 0 && felicidadprogressC < 40) {
                gifETiddyC.setBackgroundResource(R.drawable.tiddy_triste)
            }

            if (felicidadprogressC >= 40 && felicidadprogressC < 60) {
                gifETiddyC.setBackgroundResource(R.drawable.tiddy_neutro)
            }
            if (felicidadprogressC >= 60) {
                gifETiddyC.setBackgroundResource(R.drawable.tiddy_feliz)
            }
            if (hambreprogressC >= 70) {
                gifETiddyC.setBackgroundResource(R.drawable.tiddy_triste)
            }
            /* limito los progresos*/
            if (felicidadprogressC <= 0) {
                felicidadprogressC = 0
            }
            if (hambreprogressC <= 0) {
                hambreprogressC = 0
            }
        }


        btnComida2Principal.setOnClickListener {
            val aPComida2Perezoso = PComidaDirections.actionPComidaToPerezoso(hambreprogressC,felicidadprogressC)
            v.findNavController().navigate(aPComida2Perezoso)
        }
    }
}