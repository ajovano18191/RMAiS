package elfak.mosis.rmais.reference.filter

import elfak.mosis.rmais.reference.ReferenceDB
import elfak.mosis.rmais.reference.model.ReferencesViewModel

class DateFilter(
    field: String,
    dateFrom: Long,
    dateTo: Long,
    override val referencesViewModel: ReferencesViewModel
): IFilter {
    init {
        val dbRef = ReferenceDB.dbRef.orderByChild(field).startAt(dateFrom.toDouble()).endAt(dateTo.toDouble() + 24 * 3600 * 1000)
        dbRef.addChildEventListener(this)
    }
}