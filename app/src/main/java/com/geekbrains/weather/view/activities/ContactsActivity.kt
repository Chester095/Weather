package com.geekbrains.weather.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.weather.R
import com.geekbrains.weather.model.Contact
import com.geekbrains.weather.model.ContactsAdapter


class ContactsActivity : AppCompatActivity() {
    companion object {
        private var contact = mutableListOf<Contact>()
        const val TAG = "!!! ContactsActivity "
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        supportActionBar!!.title = "Контакты"
        checkPermission()
    }

    @SuppressLint("NotifyDataSetChanged")
    private val permissionResultCallPhone = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { result ->
        // описываем сценарии
        when {
            // разрешения выданы
            result -> {
                getContacts()
                findViewById<RecyclerView>(R.id.contact_recycle_view).apply {
                    // выводим данные
                    adapter = ContactsAdapter(contact).also {
                        it.notifyDataSetChanged()

                        it.listener = ContactsAdapter.OnItemClick { contact ->
                            Bundle().apply { putParcelable("CONTACT_EXTRA", contact) }
                            startPhoneDial(contact.number)
                            !ActivityCompat.shouldShowRequestPermissionRationale(
                                this@ContactsActivity,
                                Manifest.permission.CALL_PHONE

                            )
                        }
                    }
                }
            }
        }
    }

    private val permissionResultReadContacts =
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

    private fun startPhoneDial(phoneNo: String) {
        Log.d(TAG, " startPhoneDial phoneNo = $phoneNo")
        val callIntent = Intent(Intent.ACTION_CALL)
        //callIntent.data = Uri.parse(phoneNo)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            callIntent.setPackage("com.android.phone")
        } else {
            callIntent.setPackage("com.android.server.telecom")
        }
        callIntent.data = Uri.parse("tel:$phoneNo")
        try {
            startActivity(callIntent)
        } catch (exp: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:${phoneNo}"));
            startActivity(intent);

        }
    }

    private fun checkPermission() {
        permissionResultReadContacts.launch(Manifest.permission.READ_CONTACTS)
        permissionResultCallPhone.launch(Manifest.permission.CALL_PHONE)
    }

    // стучимся в системный контент провайдер
    @SuppressLint("Range", "SetTextI18n")
    private fun getContacts() {
        //объект через который будем общаться с контент провайдером
        val contentResolver: ContentResolver = contentResolver
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
            ContactsContract.Contacts._ID + " ASC" // пробел обязательно. ASC - по возрастанию
        )


        cursor?.let { cursor ->
            val columnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            contact.clear()
            if (columnIndex >= 0) {
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val phoneNumber: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val id: String = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))
                        val name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY))
                        contact.add(Contact(id, name, phoneNumber))
                        Log.d(TAG, " columnIndex  id = $id  name = $name  number = $phoneNumber")
                    }
                }
            }
            cursor?.close()
        }


    }
}