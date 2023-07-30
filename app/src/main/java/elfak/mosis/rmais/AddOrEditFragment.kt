package elfak.mosis.rmais

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.data.SOTAReference
import elfak.mosis.rmais.reference.data.WFFReference
import elfak.mosis.rmais.reference.model.LocationViewModel
import elfak.mosis.rmais.reference.model.ReferencesViewModel

class AddOrEditFragment : Fragment() {
    private val referencesViewModel: ReferencesViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()

    private lateinit var referencePrefixEditText: EditText
    private lateinit var referenceNumberEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var locEditText: TextView
    private lateinit var latEditText: EditText
    private lateinit var lonEditText: EditText

    private lateinit var setLocationButton: Button

    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button

    private lateinit var wffRB: RadioButton
    private lateinit var sotaRB: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_or_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab: FloatingActionButton = (requireView().parent.parent.parent as View).findViewById(R.id.fab)
        fab.hide()

        findViews()
        initLatAndLon()
        initOtherControls()

        initSaveButton()
        initSetLocationButton()
        initCancelButton()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        for (x in menu.children) {
            x.isVisible = false
        }
    }

    private val isEdit: Boolean
        get() = referencesViewModel.selectedReference != null

    private fun findViews() {
        val view = requireView()
        referencePrefixEditText = view.findViewById(R.id.addedit_reference_prefix_text)
        referenceNumberEditText = view.findViewById(R.id.addedit_reference_number_text)
        nameEditText = view.findViewById(R.id.addedit_name_text)
        locEditText = view.findViewById(R.id.addedit_loc_text)

        latEditText = view.findViewById(R.id.addedit_lat_text)
        lonEditText = view.findViewById(R.id.addedit_log_text)

        setLocationButton = view.findViewById(R.id.addedit_set_location_button)

        cancelButton = view.findViewById(R.id.addedit_cancel_button)
        saveButton = view.findViewById(R.id.addedit_save_button)

        wffRB = view.findViewById(R.id.addedit_radio_button_wff)
        sotaRB = view.findViewById(R.id.addedit_radio_button_sota)
    }

    private fun initLatAndLon() {
        latEditText.doOnTextChanged { text, _, _, _ ->
            setLocText(text, lonEditText.text)
        }
        val latObserver = Observer<Double> { lat ->
            latEditText.setText(lat.toString())
        }
        locationViewModel.lat.observe(viewLifecycleOwner, latObserver)

        lonEditText.doOnTextChanged { text, _, _, _ ->
            setLocText(latEditText.text, text)
        }
        val lonObserver = Observer<Double> { newValue ->
            lonEditText.setText(newValue.toString())
        }
        locationViewModel.lon.observe(viewLifecycleOwner, lonObserver)
    }

    private fun initOtherControls() {
        if(isEdit) {
            wffRB.isChecked = referencesViewModel.selectedReference is WFFReference
            sotaRB.isChecked = referencesViewModel.selectedReference is SOTAReference

            val arrRef = referencesViewModel.selectedReference!!.reference.split('-')
            referencePrefixEditText.setText(arrRef[0])
            referenceNumberEditText.setText(arrRef[1])

            nameEditText.setText(referencesViewModel.selectedReference!!.name)
            locEditText.text = referencesViewModel.selectedReference!!.loc

            if(locationViewModel.lat.value == 0.0 && locationViewModel.lon.value == 0.0) {
                locationViewModel.setLocation(referencesViewModel.selectedReference!!.lon, referencesViewModel.selectedReference!!.lat)
            }
        }
    }

    private fun initSetLocationButton() {
        setLocationButton.setOnClickListener {
            locationViewModel.setLocation = true
            findNavController().navigate(R.id.action_AddOrEditFragment_to_MapFragment)
        }
    }

    private fun initSaveButton() {
        if(isEdit) {
            saveButton.isEnabled = true
            saveButton.setText(R.string.addedit_save_button)
        }
        else {
            saveButton.isEnabled = false
            saveButton.setText(R.string.addedit_add_button)
        }

        saveButton.setOnClickListener {
            referencesViewModel.selectedReference = referencesViewModel.addOrUpdateReference(getReference())
            locationViewModel.setLocation(0.0, 0.0)
            findNavController().popBackStack()
        }
    }

    private fun initCancelButton() {
        cancelButton.setOnClickListener {
            locationViewModel.setLocation(0.0, 0.0)
            findNavController().popBackStack()
        }
    }

    private fun setLocText(latText: CharSequence?, lonText: CharSequence?) {
        saveButton.isEnabled = false
        try {
            locEditText.text = locationViewModel.gcs2QTH(latText!!.toString().toDouble(), lonText!!.toString().toDouble())
            saveButton.isEnabled = true
        }
        catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getReference() : Reference {
        val referencePrefix: String = referencePrefixEditText.text.toString()
        val referenceNumber: String = referenceNumberEditText.text.toString().padStart(4, '0')
        val name: String = nameEditText.text.toString()
        val loc: String = locEditText.text.toString()
        val lat: String = latEditText.text.toString()
        val lon: String = lonEditText.text.toString()

        var reference: Reference? = null
        val referenceID = "$referencePrefix-$referenceNumber"
        if(wffRB.isChecked) {
            reference = WFFReference("", name, referenceID, loc, lat.toDouble(), lon.toDouble())
        }
        else if(sotaRB.isChecked) {
            reference = SOTAReference("", name, referenceID, loc, lat.toDouble(), lon.toDouble())
        }
        return reference!!
    }
}