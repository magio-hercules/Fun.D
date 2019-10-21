package com.github.rubensousa.viewpagercards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment: Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
    override fun onResume() {
        super.onResume()

        textViewManager()
    }

    fun textViewManager()
    {
        tv_Title.text = getString(R.string.title_businessman)
        tv_Contents.text = getString(R.string.contents_businessman)
    }

}