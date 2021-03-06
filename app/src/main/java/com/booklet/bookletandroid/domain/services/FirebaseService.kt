package com.booklet.bookletandroid.domain.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.booklet.bookletandroid.R
import com.booklet.bookletandroid.domain.Preferences
import com.booklet.bookletandroid.domain.model.Mark
import com.booklet.bookletandroid.presentation.activities.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.greenrobot.eventbus.EventBus

class FirebaseService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val prefs = Preferences.getInstance(this)
        when (message.data["type"]) {
            TYPE_NEW_MARK -> {
                EventBus.getDefault().post(Mark(message.data["grade"]?.toInt() ?: return,
                        message.data["subject"] ?: return,
                        message.data["date"] ?: return))
                if (prefs.notificationNewMark && prefs.notificationsSubscription)
                    sendMarkNotification(message.data)
            }
            TYPE_EVENT -> {
                if (prefs.notificationNews)
                    sendEventNotification(message.data)
            }
        }
    }

    private fun sendNotification(title: String, body: String, expand_body: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT)

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mark_outline)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(expand_body))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun sendMarkNotification(data: Map<String, String>) {
        sendNotification(
                "Новая оценка!",
                "Оценка ${data["grade"]} по предмету ${data["subject"]}",
                "Оценка: ${data["grade"]}\nПредмет: ${data["subject"]}"
        )
    }

    private fun sendEventNotification(data: Map<String, String>) {
        sendNotification(
                "Новое событие в классе!",
                data["content"] ?: "",
                data["content"] ?: ""
        )
    }

    companion object {
        private const val TYPE_NEW_MARK = "new_mark"
        private const val TYPE_EVENT = "event"
    }
}