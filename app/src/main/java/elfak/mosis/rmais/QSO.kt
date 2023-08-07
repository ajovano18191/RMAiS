package elfak.mosis.rmais

import android.util.Log
import com.google.firebase.database.Exclude
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import elfak.mosis.rmais.reference.ReferenceDB
import elfak.mosis.rmais.reference.data.Reference
import java.util.Calendar

data class QSO(
    @get:Exclude
    val reference: Reference,
    val callSign: String,
    val rstSent: Int,
    val rstRecv: Int,
    val mode: String,
    var band: String,
    val info: String
) {
    val dateTime: Long = Calendar.getInstance().timeInMillis
    val userKey = MainActivity.auth.currentUser!!.uid
    val userCallSign = MainActivity.auth.currentUser!!.email!!.substringBefore('@').uppercase()
    val referenceKey = reference.key
    val referenceToString = reference.toString()

    init {
        band = band.substringBefore(' ').substringBefore('m')
    }

    fun log() {
        dbQSO.push().setValue(this)
            .addOnSuccessListener {
                ReferenceDB.dbRef
                    .child(reference.key)
                    .child("lastActivationDateTime")
                    .setValue(ServerValue.TIMESTAMP)
            }
            .addOnCompleteListener {
                Log.v("QSO", it.exception?.toString() ?: "")
            }
    }

    companion object {
        val dbQSO = Firebase.database.getReference("qsos")
    }
}