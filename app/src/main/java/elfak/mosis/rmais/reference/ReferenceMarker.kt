package elfak.mosis.rmais.reference

import androidx.core.content.res.ResourcesCompat
import elfak.mosis.rmais.MapFragment
import elfak.mosis.rmais.ReferenceWindow
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.model.ReferencesViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class ReferenceMarker(private val reference: Reference) {
    private var marker: Marker? = null

    fun create(referencesViewModel: ReferencesViewModel, infoWindowIsOpened: Boolean = false) {
        if(MapFragment.mapView != null) {
            marker = Marker(MapFragment.mapView)
            MapFragment.mapView?.overlays?.add(marker)
            update(referencesViewModel, infoWindowIsOpened)
        }
        else {
            marker = null
        }
    }

    private fun update(referencesViewModel: ReferencesViewModel, infoWindowIsOpened: Boolean = false) {
        marker?.id = reference.key
        marker?.position = GeoPoint(reference.lat, reference.lon)
        marker?.title = toString()
        marker?.icon = ResourcesCompat.getDrawable(MapFragment.mapView!!.resources, reference.pinIcon, null)
        marker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        marker?.infoWindow = ReferenceWindow(MapFragment.mapView!!, reference, referencesViewModel)
        if(reference.key == referencesViewModel.selectedReference?.key) {
            marker?.showInfoWindow()
        }
        if(infoWindowIsOpened) {
            marker?.showInfoWindow()
        }
        MapFragment.mapView?.invalidate()
    }

    fun remove(): Boolean {
        var isInfoWindowOpen = false
        if(MapFragment.mapView != null) {
            for(overlay in MapFragment.mapView!!.overlays) {
                if((overlay as? Marker)?.id == reference.key) {
                    isInfoWindowOpen = overlay.isInfoWindowOpen
                    overlay.closeInfoWindow()
                    MapFragment.mapView?.overlays?.remove(overlay)
                    MapFragment.mapView?.invalidate()
                    //break
                }
            }
        }
        marker = null
        return isInfoWindowOpen
    }
}