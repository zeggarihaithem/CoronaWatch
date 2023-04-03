package solutus.coronawatch.ui.firstActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.coronawatch_mobile.R
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.activity_first.*
import solutus.coronawatch.ui.mainActivity.MainActivity


class FirstActivity : AppCompatActivity() {
    companion object {
        private const val NUM_PAGES = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        //set pager adapter
        val pagerAdapter = ScreenSlidePagerAdapter(supportFragmentManager)
        first_activity_view_pager.adapter = pagerAdapter

    }

    override fun onBackPressed() {
        if (first_activity_view_pager.currentItem == 0) {
            super.onBackPressed()
        } else {
            first_activity_view_pager.currentItem = first_activity_view_pager.currentItem + 1
        }
    }


    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) :
        FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = NUM_PAGES

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> AdviceFragment()
                else -> StartFragment()
            }
        }
    }



}
