package elfak.mosis.rmais.data

interface IReference {
    val name: String
    val reference: String
    val loc: String
    val lat: Double
    val lon: Double
    var pinIcon: Int
}