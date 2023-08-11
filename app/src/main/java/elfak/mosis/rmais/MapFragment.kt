package elfak.mosis.rmais

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import elfak.mosis.rmais.reference.model.LocationViewModel
import elfak.mosis.rmais.reference.model.ReferencesViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapFragment : Fragment() {
    private lateinit var map: MapView
    private val referencesViewModel: ReferencesViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.showHideFabButtons(true)

        initMap()
        checkPermissions()

        showReferencesOnMap()
    }

    private fun initMap() {
        val ctx: Context? = activity?.applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences((ctx!!)))
        map = requireView().findViewById(R.id.map)
        map.overlays.clear()
        map.setMultiTouchControls(true)
        mapView = map
    }

    private fun checkPermissions() {
        if(ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        else {
            setupMap()
        }
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

    private fun setupMap() {
        map.controller.setZoom(8.0)

        // val overlay = LatLonGridlineOverlay2()
        // map.overlays.add(overlay)

        if(locationViewModel.setLocation) {
            setOnMapClickOverlay()
        }

        val startPoint = if(referencesViewModel.selectedReference == null) {
            setMyLocationOverlay()
        } else {
            GeoPoint(referencesViewModel.selectedReference!!.lat, referencesViewModel.selectedReference!!.lon)
        }
        map.controller.animateTo(startPoint)
    }

    private fun setMyLocationOverlay(): GeoPoint {
        val provider = GpsMyLocationProvider(activity)
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER)

        val myLocationOverlay = MyLocationNewOverlay(provider, map)
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.enableFollowLocation()

        var userLocation = GeoPoint(43.753629, 20.090579)
        myLocationOverlay.runOnFirstFix {
            userLocation = GeoPoint(myLocationOverlay.lastFix.latitude, myLocationOverlay.lastFix.longitude)
        }
        map.overlays.add(myLocationOverlay)

        return userLocation
    }

    private fun setOnMapClickOverlay() {
        val receive = object:MapEventsReceiver{
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                locationViewModel.setLocation(p!!.longitude, p.latitude)
                findNavController().popBackStack()
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }
        val overlayEvents = MapEventsOverlay(receive)
        map.overlays.add(overlayEvents)
    }

    private fun showReferencesOnMap() {
        for(ref in referencesViewModel.referencesList) {
            ref.referenceMarker.create(referencesViewModel)
        }
        map.invalidate()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDestroyView() {
        mapView = null
        super.onDestroyView()
    }

    companion object {
        var mapView: MapView? = null
    }
}