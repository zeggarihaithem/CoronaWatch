package solutus.coronawatch.ui.mainActivity.info.camera

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.coronawatch_mobile.R
import kotlinx.android.synthetic.main.camera_info_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import solutus.coronawatch.ui.mainActivity.MainActivity
import java.io.ByteArrayOutputStream


class CameraInfoFragment : Fragment()  , KodeinAware{

    override val kodein by closestKodein()
    companion object {
        fun newInstance() = CameraInfoFragment
        const val REQUEST_CODE_PICK_IMAGE_CAMERA = 100
        const val REQUEST_CODE_PICK_VIDEO_CAMERA = 101
        const val PHOTO_TYPE = "PHOTO"
        const val VIDEO_TYPE = "VIDEO"
    }

    private  val viewModel: CameraInfoViewModel by instance<CameraInfoViewModel>()
    private lateinit var activity: MainActivity
    private lateinit var mediaController: MediaController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.camera_info_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //set view model


        //set media controller to video view
        mediaController = MediaController(this.context)
        mediaController.setAnchorView(video_view)
        video_view.setMediaController(mediaController)

        if (viewModel.mediaPath != null) {
            setMedia()
        }

        //use on first time
        frame_view.setOnClickListener {
            showDialog()
        }
        //use to replace the media exist
        replace_media_button.setOnClickListener {
            showDialog()
        }

        //report case on button click
        report_button.setOnClickListener {
            reportCase()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_VIDEO_CAMERA || requestCode == REQUEST_CODE_PICK_IMAGE_CAMERA) {
                viewModel.type = when (requestCode) {
                    REQUEST_CODE_PICK_IMAGE_CAMERA -> PHOTO_TYPE
                    REQUEST_CODE_PICK_VIDEO_CAMERA -> VIDEO_TYPE
                    else -> null.toString()
                }
                val selectedMediaUri: Uri? = when (viewModel.type) {
                    PHOTO_TYPE -> getImageUri(activity, data?.extras?.get("data") as Bitmap)
                    VIDEO_TYPE -> data?.data
                    else -> null
                }
                viewModel.mediaUri = selectedMediaUri
                viewModel.mediaPath = getRealPathFromURI(selectedMediaUri, viewModel.type!!)
                setMedia()
            }
        }
    }


    private fun takePhotoCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE_CAMERA)
    }

    private fun takeVideoCamera() {
        val intent = Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA)
        startActivityForResult(intent, REQUEST_CODE_PICK_VIDEO_CAMERA)
    }

    private fun reportCase() {
        if (viewModel.mediaPath == null) {
            Toast.makeText(activity, "يجب اضافة صورة او فيديو", Toast.LENGTH_SHORT).show()
        } else {
            if (description_edit.text.toString().trim().isEmpty()) {
                Toast.makeText(activity, "اضف وصفا", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.uploadCase(requireContext(), description_edit.text.toString())
                frame_view.isClickable = true
                add_media_layout.visibility = View.VISIBLE
                photo_view.visibility = View.GONE
                video_view.visibility = View.GONE
                replace_media_frame.visibility = View.GONE
                //reset the photo path
                viewModel.mediaPath = null
                //empty the edit text
                description_edit.text.clear()
                description_edit.isCursorVisible = false
            }
        }
    }

    private fun setMedia() {
        add_media_layout.visibility = View.GONE
        frame_view.isClickable = false
        replace_media_frame.visibility = View.VISIBLE
        if (viewModel.type == VIDEO_TYPE) {
            video_view.visibility = View.VISIBLE
            photo_view.visibility = View.GONE
            video_view.setVideoPath(viewModel.mediaPath)
            video_view.seekTo(1)
        }
        if (viewModel.type == PHOTO_TYPE) {
            photo_view.visibility = View.VISIBLE
            video_view.visibility = View.GONE
            photo_view.setImageBitmap(
                BitmapFactory.decodeFile(
                    viewModel.mediaPath
                )
            )
        }

    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    @SuppressLint("Recycle")
    private fun getRealPathFromURI(uri: Uri?, type: String): String? {
        val contentResolver: ContentResolver = requireActivity().contentResolver!!
        val projection = when (type) {
            PHOTO_TYPE -> arrayOf(Images.Media.DATA)
            VIDEO_TYPE -> arrayOf(MediaStore.Video.Media.DATA)
            else -> null
        }
        val cursor: Cursor = contentResolver.query(uri!!, projection, null, null, null)!!
        val columnIndex: Int? = when (type) {
            PHOTO_TYPE -> cursor.getColumnIndexOrThrow(Images.Media.DATA)
            VIDEO_TYPE -> cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            else -> null
        }
        cursor.moveToFirst()
        return cursor.getString(columnIndex!!)

    }

    private fun showDialog() {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custome_dialog_image_video)
        val firstChoice = dialog.findViewById(R.id.first_choice_layout) as LinearLayout
        val secondChoice = dialog.findViewById(R.id.second_choice_layout) as LinearLayout
        firstChoice.setOnClickListener {
            takePhotoCamera()
            dialog.dismiss()
        }
        secondChoice.setOnClickListener {
            takeVideoCamera()
            dialog.dismiss()
        }
        dialog.show()
    }

}
