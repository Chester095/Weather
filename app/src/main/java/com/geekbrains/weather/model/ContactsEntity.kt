package com.geekbrains.weather.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity // анотация чтобы указать что это таблица
data class ContactsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val number: String
)