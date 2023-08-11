package elfak.mosis.rmais

import android.content.pm.PackageManager
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.model.ReferencesViewModel
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class ReferenceWindow(private val mapView: MapView, private val reference: Reference, private val referencesViewModel: ReferencesViewModel) :
    InfoWindow(R.layout.info_window, mapView) {

    override fun onOpen(item: Any?) {
        // Following command
        closeAllInfoWindowsOn(mapView)

        reference.initViews(mView)

        initActivateButton()
        initEditButton()
        initDeleteButton()
        initCloseButton()
    }

    private fun initActivateButton() {
        val activateButton: ImageButton = mView.findViewById(R.id.activate_button)
        activateButton.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(mView.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mView.context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mView, "Morate odobriti pristup lokaciji", Snackbar.LENGTH_INDEFINITE).show()
            }
            else {
                referencesViewModel.selectedReference = reference
                mapView.findNavController().navigate(R.id.action_MapFragment_to_LogQSOFragment)
            }
        }
    }

    private fun initEditButton() {
        val editButton: ImageButton = mView.findViewById(R.id.edit_button)
        editButton.setOnClickListener {
            referencesViewModel.selectedReference = reference
            mapView.findNavController().navigate(R.id.action_MapFragment_to_AddOrEditFragment)
        }
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