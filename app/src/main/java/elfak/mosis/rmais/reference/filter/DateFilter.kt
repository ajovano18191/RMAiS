package elfak.mosis.rmais.reference.filter

import elfak.mosis.rmais.reference.ReferenceDB
import elfak.mosis.rmais.reference.model.ReferencesViewModel

class DateFilter(
    field: String,
    var dateFrom: Long,
    var dateTo: Long,
    override val referencesViewModel: ReferencesViewModel
): IFilter {
    init {
        val dayInMS: Long = 24 * 3600 * 1000
        dateFrom = (dateFrom / dayInMS) * dayInMS
        dateTo = (dateTo / dayInMS) * dayInMS
        val dbRef = ReferenceDB.dbRef.orderByChild(field).startAt(dateFrom.toDouble()).endAt(dateTo.toDouble() + 24 * 3600 * 1000)
        dbRef.addChildEventListener(this)
    }
}