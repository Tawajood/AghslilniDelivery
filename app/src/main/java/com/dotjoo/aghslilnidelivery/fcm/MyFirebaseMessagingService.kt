package com.dotjoo.aghslilnidelivery.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.data.PrefsHelper
import com.dotjoo.aghslilnidelivery.ui.activity.MainActivity
import com.dotjoo.aghslilnidelivery.util.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random
import javax.inject.Inject

private const val CHANNEL_ID = "my_channel"
@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
@Inject
lateinit var useCase:FcmUseCase
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d("islam", "onNewToken: $s")
        PrefsHelper.setFCMToken(s)
        sendRegistrationToServer(s)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendRealTimeBroadcast()
        showNotification(remoteMessage.data)
    }

    private fun sendRegistrationToServer(token: String?) {
        token?.let {   useCase.sendFcmTokenToServer(params =  FcmParam(it)) }
    }


    private fun showNotification(remoteMessage: Map<String, String>) {
        //val soundUri =
           // Uri.parse("android.resource://" + applicationContext.packageName + "/" + R.raw.notification)

         val intent = Intent(applicationContext, MainActivity::class.java)
        intent.action = Constants.OPEN_NOTIFICATION

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent =
            PendingIntent.getActivity(
                applicationContext, 100, intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pattern = longArrayOf(500, 500, 500, 500, 500, 500, 500, 500, 500)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val imageUrl = remoteMessage["imageUrl"]


        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(if (PrefsHelper.getLanguage() == Constants.AR) remoteMessage["title_ar"] else remoteMessage["title_en"])
            .setContentText(if (PrefsHelper.getLanguage() == Constants.AR) remoteMessage["body_ar"] else remoteMessage["body_en"])
            .setContentIntent(pendingIntent)
            //.setSound(soundUri)
            .setVibrate(pattern)

        var futureTarget = Glide.with(this)
            .asBitmap()
            .load(R.drawable.logo)
            .submit()
        if (imageUrl != null) {
            futureTarget = Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .submit()
        }
        val bitmap = futureTarget.get()
        notification.setLargeIcon(bitmap)
        Glide.with(this).clear(futureTarget)


        notificationManager.notify(Random().nextInt(), notification.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "Advisor Online Channel"
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        val channel = NotificationChannel(
            CHANNEL_ID, channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "A notification from Aghsilini"
            enableLights(true)
            lightColor = Color.GREEN
        }
        //channel.setSound(soundUri, audioAttributes)
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendRealTimeBroadcast( ) {
        val intent =
            Intent(MainActivity.MAIN_SCREEN_ACTION) //used to receive in intent filter when register the broadcast
         sendBroadcast(intent)

    }
}