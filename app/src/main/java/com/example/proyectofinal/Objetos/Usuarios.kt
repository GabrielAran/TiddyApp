package com.example.proyectofinal.Objetos

import android.os.Parcel
import android.os.Parcelable

class Usuarios (uid : String, mail : String, nombre : String, dinero : Int): Parcelable{

    var uid : String
    var mail : String
    var nombre : String
    var dinero : Int
    
    constructor():this("","","",0)
        
    init {
        this.uid = uid
        this.mail = mail
        this.nombre = nombre
        this.dinero = dinero
    }
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.readInt()
    ){
        uid = source.readString()!!
        mail = source.readString()!!
        nombre = source.readString()!!
        dinero = source.readInt()
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

    companion object CREATOR : Parcelable.Creator<Usuarios> {
        override fun createFromParcel(parcel: Parcel): Usuarios {
            return Usuarios(parcel)
        }

        override fun newArray(size: Int): Array<Usuarios?> {
            return arrayOfNulls(size)
        }
    }
    
}