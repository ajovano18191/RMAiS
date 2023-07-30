package elfak.mosis.rmais.reference

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.data.SOTAReference
import elfak.mosis.rmais.reference.data.WFFReference
import elfak.mosis.rmais.reference.model.ReferencesViewModel

internal class ReferenceDB(private val referencesViewModel: ReferencesViewModel) {
    private val dbRef = Firebase.database.getReference("references")

    init {
        dbRef.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                this@ReferenceDB.onChildAdded(snapshot, previousChildName)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                this@ReferenceDB.onChildChanged(snapshot, previousChildName)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                this@ReferenceDB.onChildRemoved(snapshot)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                this@ReferenceDB.onChildMoved(snapshot, previousChildName)
            }

            override fun onCancelled(error: DatabaseError) {
                this@ReferenceDB.onCancelled(error)
            }
        })
    }

    private fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        val ref: Reference? = getTypedReference(snapshot)
        if(ref != null) {
            referencesViewModel.referencesList.add(ref)
            referencesViewModel.arrayAdapter?.notifyDataSetChanged()
        }
    }

    private fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        for(ref in referencesViewModel.referencesList) {
            if(ref.key == snapshot.key) {
                val ind = referencesViewModel.referencesList.indexOf(ref)
                if(ind >= 0) {
                    replaceReference(ind, snapshot)
                }
                break
            }
        }
        referencesViewModel.arrayAdapter?.notifyDataSetChanged()
    }

    private fun onChildRemoved(snapshot: DataSnapshot) {
        for(ref in referencesViewModel.referencesList) {
            if(ref.key == snapshot.key) {
                referencesViewModel.referencesList.remove(ref)
                ref.referenceMarker.remove()
                break
            }
        }
        referencesViewModel.arrayAdapter?.notifyDataSetChanged()
    }

    private fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        TODO("Not yet implemented")
    }

    private fun onCancelled(error: DatabaseError) {
        TODO("Not yet implemented")
    }

    private fun getTypedReference(snapshot: DataSnapshot, isInfoWindowOpen: Boolean = false): Reference? {
        var ref: Reference? = null

        if((snapshot.value as? HashMap<*, *>)?.get("type") == "WFF") {
            ref = snapshot.getValue<WFFReference>()!!
        }
        else if((snapshot.value as? HashMap<*, *>)?.get("type") == "SOTA") {
            ref = snapshot.getValue<SOTAReference>()!!
        }

        ref?.key = snapshot.key!!
        ref?.referenceMarker?.create(referencesViewModel, isInfoWindowOpen)

        return ref
    }

    private fun replaceReference(ind: Int, snapshot: DataSnapshot) {
        val isInfoWindowOpen = referencesViewModel.referencesList[ind].referenceMarker.remove()
        val ref: Reference? = getTypedReference(snapshot, isInfoWindowOpen)

        if(ref != null) {
            referencesViewModel.referencesList[ind] = ref
            referencesViewModel.arrayAdapter?.notifyDataSetChanged()
        }
    }

    fun addOrUpdate(reference: Reference) {
        var dbRefForWrite = dbRef.push()
        if(reference.key.isNotEmpty()) {
            dbRefForWrite = dbRef.child(reference.key)
        }
        dbRefForWrite.setValue(reference)
    }

    fun delete(reference: Reference) {
        if(reference.key.isNotEmpty()) {
            dbRef.child(reference.key).setValue(null)
        }
    }
}