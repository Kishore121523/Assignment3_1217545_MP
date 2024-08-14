package com.example.wish_it

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class WishFragment : Fragment() {

    private var latestWish: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wish, container, false)

        val tvFragmentMessage = view.findViewById<TextView>(R.id.motivationalMessage)

        // Set the most recent wish to the TextView
        latestWish?.let {
            tvFragmentMessage.text = "Recent Wish: $it"
        }

        return view
    }

    fun updateWish(wish: String) {
        latestWish = wish
        view?.findViewById<TextView>(R.id.motivationalMessage)?.text = "Recent Wish: $wish"
    }
}
