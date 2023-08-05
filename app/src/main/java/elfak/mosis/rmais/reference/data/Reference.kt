package elfak.mosis.rmais.reference.data

import android.view.View
import android.widget.TextView
import com.google.firebase.database.Exclude
import elfak.mosis.rmais.R
import elfak.mosis.rmais.reference.ReferenceMarker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

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

    @get:Exclude
    internal val referenceMarker = ReferenceMarker(this)

    var creationDateTime: Long = 0

    @get:Exclude
    val creationDate: Date
        get() = Date(creationDateTime)

    fun initViews(mView: View) {
        val creationDateText = mView.findViewById<TextView>(R.id.infowindow_creation_date_text)
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm z")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        creationDateText.text = dateFormat.format(creationDate)

        val titleText = mView.findViewById<TextView>(R.id.infowindow_title_text)
        titleText.text = "${reference} ${name}"

        val latText = mView.findViewById<TextView>(R.id.infowindow_latitude_text)
        latText.text = lat.toString()

        val logText = mView.findViewById<TextView>(R.id.infowindow_longitude_text)
        logText.text = lon.toString()

        val locText = mView.findViewById<TextView>(R.id.infowindow_loc_text)
        locText.text = loc
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return key == other
    }
}