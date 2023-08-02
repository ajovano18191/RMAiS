package elfak.mosis.rmais.reference.filter

import elfak.mosis.rmais.reference.ReferenceDB
import elfak.mosis.rmais.reference.model.ReferencesViewModel

class NoFilter(override val referencesViewModel: ReferencesViewModel) : IFilter {
    init {
        referencesViewModel.referencesList.clear()
        ReferenceDB.dbRef.addChildEventListener(this)
    }
}