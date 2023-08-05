package elfak.mosis.rmais.reference

import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.filter.IFilter
import elfak.mosis.rmais.reference.filter.NoFilter
import elfak.mosis.rmais.reference.model.ReferencesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReferenceDB(referencesViewModel: ReferencesViewModel) {

    var filter: IFilter = NoFilter(referencesViewModel)
        set(value) {
            dbRef.removeEventListener(field)
            for(ref in field.referencesViewModel.referencesList) {
                ref.referenceMarker.remove()
            }
            field.referencesViewModel.referencesList.clear()
            field = value
        }

    fun addOrUpdate(reference: Reference) {
        if(reference.key.isNotEmpty()) {
            update(reference)
        }
        else {
            add(reference)
        }
    }

    private fun update(reference: Reference) {
        val dbRefForWrite = dbRef.child(reference.key)
        CoroutineScope(Dispatchers.IO).launch {
            reference.creationDateTime = await(dbRefForWrite.child("creationDateTime").get()).getValue<Long>() ?: 0
            dbRefForWrite.setValue(reference)
        }
    }

    private fun add(reference: Reference) {
        val dbRefForWrite = dbRef.push()
        dbRefForWrite.setValue(reference)
            .addOnSuccessListener {
                dbRefForWrite.child("creationDateTime").setValue(ServerValue.TIMESTAMP)
            }
    }

    fun delete(reference: Reference) {
        if(reference.key.isNotEmpty()) {
            dbRef.child(reference.key).setValue(null)
        }
    }

    companion object {
        val dbRef = Firebase.database.getReference("references")
    }
}