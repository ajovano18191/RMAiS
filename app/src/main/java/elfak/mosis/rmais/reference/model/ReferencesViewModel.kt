package elfak.mosis.rmais.reference.model

import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import elfak.mosis.rmais.reference.ReferenceDB
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.filter.RadiusFilter
import org.osmdroid.util.GeoPoint

class ReferencesViewModel : ViewModel() {
    var userLocation = GeoPoint(500.0, 500.0)
        set(value) {
            if(referenceDB.filter is RadiusFilter) {
                if(value.latitude != field.latitude || value.longitude != field.longitude) {
                    referenceDB.filter = RadiusFilter((referenceDB.filter as RadiusFilter).radius, this)
                }
            }
            field = value
        }

    private val _referencesList = ArrayList<Reference>()
    val referencesList: ArrayList<Reference>
        get() = _referencesList

    var arrayAdapter: ArrayAdapter<Reference>? = null

    val referenceDB = ReferenceDB(this)

    fun addOrUpdateReference(reference: Reference): Reference {
        referenceDB.addOrUpdate(reference)
        arrayAdapter?.notifyDataSetChanged()
        return reference
    }

    fun deleteReference(reference: Reference) {
        referenceDB.delete(reference)
        arrayAdapter?.notifyDataSetChanged()
    }

    private var selectedReferenceIndex: Int = -1
    var selectedReference: Reference?
        get() {
            if(selectedReferenceIndex >= 0) {
                return _referencesList[selectedReferenceIndex]
            }
            return null
        }
        set(value) {
            selectedReferenceIndex = _referencesList.indexOf(value)
        }
}