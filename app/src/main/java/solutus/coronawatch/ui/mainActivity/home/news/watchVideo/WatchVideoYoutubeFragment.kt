package solutus.coronawatch.ui.mainActivity.home.news.watchVideo

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.example.coronawatch_mobile.R
import kotlinx.android.synthetic.main.watch_video_youtube_fragment.*
import solutus.coronawatch.service.Browser
import solutus.coronawatch.service.ChromeClient

class WatchVideoYoutubeFragment : Fragment() {

    companion object {
        fun newInstance() = WatchVideoYoutubeFragment()
    }

    private var url: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.watch_video_youtube_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //get data from list videos fragment using bundle
        val bundle = arguments
        url = bundle!!.getString("url")
        //set video
        setVideo()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setVideo() {
        video_view.webViewClient = object : Browser() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progress_bar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progress_bar.visibility = View.GONE
            }
        }
        video_view.webChromeClient = ChromeClient(requireActivity())
        val webSettings = video_view.settings
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.setAppCacheEnabled(true)
        video_view.loadUrl(url)
    }

}
