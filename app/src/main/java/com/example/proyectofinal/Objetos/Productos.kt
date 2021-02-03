package com.example.proyectofinal.Objetos

import android.os.Parcel
import android.os.Parcelable

class Productos (nombre : String, imagenUrl : String, precio : Int, tipo : String) : Parcelable{

    var nombre : String = ""
    var imagenUrl : String = ""
    var precio : Int = 0
    var tipo : String =""

    constructor():this("","",0,"")

    init {
        this.nombre=nombre
        this.imagenUrl=imagenUrl
        this.precio=precio
        this.tipo=tipo
    }

    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readInt(),
        source.readString()!!
    ) {
        nombre = source.readString()!!
        imagenUrl = source.readString()!!
        precio = source.readInt()
        tipo = source.readString()!!
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(imagenUrl)
        parcel.writeInt(precio)
        parcel.writeString(tipo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Productos> {
        override fun createFromParcel(parcel: Parcel): Productos {
            return Productos(parcel)
        }

        override fun newArray(size: Int): Array<Productos?> {
            return arrayOfNulls(size)
        }
    }

}