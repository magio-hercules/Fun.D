package com.fundroid.routinesc

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lsc", "MainFragment onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("lsc", "MainFragment onViewCreated")
        val dummyRoutines = listOf(
            Routine("test01", "resource01"),
            Routine("test02", "resource02"),
            Routine("test03", "resource03"),
            Routine("test04", "resource04")
        )

        val routineAdapter = RoutineAdapter(dummyRoutines)

        routines.apply { adapter = routineAdapter }
    }
}
