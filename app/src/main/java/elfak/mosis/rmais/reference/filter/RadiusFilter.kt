package elfak.mosis.rmais.reference.filter

import com.google.firebase.database.DataSnapshot
import elfak.mosis.rmais.FB
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.model.ReferencesViewModel

class RadiusFilter(val radius: Double, override val referencesViewModel: ReferencesViewModel): IFilter {
    init {
        FB.referencesDB.addChildEventListener(this)
    }

    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        val ref: Reference? = getTypedReference(snapshot)
        if(ref != null) {
            if(referencesViewModel.distanceFromUser(ref) < radius) {
                addReference(ref)
            }
            referencesViewModel.arrayAdapter?.notifyDataSetChanged()
        }
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        val ref: Reference? = getTypedReference(snapshot)
        if(ref != null) {
            if(referencesViewModel.distanceFromUser(ref) < radius) {
                replaceReference(ref)
            }
            else {
                super.onChildRemoved(snapshot)
            }
        }
    }
}