package elfak.mosis.rmais

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import elfak.mosis.rmais.reference.model.ReferencesViewModel

class LogQSOFragment : Fragment() {
    private val referencesViewModel: ReferencesViewModel by activityViewModels()
    private lateinit var callSignText: EditText
    private lateinit var rstSentText: EditText
    private lateinit var rstRecvText: EditText
    private lateinit var modeSpinner: Spinner
    private lateinit var bandSpinner: Spinner
    private lateinit var infoText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_qso, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab: FloatingActionButton = (requireView().parent.parent.parent as View).findViewById(R.id.fab)
        fab.hide()
        val fabSearch: FloatingActionButton = (requireView().parent.parent.parent as View).findViewById(R.id.fab_search)
        fabSearch.hide()

        findViews(view)
        initLogQSOButton(view)
    }

    private fun findViews(view: View) {
        callSignText = view.findViewById(R.id.log_qso_call_sign_text)
        rstSentText = view.findViewById(R.id.log_qso_rst_sent_text)
        rstRecvText = view.findViewById(R.id.log_qso_rst_recv_text)

        modeSpinner = view.findViewById(R.id.log_qso_mode_spinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.log_qso_modes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            modeSpinner.adapter = adapter
        }

        bandSpinner = view.findViewById(R.id.log_qso_band_spinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.log_qso_bands,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bandSpinner.adapter = adapter
        }

        infoText = view.findViewById(R.id.log_qso_info_text)
    }

    private fun initLogQSOButton(view: View) {
        val logQSOButton: Button = view.findViewById(R.id.log_qso_log_button)
        logQSOButton.setOnClickListener {
            val qso = getQSO()
            qso.log()
            clearViews()
        }
    }

    private fun getQSO(): QSO {
        val callSign = callSignText.text.toString()
        val rstSent = rstSentText.text.toString().toInt()
        val rstRecv = rstSentText.text.toString().toInt()
        val mode = modeSpinner.selectedItem.toString()
        val band = bandSpinner.selectedItem.toString()
        val info = infoText.text.toString()

        return QSO(referencesViewModel.selectedReference!!, callSign, rstSent, rstRecv, mode, band, info)
    }

    private fun clearViews() {
        callSignText.setText("")
        infoText.setText("")
    }
}