package elfak.mosis.rmais

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import elfak.mosis.rmais.model.LocationViewModel
import elfak.mosis.rmais.model.ReferencesViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.lang.String


/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment() {
    lateinit var map: MapView
    private val referencesViewModel: ReferencesViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var fab: FloatingActionButton = (requireView().parent.parent.parent as View).findViewById<FloatingActionButton>(R.id.fab)
        fab.show()

        var ctx: Context? = activity?.applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences((ctx!!)))
        map = requireView().findViewById<MapView>(R.id.map)
        map.overlays.clear()
        map.setMultiTouchControls(true)
        if(ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        else {
            setupMap()
        }

        for(reference in referencesViewModel.referencesList) {
            var marker = Marker(map)
            marker.position = GeoPoint(reference.lat, reference.lon)
            marker.title = reference.toString()
            marker.icon = resources.getDrawable(reference.pinIcon)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            marker.infoWindow = ReferenceWindow(map, reference, referencesViewModel)
            map.overlays.add(marker)
        }
        map.invalidate()

    }

    private fun setupMap() {
        map.controller.setZoom(15.0)

        // val overlay = LatLonGridlineOverlay2()
        // map.overlays.add(overlay)

        if(locationViewModel.setLocation) {
            setOnMapClickOverlay()
        }
        var startPoint: GeoPoint = GeoPoint(43.753629, 20.090579)
        if(referencesViewModel.selectedReference == null) {
            startPoint = setMyLocationOverlay()
        }
        else {
            startPoint = GeoPoint(referencesViewModel.selectedReference!!.lat, referencesViewModel.selectedReference!!.lon)
        }
        map.controller.animateTo(startPoint)
    }

    private fun setMyLocationOverlay(): GeoPoint {
        val provider = GpsMyLocationProvider(activity)
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER)
        var myLocationOverlay = MyLocationNewOverlay(provider, map)
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.enableFollowLocation()
        var gp: GeoPoint = GeoPoint(0.0, 0.0)
        myLocationOverlay.runOnFirstFix(Runnable {
            gp = GeoPoint(myLocationOverlay.lastFix.latitude, myLocationOverlay.lastFix.longitude)
        })
        map.overlays.add(myLocationOverlay)
        return gp
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if(isGranted) {
                setMyLocationOverlay()
                setOnMapClickOverlay()
            }
    }

    private fun setOnMapClickOverlay() {
        var receive = object:MapEventsReceiver{
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                locationViewModel.setLocation(p!!.longitude, p!!.latitude)
                findNavController().popBackStack()
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }
        var overlayEvents = MapEventsOverlay(receive)
        map.overlays.add(overlayEvents)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

}