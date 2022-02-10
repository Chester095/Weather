package com.geekbrains.weather.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.geekbrains.weather.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        const val TAG = "!!! MyFirebaseMessagingService"
        const val CHANNEL_ID = "Default"
        const val NOTIFICATION_ID = 40
    }

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
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, " onNewToken  token = $token")
        super.onNewToken(token)
    }

}
