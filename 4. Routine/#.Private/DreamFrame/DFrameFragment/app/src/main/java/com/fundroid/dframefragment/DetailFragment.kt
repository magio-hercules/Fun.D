package com.fundroid.dframefragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fundroid.dframefragment.adapter.DetailAdapter
import com.fundroid.dframefragment.adapter.FeedAdapter
import com.fundroid.dframefragment.data.DummyItem
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_feed.*
import org.w3c.dom.Text


/**
 * A simple [Fragment] subclass.
 *
 */
class DetailFragment : Fragment() {

    var str_Title:String = "사업가"
    var str_Contents:String =
        "언제 스타가 될지, 성공할지 나도 잘 몰라\n" +
                "이 바닥은 아이돌이 되려다\n" +
                "아이~ 돌아버리겠네하면서\n" +
                "중간에 포기하는 친구들이 대부분이야.\n" +
                "도전해보고 내 길이 아니다 싶으면 빨리\n" +
                "새로운 길을 찾는게 현명한 걸지도 몰라."


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }
    override fun onResume() {
        super.onResume()

        textViewManager()


        val dataSetsample = mutableListOf<DummyItem>()
        for(i in 1..5) {
            dataSetsample.add(DummyItem("dummy itme $i", R.drawable.sample))
        }
    }

    fun textViewManager()
    {
        tv_Title.text = getString(R.string.title_businessman)
        tv_Contents.text = getString(R.string.contents_businessman)
    }
}
