package com.example.proyectofinal.App.Perezoso

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.navigation.findNavController
import com.example.proyectofinal.R
import pl.droidsonroids.gif.GifImageView


class Perezoso : Fragment() {

    lateinit var v: View
    lateinit var txtAux : TextView
    lateinit var btnMas: Button
    lateinit var btnComida: Button
    lateinit var btnRopa: Button
    lateinit var btnBackground: Button
    lateinit var felicidadBar: ProgressBar
    lateinit var hambreBar: ProgressBar
    lateinit var gifETiddy: GifImageView
    var hambreprogress: Int = 0
    var felicidadprogress: Int = 0
    var aux: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_perezoso, container, false)
        btnMas = v.findViewById(R.id.btnMas)
        btnRopa = v.findViewById(R.id.btnRopa)
        btnComida = v.findViewById(R.id.btnComida)
        btnBackground = v.findViewById(R.id.btnBackground)
        felicidadBar = v.findViewById(R.id.FelicidadBar)
        hambreBar = v.findViewById(R.id.HambreBar)
        gifETiddy = v.findViewById(R.id.gifETiddy)
        txtAux = v.findViewById(R.id.txtAux)
        felicidadBar.max = 100
        hambreBar.max = 100
        return v
    }

    @SuppressLint("WrongConstant")
    override fun onStart() {
        super.onStart()

        felicidadprogress = PerezosoArgs.fromBundle(requireArguments()).felicidadProgressC
        hambreprogress = PerezosoArgs.fromBundle(requireArguments()).hambreProgressC
        felicidadBar.progress = felicidadprogress
        hambreBar.progress = hambreprogress


        if (felicidadprogress >= 0 && felicidadprogress < 40) {
            gifETiddy.setBackgroundResource(R.drawable.tiddy_triste)
        }

        if (felicidadprogress >= 40 && felicidadprogress < 60) {
            gifETiddy.setBackgroundResource(R.drawable.tiddy_neutro)
        }
        if (felicidadprogress >= 60) {
            gifETiddy.setBackgroundResource(R.drawable.tiddy_feliz)
        }
        if (hambreprogress >= 70) {
            gifETiddy.setBackgroundResource(R.drawable.tiddy_triste)
        }

        Log.d("LuchoUri","felicidad:" + felicidadprogress)
        Log.d("LuchoUri","hambre:" + hambreprogress)

        btnMas.setOnClickListener {

            if(btnRopa.visibility == View.INVISIBLE && btnComida.visibility == View.INVISIBLE && btnBackground.visibility == View.INVISIBLE ) {
                btnRopa.visibility = View.VISIBLE
                btnComida.visibility = View.VISIBLE
                btnBackground.visibility = View.VISIBLE
            }
            else
            {
                btnRopa.visibility = View.INVISIBLE
                btnComida.visibility = View.INVISIBLE
                btnBackground.visibility = View.INVISIBLE
            }

            if(btnRopa.isEnabled == false && btnComida.isEnabled == false && btnBackground.isEnabled == false )
            {
                btnRopa.isEnabled = true
                btnComida.isEnabled = true
                btnBackground.isEnabled = true
            }
            else
            {
                btnRopa.isEnabled = false
                btnComida.isEnabled = false
                btnBackground.isEnabled = false
            }

        }

            btnRopa.setOnClickListener {
                val aPerezoso2PRopa = PerezosoDirections.actionPerezosoToRopa()
                v.findNavController().navigate(aPerezoso2PRopa)
            }
            btnComida.setOnClickListener {
                val aPerezoso2PComida = PerezosoDirections.actionPerezosoToComida(hambreprogress,felicidadprogress)
                v.findNavController().navigate(aPerezoso2PComida)

            }
            btnBackground.setOnClickListener {
                val aPerezoso2PBackground = PerezosoDirections.actionPerezosoToBackground()
                v.findNavController().navigate(aPerezoso2PBackground)
            }


        }

    }
