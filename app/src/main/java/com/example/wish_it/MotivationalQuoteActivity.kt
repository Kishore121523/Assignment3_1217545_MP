package com.example.wish_it

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MotivationalQuoteActivity : AppCompatActivity() {

    private val quotes = listOf(
        "Believe you can and you're halfway there.",
        "The only way to do great work is to love what you do.",
        "The best time to plant a tree was 20 years ago. The second best time is now.",
        "Don't watch the clock; do what it does. Keep going.",
        "Your limitation—it's only your imagination.",
        "Push yourself, because no one else is going to do it for you.",
        "Success doesn’t just find you. You have to go out and get it.",
        "The harder you work for something, the greater you’ll feel when you achieve it.",
        "Don’t stop when you’re tired. Stop when you’re done.",
        "Dream it. Wish it. Do it.",
        "Little things make big days.",
        "It’s going to be hard, but hard does not mean impossible.",
        "Don’t wait for opportunity. Create it.",
        "Sometimes later becomes never. Do it now.",
        "Great things never come from comfort zones.",
        "Wake up with determination. Go to bed with satisfaction.",
        "Success is not for the lazy.",
        "The key to success is to focus on goals, not obstacles.",
        "The harder the battle, the sweeter the victory.",
        "Dream big and dare to fail."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motivational_quote)

        val tvMotivationalQuote = findViewById<TextView>(R.id.randomQuote)

        val randomQuote = quotes[Random.nextInt(quotes.size)]
        tvMotivationalQuote.text = randomQuote
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
}
