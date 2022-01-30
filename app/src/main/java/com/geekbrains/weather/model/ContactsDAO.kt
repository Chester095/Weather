package com.geekbrains.weather.model

import androidx.room.*

// методы для работы с нашей Entity (сущностью)
@Dao
interface ContactsDAO {

    // получить все данные из БД
    // "ORDER BY timestamp DESC" - отсортировать по полю timestamp в обратном порядке
    @Query("SELECT * FROM ContactsEntity ORDER BY name DESC")
    fun all(): List<ContactsEntity>

    // данные по имени
    @Query("SELECT * FROM ContactsEntity WHERE name LIKE :name")
    fun getContactsByName(name: String): List<ContactsEntity>

    // добавить данные
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: ContactsEntity)

    @Update
    fun update(entity: ContactsEntity)

    @Delete
    fun delete(entity: ContactsEntity)
}
