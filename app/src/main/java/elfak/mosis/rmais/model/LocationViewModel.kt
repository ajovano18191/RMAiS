package elfak.mosis.rmais.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.floor

class LocationViewModel : ViewModel() {
    private val _lon = MutableLiveData(0.0)
    val lon: LiveData<Double>
        get() = _lon

    private val _lat = MutableLiveData(0.0)
    val lat: LiveData<Double>
        get() = _lat

    var setLocation: Boolean = false

    fun setLocation(lon: Double, lat: Double) {
        _lon.value = lon
        _lat.value = lat
        setLocation = false
    }

    fun gcs2QTH(lat: Double, lon: Double): String {
        var lonCeo: Int = floor(lon).toInt()
        lonCeo += 180
        lonCeo /= 2

        var latCeo: Int = floor(lat).toInt()
        latCeo += 90

        val firstFieldLetter: Int = lonCeo / 10
        val secondFieldLetter: Int = latCeo / 10
        val cFFL: Char = firstFieldLetter.toChar() + 'A'.code
        val cSFL: Char = secondFieldLetter.toChar() + 'A'.code

        val firstDigit: Int = lonCeo % 10
        val secondDigit: Int = latCeo % 10

        val firstSubsquareLetter: Int = floor(((lon + 180.0) - floor((lon + 180.0) / 2) * 2) * 100 / (200.0 / 24.0)).toInt()
        val secondSubsquareLetter: Int = floor(((lat + 90.0) - floor(lat + 90.0)) * 100 / (100.0 / 24.0)).toInt()
        val cFSsL: Char = firstSubsquareLetter.toChar() + 'a'.code
        val cSSsL: Char = secondSubsquareLetter.toChar() + 'a'.code

        return "$cFFL$cSFL$firstDigit$secondDigit$cFSsL$cSSsL"
    }
}