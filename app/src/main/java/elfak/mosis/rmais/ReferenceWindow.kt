package elfak.mosis.rmais

import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import elfak.mosis.rmais.data.IReference
import elfak.mosis.rmais.model.ReferencesViewModel
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class ReferenceWindow(private val mapView: MapView, private val reference: IReference) :
    InfoWindow(R.layout.info_window, mapView) {

    override fun onOpen(item: Any?) {
        // Following command
        closeAllInfoWindowsOn(mapView)

        ReferencesViewModel.updateView(mView, reference)
    }

    override fun onClose() {

    }
}