package elfak.mosis.rmais.reference.data

import elfak.mosis.rmais.R

data class WFFReference(
    @Transient
    override var key: String = "",
    override val name: String = "",
    override val reference: String = "",
    override val loc: String = "",
    override val lat: Double = 0.0,
    override val lon: Double = 0.0,
) : Reference(key, name, reference, loc, lat, lon) {
    @Transient
    override var pinIcon: Int = R.drawable.aleksa_baseline_location_on_24_wff
    override var type: String = "WFF"
    override fun toString(): String {
        return "$reference $name"
    }
}
