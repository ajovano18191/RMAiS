package elfak.mosis.rmais.data

import elfak.mosis.rmais.R

data class WFFReference(
    override val name: String,
    override val reference: String,
    override val loc: String,
    override val lat: Double,
    override val lon: Double,
    ) : IReference {
    override var pinIcon: Int = R.drawable.aleksa_baseline_location_on_24_wff

    override fun toString(): String {
        return "$reference $name"
    }
}
