package elfak.mosis.rmais.reference.filter

import com.google.firebase.database.DataSnapshot
import elfak.mosis.rmais.reference.ReferenceDB
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.model.ReferencesViewModel
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class RadiusFilter(val radius: Double, override val referencesViewModel: ReferencesViewModel): IFilter {
    init {
        ReferenceDB.dbRef.addChildEventListener(this)
    }

    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        val ref: Reference? = getTypedReference(snapshot)
        if(ref != null) {
            if(calculateDistance(ref) < radius) {
                addReference(ref)
            }
            else {
                ref.referenceMarker.remove()
            }
            referencesViewModel.arrayAdapter?.notifyDataSetChanged()
        }
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        val ref: Reference? = getTypedReference(snapshot)
        if(ref != null) {
            if(calculateDistance(ref) < radius) {
                replaceReference(ref)
            }
            else {
                super.onChildRemoved(snapshot)
            }
        }
    }

    private fun calculateDistance(ref: Reference): Double {
        val testGP = referencesViewModel.userLocation
        val dLat = Math.toRadians(ref.lat - testGP.latitude)
        val dLon = Math.toRadians(ref.lon - testGP.longitude)
        val originLat = Math.toRadians(testGP.latitude)
        val destinationLat = Math.toRadians(ref.lat)

        val a = sin(dLat / 2).pow(2.toDouble()) + sin(dLon / 2).pow(2.toDouble()) * cos(originLat) * cos(destinationLat)
        val c = 2 * asin(sqrt(a))

        val radius = 6372.8

        return radius * c
    }
}