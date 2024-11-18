package com.faiz.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.faiz.notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val channelId = "TEST_NOTIF"
    private val notifId = 90

    companion object{
        lateinit var binding: ActivityMainBinding
        var countLike = 0
        var countDislike = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        binding.btnNotif.setOnClickListener {
            val flag = PendingIntent.FLAG_IMMUTABLE
            val intent = Intent(this,  MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, flag)
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Greetings, the Tarnished")
                .setContentText("Ahh, noble one, Your knowledge has lifted the fog of ignorance that clouded my path.")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(0, "Baca Selengkapnya", pendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notifChannel = NotificationChannel(
                    channelId, "The Tarnished One", NotificationManager.IMPORTANCE_DEFAULT
                )
                with(notifManager) {
                    createNotificationChannel(notifChannel)
                    notify(notifId, builder.build())
                }
            } else {
                notifManager.notify(notifId, builder.build())
            }
        }

        binding.btnUpdate.setOnClickListener {
            val notifImage = BitmapFactory.decodeResource(resources, R.drawable.maidenless_2)
            val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.maidenless)

            val builder = NotificationCompat.Builder(this, channelId)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Hail, Tarnished. A notification awaits thee.")
                .setContentText("L + MAIDENLESS + NO RUNES + TARNISHED")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(notifImage))
                .addAction(1, "Like", triggerNotif("Like"))
                .addAction(2, "Dislike", triggerNotif("Dislike"))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notifChannel = NotificationChannel(
                    channelId, "Hail, Tarnished. A notification awaits thee.", NotificationManager.IMPORTANCE_DEFAULT
                )
                with(notifManager) {
                    createNotificationChannel(notifChannel)
                    notify(notifId, builder.build())
                }
            } else {
                notifManager.notify(notifId, builder.build())
            }
        }
    }

    private fun triggerNotif(key: String): PendingIntent {
        val flag = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val intent = Intent(this, NotifReceiver::class.java).putExtra("TEST", key)
        return PendingIntent.getBroadcast(this, if (key == "Like") 1 else 2, intent, flag)
    }
}