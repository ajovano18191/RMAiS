package elfak.mosis.rmais.reference

import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.getValue
import elfak.mosis.rmais.FB
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
            FB.referencesDB.removeEventListener(field)
            for(ref in field.referencesViewModel.referencesList) {
                ref.referenceMarker.remove()
            }
            field.referencesViewModel.referencesList.clear()
            field.referencesViewModel.arrayAdapter?.notifyDataSetChanged()
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
        val dbRefForWrite = FB.referencesDB.child(reference.key)
        CoroutineScope(Dispatchers.IO).launch {
            reference.creationDateTime = await(dbRefForWrite.child("creationDateTime").get()).getValue<Long>() ?: 0
            reference.lastActivationDateTime = await(dbRefForWrite.child("lastActivationDateTime").get()).getValue<Long>() ?: 0
            reference.authorKey = await(dbRefForWrite.child("authorKey").get()).getValue<String>() ?: ""
            reference.authorCallSign = await(dbRefForWrite.child("authorCallSign").get()).getValue<String>() ?: ""
            dbRefForWrite.setValue(reference)
        }
    }

    private fun add(reference: Reference) {
        reference.authorKey = FB.auth.currentUser!!.uid
        reference.authorCallSign = FB.auth.currentUser!!.email!!.substringBefore('@').uppercase()
        val dbRefForWrite = FB.referencesDB.push()
        dbRefForWrite.setValue(reference)
            .addOnSuccessListener {
                dbRefForWrite.child("creationDateTime").setValue(ServerValue.TIMESTAMP)
            }
    }

    fun delete(reference: Reference) {
        if(reference.key.isNotEmpty()) {
            FB.referencesDB.child(reference.key).setValue(null)
        }
    }
}