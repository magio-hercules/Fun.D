package com.github.rubensousa.viewpagercards

import android.content.Context
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.github.rubensousa.viewpagercards.DataBase.Code.CodeDTO
import com.github.rubensousa.viewpagercards.DataBase.Code.CodeViewModel

class MainActivity : AppCompatActivity() {

    // < Code >
    // ------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mViewPager = findViewById<View>(R.id.viewPager) as ViewPager
        val mFragmentCardAdapter = CardFragmentPagerAdapter(supportFragmentManager,
                dpToPixels(2, this))
        val mFragmentCardShadowTransformer = ShadowTransformer(mViewPager, mFragmentCardAdapter)

        mViewPager.adapter = mFragmentCardAdapter
        mViewPager.setPageTransformer(false, mFragmentCardShadowTransformer)
        mFragmentCardShadowTransformer.enableScaling(true)

        // < Code >
//        codeViewModel = ViewModelProviders.of(this).get(CodeViewModel::class.java)
//        codeViewModel.allCodes.observe(this, Observer { words ->
//            // Update the cached copy of the words in the adapter.
//            words?.let { mFragmentCardAdapter.setData(it) }
//        })
        var codeViewModel: CodeViewModel = ViewModelProviders.of(this).get(CodeViewModel::class.java)
        var codeDTO: CodeDTO? = null

        codeDTO = CodeDTO("A", "A1", "만수르", "부러운새끼1", "", "", "")
        codeViewModel.insertCode(codeDTO)

        codeDTO = CodeDTO("A", "A2", "만수르아들1", "부러운새끼2", "", "", "")
        codeViewModel.insertCode(codeDTO)

        codeDTO = CodeDTO("A", "A3", "만수르아들2", "부러운새끼3", "", "", "")
        codeViewModel.insertCode(codeDTO)
        // ------------
    }

    companion object {

        fun dpToPixels(dp: Int, context: Context): Float {
            return dp * context.resources.displayMetrics.density
        }
    }
}

