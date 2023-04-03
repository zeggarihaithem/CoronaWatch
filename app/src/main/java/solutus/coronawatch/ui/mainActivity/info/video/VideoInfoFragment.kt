package solutus.coronawatch.ui.mainActivity.info.video

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.coronawatch_mobile.R
import kotlinx.android.synthetic.main.video_info_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class VideoInfoFragment : Fragment() , KodeinAware {

    override val kodein by closestKodein()
    companion object {
        fun newInstance() = VideoInfoFragment
        const val REQUEST_CODE_PICK_VIDEO_GALLERY = 100
    }

    private  val viewModel: VideoInfoViewModel by instance<VideoInfoViewModel>()
    private lateinit var mediaController: MediaController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.video_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //set media controller to video view
        mediaController = MediaController(this.context)
        mediaController.setAnchorView(video_view)
        video_view.setMediaController(mediaController)

        if (viewModel.videoPath != null) {
            setVideo()
        }

        //use on first time
        frame_view.setOnClickListener {
            chooseVideoFromGallery()
        }
        //use to replace the video exist
        replace_video_button.setOnClickListener {
            chooseVideoFromGallery()
        }

        //report case on button click
        report_button.setOnClickListener {
            reportCase()
        }
    }

    private fun chooseVideoFromGallery() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "video/*"
            startActivityForResult(it, REQUEST_CODE_PICK_VIDEO_GALLERY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_VIDEO_GALLERY) {
                val selectedVideoUri = data?.data
                viewModel.videoUri=selectedVideoUri
                viewModel.videoPath = getRealPathFromURI(selectedVideoUri)
                setVideo()
            }
        }
    }

    private fun reportCase() {
        if (viewModel.videoPath == null) {
            Toast.makeText(activity, "يجب اضافة فيديو", Toast.LENGTH_SHORT).show()
        } else {
            if (description_edit.text.toString().trim().isEmpty()) {
                Toast.makeText(activity, "اضف وصفا", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.uploadCase(requireContext(), description_edit.text.toString())
                frame_view.isClickable = true
                add_video_layout.visibility = View.VISIBLE
                video_view.visibility = View.GONE
                mediaController.visibility = View.GONE
                replace_video_frame.visibility = View.GONE
                //reset the photo path
                viewModel.videoPath = null
                //empty the edit text
                description_edit.text.clear()
                description_edit.isCursorVisible = false
            }
        }
    }

    private fun setVideo() {
        add_video_layout.visibility = View.GONE
        video_view.visibility = View.VISIBLE
        frame_view.isClickable = false
        replace_video_frame.visibility = View.VISIBLE
        mediaController.visibility = View.VISIBLE
        video_view.setVideoPath(viewModel.videoPath)
        video_view.seekTo(1)
    }

    @SuppressLint("Recycle")
    private fun getRealPathFromURI(uri: Uri?): String? {
        val contentResolver: ContentResolver = requireActivity().contentResolver!!
        val projection =
            arrayOf(MediaStore.Video.Media.DATA)
        val cursor: Cursor = contentResolver.query(uri!!, projection, null, null, null)!!
        val columnIndex: Int = cursor
            .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }
}
