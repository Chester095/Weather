package com.geekbrains.weather.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.geekbrains.weather.R


class ContactsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        supportActionBar!!.title = "Контакты"
        checkPermission()
    }

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            // описываем сценарии
            when {
                // разрешения выданы
                result -> getContacts()
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) -> {
                    // диалог запроса разрешения
                    AlertDialog.Builder(this).setTitle("Дай доступ")
                        .setMessage("очень нада")
                        .setPositiveButton("Дать доступ") { _, _ -> checkPermission() }
                        .setNegativeButton("Не давать") { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
                else -> Toast.makeText(this, "noooo...", Toast.LENGTH_LONG).show()
            }
        }

    private fun checkPermission() {
        permissionResult.launch(Manifest.permission.READ_CONTACTS)
    }

    // стучимся в системный контент провайдер
    @SuppressLint("Range")
    private fun getContacts() {
        //объект через который будем общаться с контент провайдером
        val contentResolver: ContentResolver = contentResolver
        val contactsList = findViewById<TextView>(R.id.contacts_list).apply { text = "" }
        // через него получаем курсор. Курсор нужен для получения данных по объектно и не всех сразу.
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.Contacts.LOOKUP_KEY
        )
        val cursor: Cursor? = contentResolver.query(
            // формируем путь для запроса
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC" // пробел обязательно. ASC - по возрастанию
        )


        cursor?.let { cursor ->
             val columnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

            if (columnIndex >= 0) {
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val phoneNumber: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val name = cursor.getString(columnIndex)
                        contactsList.text = "${contactsList.text}$name $phoneNumber\n"
                    }
                }
            }
            cursor?.close()
        }


    }
}