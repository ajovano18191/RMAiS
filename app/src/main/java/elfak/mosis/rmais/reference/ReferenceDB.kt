package elfak.mosis.rmais.reference

import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import elfak.mosis.rmais.ListUsersFragment
import elfak.mosis.rmais.MainActivity
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
        val dbRefForWrite = dbRef.child(reference.key)
        CoroutineScope(Dispatchers.IO).launch {
            reference.creationDateTime = await(dbRefForWrite.child("creationDateTime").get()).getValue<Long>() ?: 0
            reference.lastActivationDateTime = await(dbRefForWrite.child("lastActivationDateTime").get()).getValue<Long>() ?: 0
            reference.authorKey = await(dbRefForWrite.child("authorKey").get()).getValue<String>() ?: ""
            reference.authorCallSign = await(dbRefForWrite.child("authorCallSign").get()).getValue<String>() ?: ""
            dbRefForWrite.setValue(reference)
        }
    }

    private fun add(reference: Reference) {
        reference.authorKey = MainActivity.auth.currentUser!!.uid
        reference.authorCallSign = MainActivity.auth.currentUser!!.email!!.substringBefore('@').uppercase()
        val dbRefForWrite = dbRef.push()
        dbRefForWrite.setValue(reference)
            .addOnSuccessListener {
                dbRefForWrite.child("creationDateTime").setValue(ServerValue.TIMESTAMP)
            }
        addPoints2User()
    }

    private fun addPoints2User() {
        CoroutineScope(Dispatchers.IO).launch {
            var score = await(
                ListUsersFragment.usersDB.child(MainActivity.auth.currentUser!!.uid).get()
            ).getValue<Long>() ?: 0
            score += 10
            ListUsersFragment.usersDB.child(MainActivity.auth.currentUser!!.uid).setValue(
                object {
                    val score = score
                    val callSign =
                        MainActivity.auth.currentUser!!.email!!.substringBefore('@').uppercase()
                }
            )
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