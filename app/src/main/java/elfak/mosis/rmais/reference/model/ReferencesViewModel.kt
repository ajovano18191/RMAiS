package elfak.mosis.rmais.reference.model

import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import elfak.mosis.rmais.reference.ReferenceDB
import elfak.mosis.rmais.reference.data.Reference

class ReferencesViewModel : ViewModel() {


    private val _referencesList = ArrayList<Reference>()
    val referencesList: ArrayList<Reference>
        get() = _referencesList

    var arrayAdapter: ArrayAdapter<Reference>? = null

    private val referenceDB = ReferenceDB(this)

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