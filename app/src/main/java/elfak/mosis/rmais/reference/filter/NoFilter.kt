package elfak.mosis.rmais.reference.filter

import elfak.mosis.rmais.FB
import elfak.mosis.rmais.reference.model.ReferencesViewModel

class NoFilter(override val referencesViewModel: ReferencesViewModel) : IFilter {
    init {
        FB.referencesDB.addChildEventListener(this)
    }
}