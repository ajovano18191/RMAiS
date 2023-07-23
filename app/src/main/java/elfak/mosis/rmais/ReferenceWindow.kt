package elfak.mosis.rmais

import android.media.Image
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import elfak.mosis.rmais.data.IReference
import elfak.mosis.rmais.model.ReferencesViewModel
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class ReferenceWindow(private val mapView: MapView, private val reference: IReference, private val referencesViewModel: ReferencesViewModel) :
    InfoWindow(R.layout.info_window, mapView) {

    override fun onOpen(item: Any?) {
        // Following command
        closeAllInfoWindowsOn(mapView)

        ReferencesViewModel.updateView(mView, reference)

        val editButton: ImageButton = mView.findViewById<ImageButton>(R.id.edit_button)
        editButton.setOnClickListener {
            referencesViewModel.selectedReference = reference
            mapView.findNavController().navigate(R.id.action_MapFragment_to_AddOrEditFragment)
        }

        val closeButton: ImageButton = mView.findViewById<ImageButton>(R.id.close_button)
        closeButton.setOnClickListener {
            referencesViewModel.selectedReference = null
            this.close()
        }
    }

    override fun onClose() {
        super.close()
    }
}