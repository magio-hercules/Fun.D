package com.github.rubensousa.viewpagercards

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


class CardFragment : Fragment() {
    //프래그먼트 초기화 파라미터, 아이템 넘버
    companion object {
         const val ARG_CARDVIEWFRAGMENT1 = "cardViewfragment1"
         const val ARG_DETAILFRAGMENT2 = "detailfragment2"
    }
    //카드뷰 0번 정보
    private var mCardViewParam: String? = null
    private var mDetailParam: String? = null


    var cardView: CardView? = null
        private set

    var cnt = 0;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.fragment_adapter, container, false)
        cardView = view.findViewById<View>(R.id.cardView) as CardView
        cardView!!.maxCardElevation = cardView!!.cardElevation * CardAdapter.MAX_ELEVATION_FACTOR
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //버튼 클릭
        cardView!!.findViewById<View>(R.id.btn_landing).setOnClickListener {
            Log.d("landingpage", "랜딩페이지버")
            //상세페이지 전환

        }
    }
    //프래그먼트에 필요한 자원
    fun changeDetailFrag(): DetailFragment {
        val detailFrag:DetailFragment = DetailFragment()
        var args:Bundle = Bundle()
        val fragment:Fragment
        args.putString(ARG_CARDVIEWFRAGMENT1, mCardViewParam)
        args.putString(ARG_DETAILFRAGMENT2, mDetailParam)
        return detailFrag
    }
}
