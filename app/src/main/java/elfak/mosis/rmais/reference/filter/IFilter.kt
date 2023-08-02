package elfak.mosis.rmais.reference.filter

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.getValue
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.data.SOTAReference
import elfak.mosis.rmais.reference.data.WFFReference
import elfak.mosis.rmais.reference.model.ReferencesViewModel

interface IFilter: ChildEventListener {
    val referencesViewModel: ReferencesViewModel

    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        val ref: Reference? = getTypedReference(snapshot)
        if(ref != null) {
            addReference(ref)
        }
    }

    fun addReference(ref: Reference) {
        referencesViewModel.referencesList.add(ref)
        referencesViewModel.arrayAdapter?.notifyDataSetChanged()
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        val ref: Reference? = getTypedReference(snapshot)
        if(ref != null) {
            replaceReference(ref)
        }
    }

    fun replaceReference(ref: Reference) {
        val ind = refInd(ref)
        if(ind >= 0) {
            val isInfoWindowOpen =
                referencesViewModel.referencesList[ind].referenceMarker.remove()
            referencesViewModel.referencesList[ind] = ref
            if(isInfoWindowOpen) {
                ref.referenceMarker.showInfoWindow()
            }
            referencesViewModel.arrayAdapter?.notifyDataSetChanged()
        }
    }

    fun refInd(ref: Reference): Int {
        val cnt = referencesViewModel.referencesList.count() - 1
        for(i in 0..cnt) {
            if(ref.key == referencesViewModel.referencesList[i].key) {
                return i
            }
        }
        return -1
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        for(ref in referencesViewModel.referencesList.filter { ref -> ref.key == snapshot.key }) {
            referencesViewModel.referencesList.remove(ref)
            ref.referenceMarker.remove()
        }
        referencesViewModel.arrayAdapter?.notifyDataSetChanged()
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

    }

    override fun onCancelled(error: DatabaseError) {
        Log.v("Pufla", error.toString())
    }

    fun getTypedReference(snapshot: DataSnapshot, isInfoWindowOpen: Boolean = false): Reference? {
        var ref: Reference? = null

        if((snapshot.value as? HashMap<*, *>)?.get("type") == "WFF") {
            ref = snapshot.getValue<WFFReference>()!!
        }
        else if((snapshot.value as? HashMap<*, *>)?.get("type") == "SOTA") {
            ref = snapshot.getValue<SOTAReference>()!!
        }

        ref?.key = snapshot.key!!
        ref?.referenceMarker?.create(referencesViewModel)

        return ref
    }
}