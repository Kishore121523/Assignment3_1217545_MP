package com.example.wish_it

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val quotes = listOf(
        "The only limit to our realization of tomorrow is our doubts of today.",
        "The future belongs to those who believe in the beauty of their dreams.",
        "Start where you are. Use what you have. Do what you can.",
        "Act as if what you do makes a difference. It does.",
        "Success is not the key to happiness. Happiness is the key to success.",
        "Keep your face always toward the sunshine—and shadows will fall behind you.",
        "The way to get started is to quit talking and begin doing.",
        "You are never too old to set another goal or to dream a new dream.",
        "Believe you can and you're halfway there.",
        "Don't watch the clock; do what it does. Keep going.",
        "It's not whether you get knocked down, it's whether you get up.",
        "The harder the conflict, the greater the triumph.",
        "What lies behind us and what lies before us are tiny matters compared to what lies within us.",
        "The best way to predict your future is to create it.",
        "You miss 100% of the shots you don't take.",
        "Life is 10% what happens to us and 90% how we react to it.",
        "You have within you right now, everything you need to deal with whatever the world can throw at you.",
        "Hardships often prepare ordinary people for an extraordinary destiny.",
        "Don’t wait for your ship to come in, swim out to it.",
        "Success usually comes to those who are too busy to be looking for it."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Function to show a motivational quote notification when the app opens
        showMotivationalQuoteNotification()

        val etWish = findViewById<EditText>(R.id.wishInput)
        val btnAddWish = findViewById<Button>(R.id.btnAdd)
        val btnSeeWishes = findViewById<Button>(R.id.btnView)

        btnAddWish.setOnClickListener {
            val wish = etWish.text.toString()
            // Pass it only when user types something, used to avoid null exception in second activity
            if (wish.isNotEmpty()) {
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("WISH", wish) // send the wish to the SecondActivity
                startActivity(intent)
            }
        }

        btnSeeWishes.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }


    // Function which pushes a notification of a random quote from the given list
    private fun showMotivationalQuoteNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // If permission is not given then request for permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
                return
            }
        }

        createNotificationChannel()

        // Getting a random quote from the list
        val randomQuote = quotes[Random.nextInt(quotes.size)]

        val notification = NotificationCompat.Builder(this, "QUOTE_CHANNEL")
            .setSmallIcon(R.drawable.ic_wish)
            .setContentTitle("Daily Motivation")
            .setContentText(randomQuote)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(2, notification)
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Quote Channel"
            val descriptionText = "Channel for daily motivational quotes"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("QUOTE_CHANNEL", name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = android.graphics.Color.BLUE
                enableVibration(true)
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Function to display menu options in the action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_motivational_quote -> {
                val intent = Intent(this, MotivationalQuoteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_add_new_wish -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showMotivationalQuoteNotification()
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 101
    }
}
