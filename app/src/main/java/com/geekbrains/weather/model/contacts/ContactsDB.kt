package com.geekbrains.weather.model.contacts

import androidx.room.Database
import androidx.room.RoomDatabase


// указываем что это БД и какие в ней будут элементы, номер версии структуры и буличность данных
@Database(entities = [ContactsEntity::class], version = 1, exportSchema = false)
abstract class ContactsDB : RoomDatabase() {
    abstract fun contactsDAO(): ContactsDAO
}