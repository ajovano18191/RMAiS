package elfak.mosis.rmais.model

import androidx.lifecycle.ViewModel
import elfak.mosis.rmais.data.IReference
import elfak.mosis.rmais.data.SOTAReference
import elfak.mosis.rmais.data.WFFReference

class ReferencesViewModel : ViewModel() {
    private var _referencesList: ArrayList<IReference> = ArrayList<IReference>()
    var referencesList: ArrayList<IReference> = ArrayList<IReference>()
        get() {
            _referencesList.clear()
            _referencesList.add(WFFReference("Djerdap",     "YUFF-0001", "KN04XM", 44.52894211, 21.97586060))
            _referencesList.add(WFFReference("Fruska Gora", "YUFF-0002", "JN95UD", 45.25049973, 19.82729912))
            _referencesList.add(WFFReference("Kopaonik",    "YUFF-0003", "KN03JH", 43.31666565, 20.78343010))
            _referencesList.add(WFFReference("Sar-planina", "YUFF-0004", "KN04XM", 42.09, 20.97))
            _referencesList.add(SOTAReference("Tara",        "SOTA-0005", "JN93RU", 43.84695816, 19.45927238))
            return _referencesList
        }
}