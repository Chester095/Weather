package com.geekbrains.weather.view

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.geekbrains.weather.model.ContactsDAO
import com.geekbrains.weather.model.ContactsDB
import com.geekbrains.weather.model.HistoryDAO
import com.geekbrains.weather.model.HistoryDB

// при создании Application будем создавать нашу БД
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // инициализация appInstance
        appInstance = this

    }

    // будем хранить БД в статическом
    companion object {
        private const val TAG = "App"
        private var appInstance: App? = null

        // сама БД
        private var historyDB: HistoryDB? = null
//        private var contactsDB: ContactsDB? = null

        // название нашей БД
        private const val DB_NAME = "History.db"

        // инициализация БД истории запросов
        fun getHistoryDao(): HistoryDAO {
            if (historyDB == null) {
                Log.d(TAG, "History.db  db != null")
                synchronized(HistoryDB::class.java) {
                    if (historyDB == null) {
                        appInstance?.let { app ->
                            historyDB = Room.databaseBuilder(
                                app.applicationContext,
                                HistoryDB::class.java,
                                DB_NAME
                            ).build()
                        } ?: throw Exception("Что-то пошло не так")
                    }
                }
            }
            return historyDB!!.historyDAO()
        }

/*        // инициализация БД телефонов
        fun getContactsDao(): ContactsDAO {
            if (contactsDB == null) {
                Log.d(TAG, "History.db  db != null")
                synchronized(HistoryDB::class.java) {
                    if (contactsDB == null) {
                        appInstance?.let { app ->
                            contactsDB = Room.databaseBuilder(
                                app.applicationContext,
                                ContactsDB::class.java,
                                DB_NAME
                            ).build()
                        } ?: throw Exception("Что-то пошло не так")
                    }
                }
            }
            return contactsDB!!.contactsDAO()
        }*/

    }


}