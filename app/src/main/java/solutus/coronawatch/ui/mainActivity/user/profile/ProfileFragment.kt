package solutus.coronawatch.ui.mainActivity.user.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.coronawatch_mobile.R
import kotlinx.android.synthetic.main.profile_fragment.*
import solutus.coronawatch.data.entity.AppUser
import solutus.coronawatch.service.DatePickerFragment
import solutus.coronawatch.ui.mainActivity.MainActivity
import java.io.ByteArrayOutputStream
import java.text.DateFormat
import java.util.*


class ProfileFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    companion object {

        const val REQUEST_CODE_PICK_IMAGE_CAMERA = 101
        const val REQUEST_CODE_PICK_IMAGE_GALLERY = 102
        fun newInstance() =
            ProfileFragment()
    }

    private lateinit var avatar: de.hdodenhof.circleimageview.CircleImageView
    private lateinit var viewModel: ProfileViewModel
    private lateinit var activity : MainActivity




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.profile_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //set the view model
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        avatar = activity.findViewById(R.id.profile_image)
        val user: AppUser = activity.user

        if (viewModel.profileImagePath != null) {
           setPhoto()
        }
        avatar.setOnClickListener {
            showDialog()
        }

        //manipulation du Date Picker
        date_naissance.inputType = InputType.TYPE_NULL
        date_naissance.setOnClickListener {
            showDatePicker()
        }
        //Initialise the fields
        email.setText(user.email)
        firstName.setText(user.firstName)
        lastName.setText(user.lastName)
        date_naissance.setText(user.birthDate)

        //souvegarder l'état du profile
        souvegarder_button.setOnClickListener {
            saveProfile()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_IMAGE_GALLERY) {
                val selectedImageUri = data?.data
                viewModel.profileImagePath = getRealPathFromURI(selectedImageUri)
                setPhoto()
            }
            if (requestCode == REQUEST_CODE_PICK_IMAGE_CAMERA) {
                val photo = data?.extras?.get("data") as Bitmap
                viewModel.profileImagePath = getRealPathFromURI(getImageUri(activity,photo))
                setPhoto()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val c: Calendar = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val currentDateString: String =
            DateFormat.getDateInstance(DateFormat.FULL).format(c.time)
        date_naissance.setText(currentDateString)
    }

    private fun saveProfile() {
        if (email?.text.toString() == "" || firstName?.text.toString() == "" || lastName?.text.toString() == "" || date_naissance?.text.toString() == "" || password?.text.toString() == "" || confirm_password.text.toString() == "") {
            Toast.makeText(activity, "يجب ملأ كل الحقول", Toast.LENGTH_SHORT).show()

        } else {
            if (password.text.toString() != confirm_password.text.toString()) {
                Toast.makeText(activity, "كلمتا المرور غير متطابقتين", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.saveProfile()
            }
        }
    }

    private fun showDatePicker() {
        //Change the language to Arabic
        val locale: Locale? = Locale("ar")
        Locale.setDefault(locale!!)
        val config: Configuration? = activity.baseContext?.resources?.configuration
        config?.setLocale(locale)
        config?.let { it1 -> activity.createConfigurationContext(it1) }
        val datePicker: DialogFragment = DatePickerFragment(this)
        datePicker.show(
            (activity as AppCompatActivity?)!!.supportFragmentManager,
            "date picker"
        )
    }

    private fun showDialog() {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custome_dialoge_photo)
        val firstChoice = dialog.findViewById(R.id.first_choice_layout) as LinearLayout
        val secondChoice = dialog.findViewById(R.id.second_choice_layout) as LinearLayout
        firstChoice.setOnClickListener {
            chooseImageFromGallery()
            dialog.dismiss()
        }
        secondChoice.setOnClickListener {
            takeImageFromCamera()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun chooseImageFromGallery() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            startActivityForResult(
                it,
                REQUEST_CODE_PICK_IMAGE_GALLERY
            )
        }
    }

    private fun takeImageFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)
        startActivityForResult(
            intent,
            REQUEST_CODE_PICK_IMAGE_CAMERA
        )
    }

    private fun setPhoto() {
        avatar.setImageBitmap(
            BitmapFactory.decodeFile(
                viewModel.profileImagePath
            )
        )
    }

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

