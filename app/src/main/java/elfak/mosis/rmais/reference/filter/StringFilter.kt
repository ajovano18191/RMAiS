package elfak.mosis.rmais.reference.filter

import elfak.mosis.rmais.FB
import elfak.mosis.rmais.reference.model.ReferencesViewModel

class StringFilter(
    field: String,
    value: String,
    override val referencesViewModel: ReferencesViewModel) : IFilter
{
    init {
        val dbRef = FB.referencesDB.orderByChild(field).startAt(value).endAt(value + "z")
        dbRef.addChildEventListener(this)
    }
}