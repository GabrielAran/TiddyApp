package com.example.proyectofinal.App

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.example.proyectofinal.AgregarTarea.AgregarTareaDirections
import com.example.proyectofinal.R
import com.example.proyectofinal.bottomNavView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate


class ModificarTarea : Fragment() {

    lateinit var v : View
    lateinit var btnModificarTarea : Button
    lateinit var btnElegirColor : Button
    lateinit var seekBarImportancia : SeekBar
    lateinit var seekBarDificultad : SeekBar
    lateinit var editTextNombreTarea : EditText
    lateinit var editTextNotasTarea : EditText
    lateinit var checkBoxNotificaciones : CheckBox
    lateinit var checkBoxBloquearPantalla : CheckBox
    lateinit var calendarView: android.widget.CalendarView
    lateinit var mLayout : ConstraintLayout
    private lateinit var auth : FirebaseAuth
    lateinit var numberPickerHoras : NumberPicker
    lateinit var numberPickerMinutos : NumberPicker
    lateinit var numberPickerVeces : NumberPicker
    private var selectedDate = LocalDate.now()
    var diaLimite : Int = 0
    var mesLimite : Int = 0
    var anoLimite : Int = 0
    var diaHoy : Int = 0
    var mesHoy : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_modificar_tarea, container, false)

        btnModificarTarea = v.findViewById(R.id.btnModificarTarea)
        seekBarImportancia = v.findViewById(R.id.seekBarModificarImportancia)
        seekBarDificultad = v.findViewById(R.id.seekBarModificarDificultad)
        editTextNombreTarea = v.findViewById(R.id.editTextModificarNombreTarea)
        editTextNotasTarea = v.findViewById(R.id.editTextModificarNotasTarea)
        checkBoxNotificaciones = v.findViewById(R.id.checkBoxModificarNotificaciones)
        checkBoxBloquearPantalla = v.findViewById(R.id.checkBoxModificarBloquearPantalla)
        calendarView = v.findViewById(R.id.calendarViewModificarTarea)
        btnElegirColor = v.findViewById(R.id.btnModificarColor)
        numberPickerHoras = v.findViewById(R.id.numberPickerModificarHoras)
        numberPickerMinutos = v.findViewById(R.id.numberPickerModificarMinutos)
        numberPickerVeces = v.findViewById(R.id.numberPickerModificarVeces)
        seekBarImportancia.max = 5
        seekBarDificultad.max = 5
        numberPickerHoras.minValue = 0
        numberPickerMinutos.minValue = 1
        numberPickerVeces.minValue = 1
        numberPickerHoras.maxValue = 23
        numberPickerMinutos.maxValue = 59
        numberPickerVeces.maxValue = 7

        bottomNavView.visibility = View.INVISIBLE


        return v
    }

    override fun onStart() {
        super.onStart()

        var tarea = ModificarTareaArgs.fromBundle(requireArguments()).tarea
        seekBarDificultad.progress = tarea.dificultad
        seekBarImportancia.progress = tarea.importancia
        numberPickerHoras.value = tarea.duracion/100
        numberPickerMinutos.value = tarea.duracion%100
        numberPickerVeces.value = tarea.vecesPorSemana
        editTextNombreTarea.setText(tarea.nombreTarea) 
        editTextNotasTarea.setText(tarea.notas)

        val dia = tarea.fecha.substringAfterLast("-")
        val mes = tarea.fecha.substringAfter("-").substringBeforeLast("-")
        val anio = tarea.fecha.substringBefore("-")
        val selectedDate = dia+"/"+mes+"/"+anio
        calendarView.setDate(
            SimpleDateFormat("dd/MM/yyyy").parse(selectedDate).getTime(),
            true,
            true
        )

        calendarView.setOnDateChangeListener{ view: CalendarView, year : Int, month: Int, dayOfMonth: Int ->
            var auxMonth : Int
            auxMonth = month + 1
            diaLimite=dayOfMonth
            mesLimite=auxMonth
            anoLimite=year
        }

        btnModificarTarea.setOnClickListener {
            var duracionSuma : Int
            tarea.importancia = seekBarImportancia.progress
            tarea.dificultad = seekBarDificultad.progress
            tarea.nombreTarea = editTextNombreTarea.text.toString()
            tarea.notas = editTextNotasTarea.text.toString()
            tarea.notifiaciones = checkBoxNotificaciones.isChecked
            tarea.bloquearPantalla = checkBoxBloquearPantalla.isChecked
            tarea.vecesPorSemana = numberPickerVeces.value
            duracionSuma = numberPickerHoras.value * 100
            duracionSuma = duracionSuma + numberPickerMinutos.value
            tarea.duracion = duracionSuma
            tarea.ganancia = (tarea.dificultad*10)+(tarea.importancia*10)+tarea.duracion
            tarea.fecha = anoLimite.toString() + "-" + mesLimite.toString() + "-" + diaLimite.toString()
            val db = Firebase.firestore
            db.collection("Tareas").document(tarea.uid).set(tarea)
            val aCalendario: NavDirections = ModificarTareaDirections.actionModificarTareaToCalendarioDiario()
            v.findNavController().navigate(aCalendario)
        }

    }

}