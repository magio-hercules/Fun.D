package com.github.rubensousa.viewpagercards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment


class CardFragment4 : Fragment() {

    var cardView: CardView? = null
        private set

    var cnt = 0;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.fragment_adapter4, container, false)
        cardView = view.findViewById<View>(R.id.cardView) as CardView
        cardView!!.maxCardElevation = cardView!!.cardElevation * CardAdapter.MAX_ELEVATION_FACTOR
        return view
    }
}
