package com.github.rubensousa.viewpagercards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment


class CardFragment : Fragment() {

    var cardView: CardView? = null
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_adapter, container, false)
        cardView = view.findViewById<View>(R.id.cardView) as CardView
        cardView!!.maxCardElevation = cardView!!.cardElevation * CardAdapter.MAX_ELEVATION_FACTOR
        return view
    }
}
