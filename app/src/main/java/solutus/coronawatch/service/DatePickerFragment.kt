package solutus.coronawatch.service

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import java.util.*


class DatePickerFragment(private val fragment: Fragment) : AppCompatDialogFragment() {

    private val c: Calendar = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog { // Set the current date as the default date

        val c: Calendar = Calendar.getInstance()
        val year: Int = c.get(Calendar.YEAR)
        val month: Int = c.get(Calendar.MONTH)
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        // Return a new instance of DatePickerDialog
        return DatePickerDialog(requireActivity(), fragment as OnDateSetListener, year, month, day)
    }

    // called when a date has been selected


}