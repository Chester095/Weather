package com.geekbrains.weather.model.contacts

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.concurrent.locks.Condition

// относится к Model так как отвечает за данные (как, что, где и в каком формате)
@Parcelize
data class Contact(
    val id: String = "",
    val name: String = "",
    val number: String = "",
) : Parcelable

