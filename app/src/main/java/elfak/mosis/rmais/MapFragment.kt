package elfak.mosis.rmais

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import elfak.mosis.rmais.model.ReferencesViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment() {
    lateinit var map: MapView
    private val referencesViewModel: ReferencesViewModel by activityViewModels()

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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var ctx: Context? = activity?.applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences((ctx!!)))
        map = requireView().findViewById<MapView>(R.id.map)
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
            marker.position = GeoPoint(reference.lat, reference.log)
            marker.title = reference.toString()
            marker.icon = resources.getDrawable(reference.pinIcon)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            marker.infoWindow = ReferenceWindow(map, reference, referencesViewModel)
            map.overlays.add(marker)
        }
        map.invalidate()

    }

    private fun setupMap() {
        map.controller.setZoom(10.0)
        var startPoint = GeoPoint(43.753629, 20.090579)
        if(referencesViewModel.selectedReference != null) {
            startPoint = GeoPoint(referencesViewModel.selectedReference!!.lat, referencesViewModel.selectedReference!!.log)
        }
        else {
            startPoint = GeoPoint(43.753629, 20.090579)
        }
        map.controller.animateTo(startPoint)

    }

    private fun setMyLocationOverlay() {
        var myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(activity), map)
        myLocationOverlay.enableMyLocation()
        map.overlays.add(myLocationOverlay)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if(isGranted) {
                setMyLocationOverlay()
            }
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