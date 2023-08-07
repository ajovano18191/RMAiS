package elfak.mosis.rmais.reference

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import elfak.mosis.rmais.R
import elfak.mosis.rmais.reference.filter.DateFilter
import elfak.mosis.rmais.reference.filter.NoFilter
import elfak.mosis.rmais.reference.filter.RadiusFilter
import elfak.mosis.rmais.reference.filter.StringFilter
import elfak.mosis.rmais.reference.model.ReferencesViewModel
import java.lang.Long.max
import java.lang.Long.min
import java.util.Calendar

class FilterDialog(private val referencesViewModel: ReferencesViewModel) {
    private lateinit var alertDialog: AlertDialog
    private lateinit var valueLayout: LinearLayout
    private lateinit var calendarsLayout: LinearLayout
    private var selectedFieldIndex: Int = 0
    private var selectedValue: String = ""
    private lateinit var dateFromCalendarView: CalendarView
    private lateinit var dateToCalendarView: CalendarView
    private lateinit var valueText: EditText
    private lateinit var spinner: Spinner

    private var selectedDateFrom: Long = Calendar.getInstance().timeInMillis
        set(value) {
            dateFromCalendarView.date = value
            field = value
        }
    private var selectedDateTo: Long = Calendar.getInstance().timeInMillis
        set(value) {
            dateToCalendarView.date = value
            field = value
        }

    fun show(context: Context) {
        alertDialog = AlertDialog.Builder(context)
            .setView(R.layout.dialog_filter)
            .show()
        initViews()
    }

    private fun initViews() {
        valueLayout = alertDialog.findViewById(R.id.filter_value_layout)
        calendarsLayout = alertDialog.findViewById(R.id.filter_calendars_layout)

        initSpinner()
        initValueText()
        initCalendarViews()
        initApplyButton()
        initClearAllButton()
        initCloseButton()
    }

    private fun initSpinner() {
        spinner = alertDialog.findViewById(R.id.filter_field_spinner)
        ArrayAdapter.createFromResource(
            alertDialog.context,
            R.array.filter_fields,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.setSelection(selectedFieldIndex)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                onItemSelected(p0, p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun onItemSelected(adapterView: AdapterView<*>?, index: Int) {
        val it = adapterView?.getItemAtPosition(index).toString()
        valueLayout.visibility = View.GONE
        calendarsLayout.visibility = View.GONE

        if(it == alertDialog.context.resources.getString(R.string.filter_spinner_creation_date)
            || it == alertDialog.context.resources.getString(R.string.filter_spinner_last_activation_date)) {
            calendarsLayout.visibility = View.VISIBLE
        }
        else {
            valueLayout.visibility = View.VISIBLE
        }
    }

    private fun initValueText() {
        valueText = alertDialog.findViewById(R.id.filter_value_text)
        valueText.setText(selectedValue, TextView.BufferType.EDITABLE)
    }

    private fun initCalendarViews() {
        dateToCalendarView = alertDialog.findViewById(R.id.filter_to_date)
        dateFromCalendarView = alertDialog.findViewById(R.id.filter_from_date)

        dateFromCalendarView.date = selectedDateFrom
        dateFromCalendarView.setOnDateChangeListener { _, year, month, day ->
            selectedDateFrom = dateToLong(year, month, day)
            selectedDateTo = max(selectedDateFrom, selectedDateTo)
        }

        dateToCalendarView.date = selectedDateTo
        dateToCalendarView.setOnDateChangeListener { _, year, month, day ->
            selectedDateTo = dateToLong(year, month, day)
            selectedDateFrom = min(selectedDateFrom, selectedDateTo)
        }
    }

    private fun dateToLong(year: Int, month: Int, day: Int): Long {
        val c: Calendar = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, day)
        return c.timeInMillis
    }

    private fun initApplyButton() {
        val applyButton: Button = alertDialog.findViewById(R.id.filter_apply_button)
        applyButton.setOnClickListener {
            selectedFieldIndex = spinner.selectedItemPosition
            val selectedField = spinner.selectedItem.toString()
            selectedValue = valueText.text.toString()

            Log.v("Pufla", "$selectedDateFrom $selectedDateTo")

            setFilter(selectedField)
            alertDialog.cancel()
        }
    }

    private fun setFilter(field: String) {
        referencesViewModel.referenceDB.filter = NoFilter(referencesViewModel)
        when(field) {
            alertDialog.context.resources.getString(R.string.filter_spinner_type) -> {
                referencesViewModel.referenceDB.filter = StringFilter("type", selectedValue, referencesViewModel)
            }
            alertDialog.context.resources.getString(R.string.filter_spinner_reference) -> {
                referencesViewModel.referenceDB.filter = StringFilter("reference", selectedValue, referencesViewModel)
            }
            alertDialog.context.resources.getString(R.string.filter_spinner_name) -> {
                referencesViewModel.referenceDB.filter = StringFilter("name", selectedValue, referencesViewModel)
            }
            alertDialog.context.resources.getString(R.string.filter_spinner_QTH_Loc) -> {
                referencesViewModel.referenceDB.filter = StringFilter("loc", selectedValue, referencesViewModel)
            }
            alertDialog.context.resources.getString(R.string.filter_spinner_radius) -> {
                referencesViewModel.referenceDB.filter = RadiusFilter(selectedValue.toDouble(), referencesViewModel)
            }
            alertDialog.context.resources.getString(R.string.filter_spinner_creation_date) -> {
                referencesViewModel.referenceDB.filter = DateFilter("creationDateTime", selectedDateFrom, selectedDateTo, referencesViewModel)
            }
            alertDialog.context.resources.getString(R.string.filter_spinner_last_activation_date) -> {
                referencesViewModel.referenceDB.filter = DateFilter("lastActivationDateTime", selectedDateFrom, selectedDateTo, referencesViewModel)
            }
        }
    }

    private fun initClearAllButton() {
        val clearAllButton: Button = alertDialog.findViewById(R.id.filter_clear_all_button)
        clearAllButton.setOnClickListener {
            selectedFieldIndex = 0
            selectedValue = ""
            referencesViewModel.referenceDB.filter = NoFilter(referencesViewModel)
        }
    }

    private fun initCloseButton() {
        val closeButton: ImageButton = alertDialog.findViewById(R.id.filter_close_button)
        closeButton.setOnClickListener {
            alertDialog.cancel()
        }
    }
}