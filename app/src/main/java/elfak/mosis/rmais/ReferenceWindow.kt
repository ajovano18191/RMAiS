package elfak.mosis.rmais

import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import elfak.mosis.rmais.data.IReference
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class ReferenceWindow(private val mapView: MapView, private val reference: IReference) :
    InfoWindow(R.layout.info_window, mapView) {

    override fun onOpen(item: Any?) {
        // Following command
        closeAllInfoWindowsOn(mapView)

        val titleText = mView.findViewById<TextView>(R.id.infowindow_title_text)
        titleText.text = "${reference.reference} ${reference.name}"

        val latText= mView.findViewById<TextView>(R.id.infowindow_latitude_text)
        latText.text = reference.lat.toString()

        val logText= mView.findViewById<TextView>(R.id.infowindow_longitude_text)
        logText.text = reference.log.toString()

        val locText= mView.findViewById<TextView>(R.id.infowindow_loc_text)
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
            close()
        }
    }

    override fun onClose() {

    }
}