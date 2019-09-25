package com.github.rubensousa.viewpagercards

import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.github.rubensousa.viewpagercards.DataBase.Code.CodeDTO
import kotlinx.android.synthetic.main.fragment_adapter.*
import java.util.*

class CardFragmentPagerAdapter(fm: FragmentManager, private val mBaseElevation: Float) : FragmentStatePagerAdapter(fm), CardAdapter {

    // < Code >
//    var codesList: List<CodeDTO>? = null
//
//    internal fun setData(codes: List<CodeDTO>){
//        codesList = codes
//
//        for (i in 1..6) {
//            addCardFragment(CardFragment())
//        }
//    }
    // ------------

    override fun baseElevation(): Float {
        return mBaseElevation
    }

    override fun count(): Int {
        return mFragments.size
    }

    private val mFragments: MutableList<CardFragment>

    init {
        mFragments = ArrayList()

        for (i in 1..4) {
            addCardFragment(CardFragment())
        }
    }

    override fun getBaseElevation(): Float {
        return mBaseElevation
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getCardViewAt(position: Int): CardView? {
        return mFragments[position].cardView
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position)
        mFragments[position] = fragment as CardFragment
        return fragment
    }

    fun addCardFragment(fragment: CardFragment) {
        mFragments.add(fragment)
    }

}
