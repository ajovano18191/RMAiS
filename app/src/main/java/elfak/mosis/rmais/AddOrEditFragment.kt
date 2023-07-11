package elfak.mosis.rmais

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import elfak.mosis.rmais.data.IReference
import elfak.mosis.rmais.data.SOTAReference
import elfak.mosis.rmais.data.WFFReference
import elfak.mosis.rmais.model.ReferencesViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [AddOrEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddOrEditFragment : Fragment() {
    private val referencesViewModel: ReferencesViewModel by activityViewModels()

    private lateinit var referencePrefixEditText: EditText
    private lateinit var referenceNumberEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var locEditText: TextView
    private lateinit var latEditText: EditText
    private lateinit var logEditText: EditText

    private lateinit var wffRB: RadioButton
    private lateinit var sotaRB: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        var fab: FloatingActionButton = (requireView().parent.parent.parent as View).findViewById<FloatingActionButton>(R.id.fab)
        fab.hide()

        referencePrefixEditText = requireView().findViewById<EditText>(R.id.addedit_reference_prefix_text)
        referenceNumberEditText = requireView().findViewById<EditText>(R.id.addedit_reference_number_text)
        nameEditText = requireView().findViewById<EditText>(R.id.addedit_name_text)
        locEditText = requireView().findViewById<TextView>(R.id.addedit_loc_text)
        latEditText = requireView().findViewById<EditText>(R.id.addedit_lat_text)
        logEditText = requireView().findViewById<EditText>(R.id.addedit_log_text)

        wffRB = requireView().findViewById<RadioButton>(R.id.addedit_radio_button_wff)
        sotaRB = requireView().findViewById<RadioButton>(R.id.addedit_radio_button_sota)

        val cancelButton: Button = requireView().findViewById<Button>(R.id.addedit_cancel_button)
        val saveButton: Button = requireView().findViewById<Button>(R.id.addedit_save_button)

        if(referencesViewModel.selectedReference != null) {
            if(referencesViewModel.selectedReference is WFFReference) {
                wffRB.isChecked = true
            }
            else if(referencesViewModel.selectedReference is SOTAReference) {
                sotaRB.isChecked = true
            }

            var arrRef = referencesViewModel.selectedReference!!.reference.split('-')
            referencePrefixEditText.setText(arrRef[0])
            referenceNumberEditText.setText(arrRef[1])
            nameEditText.setText(referencesViewModel.selectedReference?.name)
            locEditText.setText(referencesViewModel.selectedReference?.loc)
            latEditText.setText(referencesViewModel.selectedReference?.lat.toString())
            logEditText.setText(referencesViewModel.selectedReference?.log.toString())

            saveButton.setText(R.string.addedit_save_button)
        }
        else {
            saveButton.setText(R.string.addedit_add_button)
        }

        if(referencesViewModel.selectedReference != null) {
            saveButton.setOnClickListener {
                referencesViewModel.updateReference(getReference())
                findNavController().popBackStack()
            }
        }
        else {
            saveButton.setOnClickListener {
                referencesViewModel.addReference(getReference())
                findNavController().popBackStack()
            }
        }

        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getReference() : IReference {
        val referencePrefix: String = referencePrefixEditText.text.toString()
        val referenceNumber: String = referenceNumberEditText.text.toString().padStart(4, '0')
        val name: String = nameEditText.text.toString()
        val loc: String = locEditText.text.toString()
        val lat: String = latEditText.text.toString()
        val log: String = logEditText.text.toString()

        var reference: IReference? = null
        val referenceID = "$referencePrefix-$referenceNumber"
        if(wffRB.isChecked) {
            reference = WFFReference(name, referenceID, loc, lat.toDouble(), log.toDouble())
        }
        else if(sotaRB.isChecked) {
            reference = SOTAReference(name, referenceID, loc, lat.toDouble(), log.toDouble())
        }
        return reference!!
    }
}