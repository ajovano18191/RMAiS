package elfak.mosis.rmais.reference.filter

import elfak.mosis.rmais.reference.ReferenceDB
import elfak.mosis.rmais.reference.model.ReferencesViewModel

class NoFilter(override val referencesViewModel: ReferencesViewModel) : IFilter {
    init {
        ReferenceDB.dbRef.addChildEventListener(this)
    }
}