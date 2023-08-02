package elfak.mosis.rmais.reference

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.filter.IFilter
import elfak.mosis.rmais.reference.filter.NoFilter
import elfak.mosis.rmais.reference.model.ReferencesViewModel

class ReferenceDB(referencesViewModel: ReferencesViewModel) {

    var filter: IFilter = NoFilter(referencesViewModel)

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

    companion object {
        val dbRef = Firebase.database.getReference("references")
    }
}