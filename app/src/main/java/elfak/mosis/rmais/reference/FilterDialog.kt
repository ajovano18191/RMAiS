package elfak.mosis.rmais.reference

import android.app.AlertDialog
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import elfak.mosis.rmais.R
import elfak.mosis.rmais.reference.filter.StringFilter
import elfak.mosis.rmais.reference.filter.IFilter
import elfak.mosis.rmais.reference.filter.NoFilter
import elfak.mosis.rmais.reference.model.ReferencesViewModel

class FilterDialog(private val referencesViewModel: ReferencesViewModel) {
    private lateinit var alertDialog: AlertDialog
    private var selectedFieldIndex: Int = 0
    private var selectedValue: String = ""

    fun show(context: Context) {
        alertDialog = AlertDialog.Builder(context)
            .setView(R.layout.dialog_filter)
            .show()
        initViews()
    }

    private fun initViews() {
        val valueText: EditText = alertDialog.findViewById(R.id.filter_value_text)
        valueText.setText(selectedValue, TextView.BufferType.EDITABLE)

        initSpinner()
        initApplyButton()
        initClearAllButton()
        initCloseButton()
    }

    private fun initSpinner() {
        val spinner: Spinner = alertDialog.findViewById(R.id.filter_field_spinner)
        ArrayAdapter.createFromResource(
            alertDialog.context,
            R.array.filter_fields,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.setSelection(selectedFieldIndex)
    }

    private fun initApplyButton() {
        val spinner: Spinner = alertDialog.findViewById(R.id.filter_field_spinner)
        val applyButton: Button = alertDialog.findViewById(R.id.filter_apply_button)
        applyButton.setOnClickListener {
            selectedFieldIndex = spinner.selectedItemPosition
            val selectedField = spinner.selectedItem.toString()
            val valueText: EditText = alertDialog.findViewById(R.id.filter_value_text)
            selectedValue = valueText.text.toString()
            referencesViewModel.referenceDB.filter = chooseFilter(selectedField)
            alertDialog.cancel()
        }
    }

    private fun chooseFilter(field: String): IFilter {
        var filter: IFilter = NoFilter(referencesViewModel)
        when(field) {
            alertDialog.context.resources.getString(R.string.filter_spinner_type) -> {
                filter = StringFilter("type", selectedValue, referencesViewModel)
            }
            alertDialog.context.resources.getString(R.string.filter_spinner_reference) -> {
                filter = StringFilter("reference", selectedValue, referencesViewModel)
            }
            alertDialog.context.resources.getString(R.string.filter_spinner_name) -> {
                filter = StringFilter("name", selectedValue, referencesViewModel)
            }
            alertDialog.context.resources.getString(R.string.filter_spinner_QTH_Loc) -> {
                filter = StringFilter("loc", selectedValue, referencesViewModel)
            }
            alertDialog.context.resources.getString(R.string.filter_spinner_radius) -> {

            }
        }
        return filter
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