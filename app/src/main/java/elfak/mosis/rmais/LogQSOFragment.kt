package elfak.mosis.rmais

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.core.view.children
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import elfak.mosis.rmais.reference.model.ReferencesViewModel

class LogQSOFragment : Fragment() {
    private val referencesViewModel: ReferencesViewModel by activityViewModels()
    private lateinit var callSignText: EditText
    private lateinit var rstSentText: EditText
    private lateinit var rstRecvText: EditText
    private lateinit var modeSpinner: Spinner
    private lateinit var bandSpinner: Spinner
    private lateinit var infoText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log_qso, container, false)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        for (x in menu.children) {
            x.isVisible = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.showHideFabButtons(false)

        findViews(view)
        initLogQSOButton(view)
    }

    private fun findViews(view: View) {
        callSignText = view.findViewById(R.id.log_qso_call_sign_text)
        rstSentText = view.findViewById(R.id.log_qso_rst_sent_text)
        rstRecvText = view.findViewById(R.id.log_qso_rst_recv_text)

        modeSpinner = view.findViewById(R.id.log_qso_mode_spinner)
        initModeSpinner()

        bandSpinner = view.findViewById(R.id.log_qso_band_spinner)
        initBandSpinner()

        infoText = view.findViewById(R.id.log_qso_info_text)
    }

    private fun initModeSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.log_qso_modes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            modeSpinner.adapter = adapter
        }

        modeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val item = p0?.getItemAtPosition(p2).toString()
                var rst = "599"
                when(item) {
                    "CW" -> {
                        rst = "599"
                    }
                    "SSB" -> {
                        rst = "59"
                    }
                    "FM" -> {
                        rst = "59"
                    }
                    "DIGI" -> {
                        rst = "+24"
                    }
                }
                rstSentText.setText(rst)
                rstRecvText.setText(rst)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun initBandSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.log_qso_bands,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            bandSpinner.adapter = adapter
        }
    }

    private fun initLogQSOButton(view: View) {
        val logQSOButton: Button = view.findViewById(R.id.log_qso_log_button)
        logQSOButton.setOnClickListener {
            if(referencesViewModel.distanceFromUser(referencesViewModel.selectedReference!!) < 1) {
                val qso = getQSO()
                qso.log()
                clearViews()
            }
            else {
                Snackbar.make(requireView(), "Morate biti u krugu od 1km od reference", Snackbar.LENGTH_INDEFINITE).show()
            }
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