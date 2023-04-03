package solutus.coronawatch.service

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.MediaController
import androidx.fragment.app.FragmentActivity
import com.example.coronawatch_mobile.R
import solutus.coronawatch.ui.mainActivity.home.videos.watchVideo.WatchVideoFragment


class FullScreenMediaController(context: Context?) :
    MediaController(context) {
    private var fullScreen: ImageButton? = null
    private var isFullScreen: String? = null

    override fun setAnchorView(view: View?) {
        super.setAnchorView(view)
        //image button for full screen to be added to media controller
        fullScreen = ImageButton(super.getContext())
        val params = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.END
        params.rightMargin = 80
        addView(fullScreen, params)
        //fullscreen indicator from intent
        val bundle = Bundle()
        isFullScreen = bundle.getString("fullScreenInd")
        if ("y" == isFullScreen) {
            fullScreen!!.setImageResource(R.drawable.ic_fullscreen_exit)
        } else {
            fullScreen!!.setImageResource(R.drawable.ic_fullscreen)
        }
        //add listener to image button to handle full screen and exit full screen events
        fullScreen!!.setOnClickListener {
            val fragment = WatchVideoFragment() as androidx.fragment.app.Fragment
            val args = Bundle()
            if ("y" == isFullScreen) {
                args.putString("fullScreenInd", "")
            } else {
                args.putString("fullScreenInd", "y")
            }
            fragment.arguments = args
            val fragmentTransaction =
                (context as FragmentActivity).supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.video_fragment, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
    }


}