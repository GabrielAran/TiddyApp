package com.example.proyectofinal.AgregarTarea

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.example.proyectofinal.Objetos.Tareas
import com.example.proyectofinal.R
import com.example.proyectofinal.bottomNavView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.coroutines.*
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.coroutines.tasks.await


class AgregarTarea : Fragment() {

    private lateinit var v : View
    lateinit var btnAgregarTarea : Button
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
    var diaFecha : Int = 0
    lateinit var bottomBar : BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_agregar_tarea, container, false)

        btnAgregarTarea = v.findViewById(R.id.btnAgregarTarea)
        seekBarImportancia = v.findViewById(R.id.seekBarImportancia)
        seekBarDificultad = v.findViewById(R.id.seekBarDificultad)
        editTextNombreTarea = v.findViewById(R.id.editTextNombreTarea)
        editTextNotasTarea = v.findViewById(R.id.editTextNotasTarea)
        checkBoxNotificaciones = v.findViewById(R.id.checkBoxNotificaciones)
        checkBoxBloquearPantalla = v.findViewById(R.id.checkBoxBloquearPantalla)
        calendarView = v.findViewById(R.id.calendarViewAgregarTarea)
        btnElegirColor = v.findViewById(R.id.btnElegirColor)
        numberPickerHoras = v.findViewById(R.id.numberPickerHoras)
        numberPickerMinutos = v.findViewById(R.id.numberPickerMinutos)
        numberPickerVeces = v.findViewById(R.id.numberPickerVeces)
        seekBarImportancia.max = 5
        seekBarDificultad.max = 5
        numberPickerHoras.minValue = 0
        numberPickerMinutos.minValue = 1
        numberPickerVeces.minValue = 1
        numberPickerHoras.maxValue = 23
        numberPickerMinutos.maxValue = 59
        numberPickerVeces.maxValue = 7

        mLayout = v.findViewById(R.id.frameLayout19)

        bottomNavView.visibility = View.INVISIBLE

        return v
    }

    override fun onStart() {
        super.onStart()

        var datosDeTarea = Tareas("0", 0, "0",0,0,0,0,false,false, "0",0,"0", false,"",0,"",0)
        var fecha : String = "0"
        var date : Long = calendarView.date
        val parentJob = Job()
        val scope = CoroutineScope(Dispatchers.Main + parentJob)
        Log.d("LuchoUri","diaHoy1: " + selectedDate.month.toString())
        Log.d("LuchoUri","diaHoy2: " + selectedDate.monthValue.toString())

        btnAgregarTarea.setOnClickListener {
            auth = Firebase.auth
            val user = auth.currentUser
            var duracionSuma : Int
            datosDeTarea.importancia = seekBarImportancia.progress
            datosDeTarea.dificultad = seekBarDificultad.progress
            datosDeTarea.nombreTarea = editTextNombreTarea.text.toString()
            datosDeTarea.notas = editTextNotasTarea.text.toString()
            datosDeTarea.notifiaciones = checkBoxNotificaciones.isChecked
            datosDeTarea.bloquearPantalla = checkBoxBloquearPantalla.isChecked
            datosDeTarea.fechaLimite = fecha
            datosDeTarea.vecesPorSemana = numberPickerVeces.value
            duracionSuma = numberPickerHoras.value * 100
            duracionSuma = duracionSuma + numberPickerMinutos.value
            datosDeTarea.duenio = user?.uid.toString()
            datosDeTarea.duracion = duracionSuma
            datosDeTarea.ganancia = (datosDeTarea.dificultad*10)+(datosDeTarea.importancia*10)+datosDeTarea.duracion
            diaHoy = selectedDate.toString().substringAfterLast("-").toInt()
            mesHoy = selectedDate.monthValue
            datosDeTarea.uid = UUID.randomUUID().toString()
            Log.d("LuchoUri","diaHoy: " + diaHoy)
            Log.d("LuchoUri","diaHoy: " + selectedDate.toString())


            scope.launch {
                datosDeTarea.fecha=CalcularFechaYHora(diaLimite,mesLimite,anoLimite,datosDeTarea.importancia,datosDeTarea.dificultad,diaHoy,mesHoy)
                datosDeTarea.diaFecha=datosDeTarea.fecha.substringAfterLast("-").toInt()
                SubirTarea(datosDeTarea)
                Snackbar.make(v,"tarea agendada para el: " + datosDeTarea.fecha, Snackbar.LENGTH_LONG).show()
                PasarACalendario(v)
            }
        }

        btnElegirColor.setOnClickListener {
            val colorPicker = AmbilWarnaDialog(requireContext(), datosDeTarea.color, object : OnAmbilWarnaListener {
                    override fun onCancel(dialog: AmbilWarnaDialog) {}
                    override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                        datosDeTarea.color = color
                        btnElegirColor.setBackgroundColor(datosDeTarea.color)
                    }
                })
            colorPicker.show()
        }

        calendarView.setOnDateChangeListener{ view: CalendarView, year : Int, month: Int, dayOfMonth: Int ->
            var auxMonth : Int
            auxMonth = month + 1
            Log.d("LuchoUri","calendarView.date: " + calendarView.date)
            Log.d("LuchoUri","calendarView.getDate(): " + calendarView.getDate())
            Log.d("LuchoUri","date: " + date)
            Log.d("LuchoUri","fecha: " + fecha)
            Log.d("LuchoUri","year: " + year.toString())
            Log.d("LuchoUri","month: " + auxMonth.toString())
            Log.d("LuchoUri","day: " + dayOfMonth.toString())
            fecha = year.toString() + "-" + auxMonth.toString() + "-" + dayOfMonth.toString()
            diaLimite=dayOfMonth
            mesLimite=auxMonth
            anoLimite=year

            /*if (calendarView.date !=date){
                fecha = year.toString() + "-" + month.toString() + "-" + dayOfMonth.toString()
                Log.d("LuchoUri","fecha: " + fecha)
                Log.d("LuchoUri","year: " + year.toString())
                Log.d("LuchoUri","month: " + month.toString())
                Log.d("LuchoUri","day: " + dayOfMonth.toString())
            }*/
            Log.d("LuchoUri","entré")
        }

    }

}

suspend fun CalcularFechaYHora(diaLimite : Int, mesLimite : Int, anoLimite : Int, importancia : Int, dificultad : Int, diaHoy : Int, mesHoy : Int): String {
    var fecha : String = "0"
    var resultado : Double
    var hora : Int
    val db = Firebase.firestore
    var auth : FirebaseAuth = Firebase.auth
    val user = auth.currentUser
    var tareasDescargadas1: MutableList<Tareas> = ArrayList<Tareas>()
    var tareasDescargadas2: MutableList<Tareas> = ArrayList<Tareas>()
    var tareasDescargadas3: MutableList<Tareas> = ArrayList<Tareas>()
    var auxDia : Double = 0.0
    var auxCuenta1 : Int = 0
    var auxCuenta2 : Int = 0
    var auxCuenta3 : Int = 0
    resultado = ((importancia + dificultad).toDouble()/2)
    auxDia = (diaLimite-resultado)
    Log.d("LuchoUri", "auxDia antes del If: " + resultado)
    if (auxDia%2 == 0.5){
        auxDia = (auxDia-0.5)
        Log.d("LuchoUri", "auxDia en el If: " + auxDia)
    }
    if (auxDia <= 0){
        auxDia = 1.0
    }
    if (auxDia<diaHoy&&mesLimite==mesHoy){
        auxDia= diaHoy.toDouble()
    }
    if (auxDia<10){
        fecha = anoLimite.toString() + "-" + mesLimite.toString() + "-" + "0"+auxDia.toInt().toString()
    }else{
        fecha = anoLimite.toString() + "-" + mesLimite.toString() + "-" + auxDia.toInt().toString()}

    val parentJob = Job()
    val scope = CoroutineScope(Dispatchers.Main + parentJob)

        auxCuenta1 = accion1(auxDia-1)
        auxCuenta2 = accion1(auxDia)
        auxCuenta3 = accion1(auxDia+1)
        Log.d("LuchoUri", "auxCuenta1AuxCuenta: " + auxCuenta1)
        Log.d("LuchoUri", "auxCuenta2AuxCuenta: " + auxCuenta2)
        Log.d("LuchoUri", "auxCuenta3AuxCuenta: " + auxCuenta3)
        if (comparacion(auxCuenta1,auxCuenta2,auxCuenta3)==1)
        {
            Log.d("LuchoUri", "comparacion es 1")
            auxDia=auxDia-1
            if (auxDia < 10 && mesLimite < 10){
                fecha = anoLimite.toString() + "-" + "0" + mesLimite.toString() + "-" + "0" + auxDia.toInt().toString()
            }else if (auxDia < 10 && mesLimite >= 10){
                fecha = anoLimite.toString() + "-" + mesLimite.toString() + "-" + "0" + auxDia.toInt().toString()
            }else if(auxDia >= 10 && mesLimite < 10){
                fecha = anoLimite.toString() + "-" + "0" + mesLimite.toString() + "-" +auxDia.toInt().toString()
            }else{
                fecha = anoLimite.toString() + "-" + mesLimite.toString() + "-" + auxDia.toInt().toString()
            }
        }else if (comparacion(auxCuenta1,auxCuenta2,auxCuenta3)==2)
        {
            Log.d("LuchoUri", "comparacion es 2")
            if (auxDia < 10 && mesLimite < 10){
                fecha = anoLimite.toString() + "-" + "0" + mesLimite.toString() + "-" + "0" + auxDia.toInt().toString()
            }else if (auxDia < 10 && mesLimite >= 10){
                fecha = anoLimite.toString() + "-" + mesLimite.toString() + "-" + "0" + auxDia.toInt().toString()
            }else if(auxDia >= 10 && mesLimite < 10){
                fecha = anoLimite.toString() + "-" + "0" + mesLimite.toString() + "-" +auxDia.toInt().toString()
            }else{
                fecha = anoLimite.toString() + "-" + mesLimite.toString() + "-" + auxDia.toInt().toString()
            }
        }else{
            Log.d("LuchoUri", "comparacion es 3")
            auxDia=auxDia+1
            if (auxDia < 10 && mesLimite < 10){
                fecha = anoLimite.toString() + "-" + "0" + mesLimite.toString() + "-" + "0" + auxDia.toInt().toString()
            }else if (auxDia < 10 && mesLimite >= 10){
                fecha = anoLimite.toString() + "-" + mesLimite.toString() + "-" + "0" + auxDia.toInt().toString()
            }else if(auxDia >= 10 && mesLimite < 10){
                fecha = anoLimite.toString() + "-" + "0" + mesLimite.toString() + "-" +auxDia.toInt().toString()
            }else{
                fecha = anoLimite.toString() + "-" + mesLimite.toString() + "-" + auxDia.toInt().toString()
            }
        }
    Log.d("LuchoUri","Fecha: " + fecha)
    return fecha
}

suspend fun accion1(auxDia: Double): Int {
    //delay(1000)
    val db = Firebase.firestore
    var auth : FirebaseAuth = Firebase.auth
    val user = auth.currentUser

    var tareasDescargadas1: MutableList<Tareas> = ArrayList<Tareas>()
    var auxCuenta1 : Int = 0

    val questionRef = db.collection("Tareas").whereEqualTo("duenio",user?.uid.toString()).whereEqualTo("diaFecha", auxDia)
    val query = questionRef

    try {
        val data = query.get().await()
        for (tareas in data) {
            tareasDescargadas1.add(tareas.toObject<Tareas>())
        }
        for (position in tareasDescargadas1) {
            auxCuenta1=auxCuenta1+position.duracion
            Log.d("LuchoUri", "auxCuenta: " + auxCuenta1)
        }
    }catch (e: Exception){
        auxCuenta1=0
    }
    return auxCuenta1
}

suspend fun comparacion(auxCuenta1 : Int, auxCuenta2 : Int, auxCuenta3 : Int): Int {
    var aux = 0
    if (auxCuenta1<=auxCuenta2&&auxCuenta1<auxCuenta3){
        Log.d("LuchoUri2", "auxCuenta1: " + auxCuenta1)
        Log.d("LuchoUri2", "auxCuenta2: " + auxCuenta2)
        Log.d("LuchoUri2", "auxCuenta3: " + auxCuenta3)
        aux = 1
    }
    if (auxCuenta2<=auxCuenta1&&auxCuenta2<auxCuenta3){
        aux = 2
    }
    if (auxCuenta3<=auxCuenta1&&auxCuenta3<auxCuenta2){
        aux = 3
    }
    return aux
}

suspend fun SubirTarea(datosDeTarea : Tareas){
    val db = Firebase.firestore

    db.collection("Tareas").document(datosDeTarea.uid).set(datosDeTarea)
}
suspend fun PasarACalendario(v : View){
    val aCalendario: NavDirections = AgregarTareaDirections.actionAgregarTareaToCalendarioDiario()
    v.findNavController().navigate(aCalendario)
}