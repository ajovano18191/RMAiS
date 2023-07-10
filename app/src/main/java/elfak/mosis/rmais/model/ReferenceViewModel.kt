package elfak.mosis.rmais.model

import android.app.AlertDialog
import android.opengl.Visibility
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import elfak.mosis.rmais.R
import elfak.mosis.rmais.data.IReference
import elfak.mosis.rmais.data.SOTAReference
import elfak.mosis.rmais.data.WFFReference

class ReferencesViewModel() : ViewModel() {
    private var _referencesList: ArrayList<IReference> = ArrayList<IReference>()

    init {
        _referencesList.clear()
        _referencesList.add(WFFReference("Djerdap",     "YUFF-0001", "KN04XM", 44.52894211, 21.97586060))
        _referencesList.add(WFFReference("Fruska Gora", "YUFF-0002", "JN95UD", 45.25049973, 19.82729912))
        _referencesList.add(WFFReference("Kopaonik",    "YUFF-0003", "KN03JH", 43.31666565, 20.78343010))
        _referencesList.add(WFFReference("Sar-planina", "YUFF-0004", "KN04XM", 42.09, 20.97))
        _referencesList.add(SOTAReference("Tara",        "SOTA-0005", "JN93RU", 43.84695816, 19.45927238))
    }

    var referencesList: ArrayList<IReference> = ArrayList<IReference>()
        get() {
            return _referencesList
        }

    fun addReference(reference: IReference) {
        _referencesList.add(reference)
    }

    fun updateReference(reference: IReference) {
        _referencesList.add(reference)
    }

    var selectedReference: IReference? = null

    companion object {
        fun updateView(mView: View, reference: IReference) {
            val titleText = mView.findViewById<TextView>(R.id.infowindow_title_text)
            titleText.text = "${reference.reference} ${reference.name}"

            val latText = mView.findViewById<TextView>(R.id.infowindow_latitude_text)
            latText.text = reference.lat.toString()

            val logText = mView.findViewById<TextView>(R.id.infowindow_longitude_text)
            logText.text = reference.log.toString()

            val locText = mView.findViewById<TextView>(R.id.infowindow_loc_text)
            locText.text = reference.loc

            // Here we are settings onclick listeners for the buttons in the layouts.

            val activateButton = mView.findViewById<ImageButton>(R.id.activate_button)
            val editButton = mView.findViewById<ImageButton>(R.id.edit_button)
            val deleteButton = mView.findViewById<ImageButton>(R.id.delete_button)
            val mapButton = mView.findViewById<ImageButton>(R.id.map_button)
            editButton.setOnClickListener {

            }
            deleteButton.setOnClickListener {

            }

        }
    }
}