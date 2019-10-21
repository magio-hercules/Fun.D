package com.github.rubensousa.viewpagercards

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


class CardFragment3 : Fragment() {

    var cardView: CardView? = null
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.fragment_adapter3, container, false)
        cardView = view.findViewById<View>(R.id.cardView) as CardView
        cardView!!.maxCardElevation = cardView!!.cardElevation * CardAdapter.MAX_ELEVATION_FACTOR

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //버튼 클릭
        cardView!!.findViewById<View>(R.id.btn_landing).setOnClickListener {
            Log.d("landingpage","랜딩페이지버")
            //상세페이지 전환

        }
    }
}
