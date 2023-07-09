package elfak.mosis.rmais.model

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModel
import elfak.mosis.rmais.R
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

    companion object {
        fun updateView(mView: View, reference: IReference) {
            val titleText = mView.findViewById<TextView>(R.id.infowindow_title_text)
            titleText.text = "${reference.reference} ${reference.name}"

            val latText = mView.findViewById<TextView>(R.id.infowindow_latitude_text)
            latText.text = reference.lat.toString()

            val logText = mView.findViewById<TextView>(R.id.infowindow_longitude_text)
            logText.text = reference.log.toString()

            val locText = mView.findViewById<TextView>(R.id.infowindow_loc_text)
            locText.text = reference.loc

            // Here we are settings onclick listeners for the buttons in the layouts.

            val activateButton = mView.findViewById<ImageButton>(R.id.activate_button)
            val editButton = mView.findViewById<ImageButton>(R.id.edit_button)
            val deleteButton = mView.findViewById<ImageButton>(R.id.delete_button)

            editButton.setOnClickListener {
                // How to create a moveMarkerMapListener is not covered here.
                // Use the Map Listeners guide for this instead
                //mapView.addMapListener(MoveMarkerMapListener)
            }
            deleteButton.setOnClickListener {
                // Do Something

                // In order to delete the marker,
                // You would probably have to pass the "map class"
                // where the map was created,
                // along with an ID to reference the marker.

                // Using a HashMap to store markers would be useful here
                // so that the markers can be referenced by ID.

                // Once you get the marker,
                // you would do map.overlays.remove(marker)
            }

            // You can set an onClickListener on the InfoWindow itself.
            // This is so that you can close the InfoWindow once it has been tapped.

            // Instead, you could also close the InfoWindows when the map is pressed.
            // This is covered in the Map Listeners guide.

            mView.setOnClickListener {
            }
        }
    }
}