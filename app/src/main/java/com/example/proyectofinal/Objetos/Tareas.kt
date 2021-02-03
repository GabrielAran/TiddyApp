package com.example.proyectofinal.Objetos

import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable

class Tareas (nombreTarea : String, color : Int, notas : String, duracion : Int, vecesPorSemana : Int, importancia : Int, dificultad : Int, notifiaciones : Boolean, bloquearPantalla : Boolean, fechaLimite : String, ganancia : Int, duenio : String, hecha : Boolean, fecha : String, hora : Int, uid : String, diaFecha : Int) : Parcelable {

    var nombreTarea : String = ""
    var color : Int = 0
    var notas : String = ""
    var duracion : Int = 0
    var vecesPorSemana : Int = 0
    var importancia : Int = 0
    var dificultad : Int = 0
    var notifiaciones : Boolean = false
    var bloquearPantalla : Boolean = false
    var fechaLimite : String = ""
    var ganancia : Int = 0
    var duenio : String = ""
    var hecha : Boolean = false
    var fecha : String = ""
    var hora : Int = 0
    var uid : String = ""
    var diaFecha : Int = 0

    constructor():this("",0,"",0,0,0,0,false,false,"",0,"",false,"",0,"",0)

    init {
        this.nombreTarea=nombreTarea
        this.color=color
        this.notas=notas
        this.duracion=duracion
        this.vecesPorSemana=vecesPorSemana
        this.importancia=importancia
        this.dificultad=dificultad
        this.notifiaciones=notifiaciones
        this.bloquearPantalla=bloquearPantalla
        this.fechaLimite=fechaLimite
        this.ganancia=ganancia
        this.duenio=duenio
        this.hecha=hecha
        this.fecha=fecha
        this.hora=hora
        this.uid=uid
        this.diaFecha=diaFecha
    }

    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readInt(),
        source.readString()!!,
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readInt(),
        source.readBoolean(),
        source.readBoolean(),
        source.readString()!!,
        source.readInt(),
        source.readString()!!,
        source.readBoolean(),
        source.readString()!!,
        source.readInt(),
        source.readString()!!,
        source.readInt()
        ) {
        nombreTarea = source.readString()!!
        color = source.readInt()
        notas = source.readString()!!
        duracion = source.readInt()
        vecesPorSemana = source.readInt()
        importancia = source.readInt()
        dificultad = source.readInt()
        notifiaciones = source.readByte() != 0.toByte()
        bloquearPantalla = source.readByte() != 0.toByte()
        fechaLimite = source.readString()!!
        ganancia = source.readInt()
        duenio = source.readString()!!
        hecha = source.readByte() != 0.toByte()
        fecha = source.readString()!!
        hora = source.readInt()
        uid = source.readString()!!
        diaFecha = source.readInt()
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<Tareas> {
        override fun createFromParcel(parcel: Parcel): Tareas {
            return Tareas(parcel)
        }

        override fun newArray(size: Int): Array<Tareas?> {
            return arrayOfNulls(size)
        }
    }

}