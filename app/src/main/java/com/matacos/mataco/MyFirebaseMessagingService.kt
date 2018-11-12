package com.matacos.mataco

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.matacos.mataco.apiController.APIController
import com.matacos.mataco.apiController.ServiceVolley
import org.json.JSONObject


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG: String = MyFirebaseMessagingService::class.java.simpleName

    override fun onNewToken(newToken: String?) {
        Log.d(TAG, "onNewToken")
        super.onNewToken(newToken)
        Log.d(TAG, "New: Refreshed firebase token: " + newToken!!)

        val firebasePreferences = getSharedPreferences("my_firebase_preferences", Context.MODE_PRIVATE)
        val editFirebasePreferences = firebasePreferences.edit()
        editFirebasePreferences.putString("firebase_token", newToken).apply()

        val preferences: SharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val username: String = preferences.getString("username", "")
        if (username != "") {
            sendRegistrationToServer(username, newToken)
        }

    }

    private fun sendRegistrationToServer(username: String, firebaseToken: String) {
        Log.d(TAG, "sendRegistrationToServer")
        val service = ServiceVolley()
        val apiController = APIController(service)

        val path = "api/firebase"
        val params = JSONObject()
        params.put("username", username)
        params.put("firebase_token", firebaseToken)
        Log.d(TAG, "Params: " + params.toString())
        apiController.post(path, params) { response ->
            Log.d(TAG, response.toString())
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "onMessageReceived")
        Log.d(TAG, "From: " + remoteMessage.from!!)
        Log.d(TAG, "Message: " + remoteMessage.toString())

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            showNotification(remoteMessage.data!!["title"]!!, remoteMessage.data!!["body"]!!, remoteMessage.data!!["click_action"]!!, remoteMessage.data!!["channel_id"]!!)
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
        }
    }

    private fun createNotificationChannel(channelId: String) {
        Log.d(TAG, "createNotificationChannel")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var name = ""
            var descriptionText = ""
            if (channelId == "exams") {
                name = getString(R.string.exams_channel)
                descriptionText = getString(R.string.exams_channel_desc)
            } else if (channelId == "courses") {
                name = getString(R.string.courses_channel)
                descriptionText = getString(R.string.courses_channel_desc)
            }
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    private fun showNotification(title: String, body: String, clickAction: String, channelId: String) {
        Log.d(TAG, "showNotification")
        Log.d(TAG, "Channel id: $channelId")
        createNotificationChannel(channelId)

        Log.d(TAG, "Click action: $clickAction")
        val intent = Intent(clickAction)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)


        val mBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(body))

        val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, mBuilder.build())
    }
}
