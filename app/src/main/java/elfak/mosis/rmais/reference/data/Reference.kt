package elfak.mosis.rmais.reference.data

import android.view.View
import androidx.navigation.NavController
import com.google.firebase.database.Exclude
import elfak.mosis.rmais.reference.ReferenceMarker
import elfak.mosis.rmais.reference.ReferenceView
import elfak.mosis.rmais.reference.model.ReferencesViewModel
import java.util.Date

abstract class Reference (
    @get:Exclude
    open var key: String = "",
    open val name: String = "",
    open val reference: String = "",
    open val loc: String = "",
    open val lat: Double = 0.0,
    open val lon: Double = 0.0,
) {
    abstract var pinIcon: Int
    abstract var type: String

    var authorKey = ""
    var authorCallSign = ""

    @get:Exclude
    internal val referenceMarker = ReferenceMarker(this)

    var creationDateTime: Long = 0
    @get:Exclude
    val creationDate: Date
        get() = Date(creationDateTime)

    var lastActivationDateTime: Long = 0
    @get:Exclude
    val lastActivationDate: Date
        get() = Date(lastActivationDateTime)

    fun initViews(mView: View, referencesViewModel: ReferencesViewModel, navController: NavController) {
        val rv = ReferenceView(this)
        rv.initViews(mView, referencesViewModel, navController)
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return key == other
    }
}