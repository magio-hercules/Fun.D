package com.github.rubensousa.viewpagercards

import android.content.Context
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity() {

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
    }

    companion object {

        fun dpToPixels(dp: Int, context: Context): Float {
            return dp * context.resources.displayMetrics.density
        }
    }
}

