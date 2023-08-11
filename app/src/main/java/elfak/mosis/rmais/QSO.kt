package elfak.mosis.rmais

import com.google.firebase.database.Exclude
import com.google.firebase.database.ServerValue
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
    val userKey = FB.currentUser!!.uid
    val userCallSign = FB.userCallSign
    val referenceKey = reference.key
    val referenceToString = reference.toString()

    init {
        band = band.substringBefore(' ').substringBefore('m')
    }

    fun log() {
        FB.QSOsDB.push().setValue(this)
            .addOnSuccessListener {
                FB.referencesDB
                    .child(reference.key)
                    .child("lastActivationDateTime")
                    .setValue(ServerValue.TIMESTAMP)
                FB.increaseScore(1)
            }
    }
}