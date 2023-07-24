package elfak.mosis.rmais.model

import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModel
import elfak.mosis.rmais.R
import elfak.mosis.rmais.data.IReference
import elfak.mosis.rmais.data.SOTAReference
import elfak.mosis.rmais.data.WFFReference

class ReferencesViewModel : ViewModel() {
    private var _referencesList: ArrayList<IReference> = ArrayList()

    init {
        _referencesList.clear()
        _referencesList.add(WFFReference("Djerdap",     "YUFF-0001", "KN04XM", 44.52894211, 21.97586060))
        _referencesList.add(WFFReference("Fruska Gora", "YUFF-0002", "JN95UD", 45.25049973, 19.82729912))
        _referencesList.add(WFFReference("Kopaonik",    "YUFF-0003", "KN03JH", 43.31666565, 20.78343010))
        _referencesList.add(WFFReference("Sar-planina", "YUFF-0004", "KN04XM", 42.09, 20.97))
        _referencesList.add(SOTAReference("Tara",        "SOTA-0005", "JN93RU", 43.84695816, 19.45927238))
    }

    val referencesList: ArrayList<IReference>
        get() {
            return _referencesList
        }

    fun addOrUpdateReference(reference: IReference): IReference {
        _referencesList.add(reference)
        return reference
    }

    var selectedReference: IReference? = null

    companion object {
        fun initOthersViews(mView: View, reference: IReference) {
            val titleText = mView.findViewById<TextView>(R.id.infowindow_title_text)
            titleText.text = "${reference.reference} ${reference.name}"

            val latText = mView.findViewById<TextView>(R.id.infowindow_latitude_text)
            latText.text = reference.lat.toString()

            val logText = mView.findViewById<TextView>(R.id.infowindow_longitude_text)
            logText.text = reference.lon.toString()

            val locText = mView.findViewById<TextView>(R.id.infowindow_loc_text)
            locText.text = reference.loc
        }
    }
}