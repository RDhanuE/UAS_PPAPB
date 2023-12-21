package com.example.calorify20.auth

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.calorify20.mainApp.MainAppActivity
import com.example.calorify20.mainApp.PrefManager
import com.example.calorify20.databinding.ActivityLoginRegisterBinding
import com.example.calorify20.notifications.AlarmReceiver
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class LogRegScreen : AppCompatActivity() {
    lateinit var mediator: TabLayoutMediator
    lateinit var viewPager2: ViewPager2
    private lateinit var binding: ActivityLoginRegisterBinding
    private lateinit var prefManager: PrefManager

    companion object {
        lateinit var auth: FirebaseAuth
        lateinit var firestore: FirebaseFirestore
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
        prefManager = PrefManager.getInstance(this@LogRegScreen)

        setAlarm(13,0)
        setAlarm(19, 0)
        setAlarm(0, 49)

        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn){
            Toast.makeText(this@LogRegScreen, "You already loged - in", Toast.LENGTH_SHORT).show()
            val email = prefManager.getEmail()
            val password = prefManager.getPassword()
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val intent = Intent(this@LogRegScreen, MainAppActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        with(binding) {
            viewPager2 = logregViewPager
            viewPager2.adapter = LogRegTabAdapter(this@LogRegScreen)
            mediator = TabLayoutMediator(logregTabLayout, viewPager2){
                tab, position ->
                when(position) {
                    0 -> tab.text = "Register"
                    1 -> tab.text = "Login"
                }
            }
            mediator.attach()
        }

        viewPager2.currentItem = intent.getIntExtra("TAB", 0)
    }

    fun switchFragment(position: Int) {
        viewPager2.currentItem = position
    }

    private fun setAlarm(hour: Int, minute: Int, isAlarmTriggered: Boolean = false) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.action = "NOTIFICATION"

        // Create a PendingIntent to open the app when the notification is pressed
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Schedule the alarm to trigger at the specified time
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val currentTime = Calendar.getInstance()

        // Check if the alarm should be triggered today or tomorrow
        if (currentTime.before(calendar)) {
            // Set up the notification only if it's triggered by an alarm
            if (isAlarmTriggered) {
                // Set up the notification
                val notification = createNotification("Calorify 2.0", "Don't forget to manage your calories", pendingIntent)

                // Display the notification immediately
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(1, notification)
            }

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    private fun createNotification(title: String, message: String, contentIntent: PendingIntent): Notification {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for devices running Android 8.0 and higher
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default_channel", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(this, "default_channel")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(contentIntent)  // Set the content intent
            .setAutoCancel(true)  // Automatically dismiss the notification when pressed
            .build()

        return notification
    }
}