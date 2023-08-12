package elfak.mosis.rmais.reference.model

import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import elfak.mosis.rmais.reference.ReferenceDB
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.filter.RadiusFilter
import org.osmdroid.util.GeoPoint
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class ReferencesViewModel : ViewModel() {
    var userLocation = GeoPoint(500.0, 500.0)
        set(value) {
            if(referenceDB.filter is RadiusFilter) {
                if(value.latitude != field.latitude || value.longitude != field.longitude) {
                    referenceDB.filter = RadiusFilter((referenceDB.filter as RadiusFilter).radius, this)
                }
            }
            field = value
        }

    private val _referencesList = ArrayList<Reference>()
    val referencesList: ArrayList<Reference>
        get() = _referencesList

    var arrayAdapter: ArrayAdapter<Reference>? = null

    val referenceDB = ReferenceDB(this)

    fun addOrUpdateReference(reference: Reference): Reference {
        referenceDB.addOrUpdate(reference)
        arrayAdapter?.notifyDataSetChanged()
        return reference
    }

    fun deleteReference(reference: Reference) {
        referenceDB.delete(reference)
        arrayAdapter?.notifyDataSetChanged()
    }

    private var selectedReferenceIndex: Int = -1
    var selectedReference: Reference?
        get() {
            if(selectedReferenceIndex >= 0) {
                return _referencesList[selectedReferenceIndex]
            }
            return null
        }
        set(value) {
            selectedReferenceIndex = _referencesList.indexOf(value)
        }

    fun distanceFromUser(ref: Reference): Double {
        val testGP = userLocation
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