package com.example.proyectofinal.App

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import com.example.proyectofinal.App.CalendarioDiario
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.Adaptadores.TareasCalendarioAdaptador
import com.example.proyectofinal.Adaptadores.TareasHistorialAdaptador
import com.example.proyectofinal.Objetos.Tareas
import com.example.proyectofinal.Objetos.Usuarios
import com.example.proyectofinal.R
import com.example.proyectofinal.bottomNavView
import com.example.proyectofinal.databinding.FragmentCalendarioDiarioBinding
import com.example.proyectofinal.databinding.ItemDiaCalendarioDiarioBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.core.ActivityScope.bind
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.Size
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.fragment_agregar_tarea.*
import kotlinx.android.synthetic.main.fragment_calendario_diario.*
import kotlinx.android.synthetic.main.item_dia_calendario_diario.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarioDiario : Fragment() {

    lateinit var v : View
    lateinit var btnAgregarTareaDiario : Button
    val toolbar: Toolbar?
        get() = binding.exSevenToolbar

    private var selectedDate = LocalDate.now()

    lateinit var recTareas: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var tareasAdaptador: TareasCalendarioAdaptador
    private val dateFormatter = DateTimeFormatter.ofPattern("dd")
    private val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    private val monthFormatter = DateTimeFormatter.ofPattern("MMM")
    private lateinit var binding: FragmentCalendarioDiarioBinding
    private lateinit var auth : FirebaseAuth
    val db = Firebase.firestore
    lateinit var diaElegido : String
    lateinit var prueba : CheckBox
    lateinit var dotView : View
    lateinit var bottomBar : BottomNavigationView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_calendario_diario, container, false)

        btnAgregarTareaDiario = v.findViewById(R.id.btnAgregarTareaDiario)
        recTareas = v.findViewById(R.id.recTareasCalendarioDiario)
        bottomNavView.visibility = View.VISIBLE

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarioDiarioBinding.bind(view)

        val dm = DisplayMetrics()
        val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        binding.exSevenCalendar.apply {
            val dayWidth = dm.widthPixels / 5
            val dayHeight = (dayWidth * 1.25).toInt()
            daySize = Size(dayWidth, dayHeight)
        }

        class DayViewContainer(view: View) : ViewContainer(view) {
            val bind = ItemDiaCalendarioDiarioBinding.bind(view)
            lateinit var day: CalendarDay


            init {
                view.setOnClickListener {
                    val firstDay = binding.exSevenCalendar.findFirstVisibleDay()
                    val lastDay = binding.exSevenCalendar.findLastVisibleDay()
                    if (firstDay == day) {

                        binding.exSevenCalendar.smoothScrollToDate(day.date)
                    } else if (lastDay == day) {

                        binding.exSevenCalendar.smoothScrollToDate(day.date.minusDays(4))
                    }

                    if (selectedDate != day.date) {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        diaElegido = selectedDate.toString()
                        binding.exSevenCalendar.notifyDateChanged(day.date)
                        oldDate?.let { binding.exSevenCalendar.notifyDateChanged(it) }

                        Log.d("LuchoUri","la fecha seleccionada es:" + selectedDate)
                        Log.d("LuchoUri","la fecha seleccionada es:" + diaElegido)

                        auth = Firebase.auth
                        val user = auth.currentUser
                        db.collection("Tareas").whereEqualTo("duenio",user?.uid.toString()).whereEqualTo("hecha",false).whereEqualTo("fecha",diaElegido).get().addOnSuccessListener {snapshot ->
                            Log.d("LuchoUri","estoy antes del if")

                            var tareasCalendario: MutableList<Tareas> = ArrayList<Tareas>()
                            if (snapshot != null)
                            {
                                Log.d("LuchoUri","estoy antes del for")
                                for (tareas in snapshot) {
                                    tareasCalendario.add(tareas.toObject<Tareas>())
                                    Log.d("LuchoUri","estoy en el for")
                                    bind.exSevenDayDot.visibility = View.VISIBLE
                                }
                            }
                            recTareas.setHasFixedSize(true)

                            linearLayoutManager = LinearLayoutManager(context)
                            recTareas.layoutManager = linearLayoutManager

                            tareasAdaptador = TareasCalendarioAdaptador(tareasCalendario,requireContext(),{position -> onCheckBoxClick(position,tareasCalendario)},{position -> onItemClick(position,tareasCalendario)})
                            recTareas.adapter = tareasAdaptador
                            Log.d("LuchoUri","pasé lo del recycler view")
                        }

                    }
                }
            }

            fun bind(day: CalendarDay) {
                this.day = day
                bind.exSevenDateText.text = dateFormatter.format(day.date)
                bind.exSevenDayText.text = dayFormatter.format(day.date)
                bind.exSevenMonthText.text = monthFormatter.format(day.date)

                auth = Firebase.auth
                val user = auth.currentUser
                db.collection("Tareas").whereEqualTo("duenio",user?.uid.toString()).whereEqualTo("hecha",false).get().addOnSuccessListener {snapshot ->
                    //Log.d("LuchoUri","estoy antes del if")

                    var tareasCalendario: MutableList<Tareas> = ArrayList<Tareas>()

                    if (snapshot != null)
                    {
                        //Log.d("LuchoUri","estoy antes del for")
                        for (tareas in snapshot) {
                            tareasCalendario.add(tareas.toObject<Tareas>())
                        }
                    }

                    for (posicion in tareasCalendario)
                    {
                        if(day.date.toString()==posicion.fecha){
                            bind.exSevenDayDot.visibility = View.VISIBLE
                        }
                    }

                }


                bind.exSevenDateText.setTextColor(view.context.getColorCompat(if (day.date == selectedDate) R.color.blanco else R.color.gris))
                bind.exSevenMonthText.setTextColor(view.context.getColorCompat(if (day.date == selectedDate) R.color.blanco else R.color.gris))
                bind.exSevenDayText.setTextColor(view.context.getColorCompat(if (day.date == selectedDate) R.color.blanco else R.color.gris))
                bind.exSevenSelectedView.isVisible = day.date == selectedDate

            }
        }

        binding.exSevenCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) = container.bind(day)
        }

        val currentMonth = YearMonth.now()
        binding.exSevenCalendar.setup(currentMonth, currentMonth.plusMonths(3), DayOfWeek.values().random())
        binding.exSevenCalendar.scrollToDate(LocalDate.now())
    }

    override fun onStart() {
        super.onStart()

        btnAgregarTareaDiario.setOnClickListener {
            val aAgregarTarea: NavDirections =
                CalendarioDiarioDirections.actionCalendarioDiarioToAgregarTarea()
            v.findNavController().navigate(aAgregarTarea)
        }
        /*if (prueba.isChecked){
            tareasCalendario[position].hecha = true
        }*/
    }

    fun onCheckBoxClick ( position : Int, tareasCalendario: MutableList<Tareas> = ArrayList<Tareas>()) {
        val db = Firebase.firestore
        if(tareasCalendario[position].hecha==true){
            tareasCalendario[position].hecha=false
            db.collection("Tareas").document(tareasCalendario[position].uid).set(tareasCalendario[position])
            val docRef = db.collection("Usuarios").document(tareasCalendario[position].duenio)
            docRef.get().addOnSuccessListener { documentSnapshot ->
                val usuario = documentSnapshot.toObject(Usuarios::class.java)
                usuario?.dinero= usuario?.dinero!! - tareasCalendario[position].ganancia
                db.collection("Usuarios").document(tareasCalendario[position].duenio).set(usuario)
            }

        }else{
            tareasCalendario[position].hecha=true
            db.collection("Tareas").document(tareasCalendario[position].uid).set(tareasCalendario[position])
            val docRef = db.collection("Usuarios").document(tareasCalendario[position].duenio)
            docRef.get().addOnSuccessListener { documentSnapshot ->
                val usuario = documentSnapshot.toObject(Usuarios::class.java)
                usuario?.dinero= usuario?.dinero!! + tareasCalendario[position].ganancia
                db.collection("Usuarios").document(tareasCalendario[position].duenio).set(usuario)
                Toast.makeText(context,"Has ganado " + tareasCalendario[position].ganancia + " monedas",Toast.LENGTH_LONG).show()
            }

        }
    }

    fun onItemClick(position: Int, tareasCalendario: MutableList<Tareas>) {

        val aModificarTarea: NavDirections =
            CalendarioDiarioDirections.actionCalendarioDiarioToModificarTarea(tareasCalendario[position])
        v.findNavController().navigate(aModificarTarea)

    }


    fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

}

