package com.github.rubensousa.viewpagercards

import androidx.cardview.widget.CardView

interface CardAdapter {

    fun baseElevation(): Float

    fun count(): Int

    fun getCardViewAt(position: Int): CardView?

    companion object {

        val MAX_ELEVATION_FACTOR = 8
    }

    fun getBaseElevation(): Float

    fun getCount(): Int

}
