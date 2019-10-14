package com.fundroid.dframefragment


import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fundroid.dframefragment.adapter.FeedAdapter
import com.fundroid.dframefragment.data.DummyItem
import kotlinx.android.synthetic.main.fragment_feed.*

/**
 * A simple [Fragment] subclass.
 *
 */

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {//바인딩 레이아웃
        //레이아웃에 필요한 정보 선언을 하는 곳
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onResume() {
        super.onResume()


        val dataSetsample = mutableListOf<DummyItem>()
        for(i in 1..10){
            dataSetsample.add(DummyItem("dummy itme $i",R.drawable.sample))
        }

        rv_feed.apply {
            setHasFixedSize(true)//뷰의 크기가 동일하냐 를 체크해준다. 사용되는이유는 원래 한뷰마다 사이즈를 연산하는데 만약 이함수를 true를 했을경우 연산을 하지 않는다
            layoutManager = LinearLayoutManager(context)
            adapter = FeedAdapter(dataSetsample)

        }

    }
}
