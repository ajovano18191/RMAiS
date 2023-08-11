package elfak.mosis.rmais

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import elfak.mosis.rmais.databinding.ActivityMainBinding
import elfak.mosis.rmais.reference.FilterDialog
import elfak.mosis.rmais.reference.model.ReferencesViewModel
import org.osmdroid.util.GeoPoint

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val referencesViewModel: ReferencesViewModel by viewModels()

        binding.fab.hide()
        binding.fab.setOnClickListener {
            referencesViewModel.selectedReference = null
            if(navController.currentDestination?.id == R.id.ListFragment) {
                navController.navigate(R.id.action_ListFragment_to_AddOrEditFragment)
            }
            else if (navController.currentDestination?.id == R.id.MapFragment) {
                navController.navigate(R.id.action_MapFragment_to_AddOrEditFragment)
            }
        }

        val filterDialog = FilterDialog(referencesViewModel)
        binding.fabSearch.hide()
        binding.fabSearch.setOnClickListener {

            filterDialog.show(it.context)
        }

        subToUserLocation(referencesViewModel)
    }

    @SuppressLint("MissingPermission")
    fun subToUserLocation(referencesViewModel: ReferencesViewModel) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRequest = LocationRequest()
        locationRequest.interval = 60000
        locationRequest.fastestInterval = 60000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                if(p0.lastLocation != null) {
                    referencesViewModel.userLocation =
                        GeoPoint(p0.lastLocation!!.latitude, p0.lastLocation!!.longitude)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val navController = this.findNavController(R.id.nav_host_fragment_content_main)

        binding.fab.hide()
        binding.fabSearch.hide()

        when (item.itemId) {
            R.id.action_map -> {
                when (navController.currentDestination?.id) {
                    R.id.FirstFragment -> {
                        navController.navigate(R.id.action_FirstFragment_to_MapFragment)
                    }
                    R.id.ListFragment -> {
                        navController.navigate(R.id.action_ListFragment_to_MapFragment)
                    }
                    R.id.ListUsersFragment -> {
                        navController.navigate(R.id.action_ListUsersFragment_to_MapFragment)
                    }
                }
            }
            R.id.action_list -> {
                when (navController.currentDestination?.id) {
                    R.id.FirstFragment -> {
                        navController.navigate(R.id.action_FirstFragment_to_ListFragment)
                    }
                    R.id.MapFragment -> {
                        navController.navigate(R.id.action_MapFragment_to_ListFragment)
                    }
                    R.id.ListUsersFragment -> {
                        navController.navigate(R.id.action_ListUsersFragment_to_ListFragment)
                    }
                }
            }
            R.id.action_logout -> {
                FB.auth.signOut()
                val i = Intent(this, LoginActivity::class.java)
                i.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
                i.addFlags(FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
                finish()
            }
            R.id.action_profile -> {
                when (navController.currentDestination?.id) {
                    R.id.FirstFragment -> {
                        navController.navigate(R.id.action_FirstFragment_to_ProfileFragment)
                    }
                    R.id.MapFragment -> {
                        navController.navigate(R.id.action_MapFragment_to_ProfileFragment)
                    }
                    R.id.ListFragment -> {
                        navController.navigate(R.id.action_ListFragment_to_ProfileFragment)
                    }
                    R.id.ListUsersFragment -> {
                        navController.navigate(R.id.action_ListUsersFragment_to_ProfileFragment)
                    }
                }
            }
            R.id.action_list_users -> {
                when (navController.currentDestination?.id) {
                    R.id.FirstFragment -> {
                        navController.navigate(R.id.action_FirstFragment_to_ListUsersFragment)
                    }
                    R.id.MapFragment -> {
                        navController.navigate(R.id.action_MapFragment_to_ListUsersFragment)
                    }
                    R.id.ListFragment -> {
                        navController.navigate(R.id.action_ListFragment_to_ListUsersFragment)
                    }
                    R.id.LogQSOFragment -> {
                        navController.navigate(R.id.action_LogQSOFragment_to_ListUsersFragment)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = FB.auth.currentUser
        if (currentUser == null) {
            val i = Intent(this, LoginActivity::class.java)
            i.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
            i.addFlags(FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finish()
        }
    }

    override fun onStop() {
        if(signOut) {
            FB.auth.signOut()
        }
        signOut = true
        super.onStop()
    }

    companion object {
        var signOut = true
    }
}