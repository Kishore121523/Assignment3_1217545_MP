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
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SecondActivity : AppCompatActivity() {

    private lateinit var wishAdapter: WishAdapter
    private val wishes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val wishesContainer = findViewById<RecyclerView>(R.id.wishesContainer)

        // Load existing wishes from SharedPreferences
        loadWishes()
        setupRecyclerView(wishesContainer)

        val newWish = intent.getStringExtra("WISH") // getting the string passed from main activity
        newWish?.let {
            wishes.add(0, it) // Add the new wish at the top of the list, so that it acts as a stack rather than queue
            wishAdapter.notifyItemInserted(0)
            wishesContainer.scrollToPosition(0)
            saveWishes() // Save the updated list to SharedPreferences
            showNotification(it) // show notification when each wish is added
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, WishFragment().apply {
                if (newWish != null) {
                    updateWish(newWish)
                }
            })
            .commit()
    }

    private fun setupRecyclerView(rvWishes: RecyclerView) {
        wishAdapter = WishAdapter(wishes)
        rvWishes.adapter = wishAdapter
        rvWishes.layoutManager = LinearLayoutManager(this)
    }


    // Convert to json and store it in the shared preference so that it is updated with latest values
    private fun saveWishes() {
        val sharedPreferences = getSharedPreferences("Wishes", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(wishes)
        editor.putString("wish_list", json)
        editor.apply()
    }


    // The wishes that are already entered by users are loaded by this function and the list is stored as JSON format using Gson library
    private fun loadWishes() {
        val sharedPreferences = getSharedPreferences("Wishes", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("wish_list", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<String>>() {}.type
            val savedWishes: MutableList<String> = gson.fromJson(json, type)
            wishes.addAll(savedWishes)
        }
    }

    // Functions to show notification and ask for permission if the permission is denied by the user
    private fun showNotification(wish: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
                return
            }
        }

        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, "WISH_CHANNEL")
            .setSmallIcon(R.drawable.ic_wish)
            .setContentTitle("New Wish Added")
            .setContentText(wish)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Wish Channel"
            val descriptionText = "Channel for wish notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("WISH_CHANNEL", name, importance).apply {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                showNotification(intent.getStringExtra("WISH") ?: "")
            } else {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

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

    companion object {
        private const val PERMISSION_REQUEST_CODE = 101
    }
}
