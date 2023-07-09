package elfak.mosis.rmais.data

import elfak.mosis.rmais.R

data class SOTAReference(
    override val name: String,
    override val reference: String,
    override val loc: String,
    override val lat: Double,
    override val log: Double,
) : IReference {
    override var pinIcon: Int = R.drawable.aleksa_baseline_location_on_24_sota
}
