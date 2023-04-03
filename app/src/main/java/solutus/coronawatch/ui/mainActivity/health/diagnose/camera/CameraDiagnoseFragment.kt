package solutus.coronawatch.ui.mainActivity.health.diagnose.camera

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.coronawatch_mobile.R
import kotlinx.android.synthetic.main.camera_diagnose_fragment.*
import java.io.ByteArrayOutputStream

class CameraDiagnoseFragment : Fragment() {

    companion object {
        fun newInstance() = CameraDiagnoseFragment()
        const val REQUEST_CODE_PICK_IMAGE_CAMERA = 100
        private lateinit var navController: NavController
    }

    private lateinit var viewModel: CameraDiagnoseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.camera_diagnose_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CameraDiagnoseViewModel::class.java)
        if (viewModel.photoPath != null) {
            setPhoto()
        }

        //use on first time
        frame_view.setOnClickListener {
            takePhotoCamera()
        }
        //use to replace the photo exist
        replace_photo_button.setOnClickListener {
            takePhotoCamera()
        }
        //report case on button click
        diagnose_button.setOnClickListener {
            diagnose()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_IMAGE_CAMERA) {
                val selectedMediaUri: Uri? =
                    activity?.let { getImageUri(it, data?.extras?.get("data") as Bitmap) }
                viewModel.photoPath = getRealPathFromURI(selectedMediaUri)
                setPhoto()
            }
        }
    }

    private fun takePhotoCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE_CAMERA)
    }

    private fun diagnose() {
        if (viewModel.photoPath == null) {
            Toast.makeText(activity, "يجب اضافة صورة", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.uploadData(requireContext())
            toResultFragment()
        }
    }

    private fun toResultFragment() {
        val diagnoseNavController =
            Navigation.findNavController(requireActivity(), R.id.nav_health_fragment)
        diagnoseNavController.navigate(R.id.to_result_fragment_action)
    }

    private fun setPhoto() {
        add_photo_layout.visibility = View.GONE
        photo_view.visibility = View.VISIBLE
        frame_view.isClickable = false
        replace_photo_frame.visibility = View.VISIBLE
        photo_view.setImageBitmap(
            BitmapFactory.decodeFile(
                viewModel.photoPath
            )
        )
    }

    @SuppressLint("Recycle")
    private fun getRealPathFromURI(uri: Uri?): String? {
        val contentResolver: ContentResolver = requireActivity().contentResolver!!
        val projection =
            arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = contentResolver.query(uri!!, projection, null, null, null)!!
        val columnIndex: Int = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


}



