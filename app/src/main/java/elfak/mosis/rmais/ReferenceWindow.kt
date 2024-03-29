package elfak.mosis.rmais

import android.widget.ImageButton
import androidx.navigation.findNavController
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.model.ReferencesViewModel
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class ReferenceWindow(private val mapView: MapView, private val reference: Reference, private val referencesViewModel: ReferencesViewModel) :
    InfoWindow(R.layout.info_window, mapView) {

    override fun onOpen(item: Any?) {
        closeAllInfoWindowsOn(mapView)

        reference.initViews(mView, referencesViewModel, mapView.findNavController())
        initDeleteButton()
        initCloseButton()
    }

    private fun initDeleteButton() {
        val deleteButton = mView.findViewById<ImageButton>(R.id.delete_button)
        deleteButton.setOnClickListener {
            referencesViewModel.deleteReference(reference)
            this.close()
        }
    }

    private fun initCloseButton() {
        val closeButton: ImageButton = mView.findViewById(R.id.close_button)
        closeButton.setOnClickListener {
            referencesViewModel.selectedReference = null
            this.close()
        }
    }

    override fun onClose() {
        super.close()
    }
}