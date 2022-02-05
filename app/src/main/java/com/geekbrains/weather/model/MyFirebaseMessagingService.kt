package com.geekbrains.weather.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.geekbrains.weather.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val CHANNEL_ID = "Default"
const val NOTIFICATION_ID = 40

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "!!! MyFirebaseMessagingService"


    // сюда будут приходить сообщения
    override fun onMessageReceived(RemoteMessage: RemoteMessage) {
        Log.d(TAG, " onMessageReceived  RemoteMessage = $RemoteMessage")
        super.onMessageReceived(RemoteMessage)

        // тут модифицируется внешне ПУШ
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.common_google_signin_btn_icon_light_focused)
            .setContentTitle("Заголовок сообщения")
            .setStyle(NotificationCompat.BigTextStyle().bigText("BIG TEXT"))
            //   интент который откроется при нажатии         .setContentIntent()
            //   кнопку можно привязать         .addAction(NotificationCompat.Action.Builder(this,))
            .setPriority(NotificationCompat.PRIORITY_MAX)


        // отправитель нотификации
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // создаём канал
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "Name channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply { description = "Описание" }
            )
            notificationBuilder.setChannelId(CHANNEL_ID)
        }

        // передаём
        notificationManager.notify(40, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, " onNewToken  token = $token")
        super.onNewToken(token)
    }

}
