package com.github.rubensousa.viewpagercards

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_adapter.*
import java.util.*


class CardFragmentPagerAdapter(fm: FragmentManager, private val mBaseElevation: Float) : FragmentStatePagerAdapter(fm), CardAdapter {

    override fun baseElevation(): Float {
        return mBaseElevation
    }

    override fun count(): Int {
        return mFragments.size
    }

    private val mFragments: MutableList<Fragment>

    init {
        mFragments = ArrayList()

        addCardFragment(CardFragment())
        addCardFragment(CardFragment2())
        addCardFragment(CardFragment3())
        addCardFragment(CardFragment4())
    }

    override fun getBaseElevation(): Float {
        return mBaseElevation
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getCardViewAt(position: Int): CardView? {
        return mFragments[position].activity?.cardView
    }

    override fun getItem(position: Int): Fragment {

        return mFragments[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position)
        mFragments[position] = fragment as Fragment
        return fragment
    }

    private fun addCardFragment(fragment: Fragment) {
        mFragments.add(fragment)
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

}
