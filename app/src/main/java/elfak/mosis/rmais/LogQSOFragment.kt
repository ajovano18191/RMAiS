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
import com.google.android.material.snackbar.Snackbar
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.model.ReferencesViewModel
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

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

        MainActivity.showHideFabButtons(false)

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
            if(calculateDistance(referencesViewModel.selectedReference!!) < 1) {
                val qso = getQSO()
                qso.log()
                clearViews()
            }
            else {
                Snackbar.make(requireView(), "Morate biti u krugu od 1km od reference", Snackbar.LENGTH_INDEFINITE).show()
            }
        }
    }

    private fun calculateDistance(ref: Reference): Double {
        val testGP = referencesViewModel.userLocation
        val dLat = Math.toRadians(ref.lat - testGP.latitude)
        val dLon = Math.toRadians(ref.lon - testGP.longitude)
        val originLat = Math.toRadians(testGP.latitude)
        val destinationLat = Math.toRadians(ref.lat)

        val a = sin(dLat / 2).pow(2.toDouble()) + sin(dLon / 2).pow(2.toDouble()) * cos(originLat) * cos(destinationLat)
        val c = 2 * asin(sqrt(a))

        val radius = 6372.8

        return radius * c
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