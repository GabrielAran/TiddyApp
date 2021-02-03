package com.example.proyectofinal.Objetos

import android.os.Parcel
import android.os.Parcelable

class ProductoCantidades(duenio : String, nombreProducto : String, cantidad : Int) : Parcelable {

    var duenio: String = ""
    var nombreProducto : String = ""
    var cantidad : Int = 0

    constructor():this("","",0)

    init {
        this.duenio=duenio
        this.nombreProducto=nombreProducto
        this.cantidad=cantidad
    }

    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readInt()
    ) {
        duenio= source.readString()!!
        nombreProducto = source.readString()!!
        cantidad = source.readInt()
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
        parcel.writeString(duenio)
        parcel.writeString(nombreProducto)
        parcel.writeInt(cantidad)
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