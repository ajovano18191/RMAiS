package elfak.mosis.rmais.reference

import android.content.pm.PackageManager
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.android.material.snackbar.Snackbar
import elfak.mosis.rmais.R
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.model.ReferencesViewModel
import java.text.SimpleDateFormat
import java.util.TimeZone

class ReferenceView(private val reference: Reference) {
    private lateinit var mView: View
    private lateinit var referencesViewModel: ReferencesViewModel
    private lateinit var navController: NavController

    fun initViews(mView: View, referencesViewModel: ReferencesViewModel, navController: NavController) {
        this.mView = mView
        this.referencesViewModel = referencesViewModel
        this.navController = navController

        val authorText = mView.findViewById<TextView>(R.id.infowindow_author_text)
        authorText.text = reference.authorCallSign

        val creationDateText = mView.findViewById<TextView>(R.id.infowindow_creation_date_text)
        creationDateText.text = dateToString(reference.creationDateTime)

        val lastActivationDateText = mView.findViewById<TextView>(R.id.infowindow_last_activation_date_text)
        lastActivationDateText.text = dateToString(reference.lastActivationDateTime)

        val titleText = mView.findViewById<TextView>(R.id.infowindow_title_text)
        titleText.text = "${reference.reference} ${reference.name}"

        val latText = mView.findViewById<TextView>(R.id.infowindow_latitude_text)
        latText.text = reference.lat.toString()

        val logText = mView.findViewById<TextView>(R.id.infowindow_longitude_text)
        logText.text = reference.lon.toString()

        val locText = mView.findViewById<TextView>(R.id.infowindow_loc_text)
        locText.text = reference.loc

        initMapButton()
        initActivationButton()
        initEditButton()
    }

    private fun dateToString(date: Long): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm z")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(date)
    }

    private fun initMapButton() {
        val mapButton = mView.findViewById<ImageButton>(R.id.map_button)
        mapButton.visibility = View.VISIBLE
        mapButton.setOnClickListener {
            referencesViewModel.selectedReference = reference
            mView.visibility = View.GONE
            navController.navigate(R.id.action_ListFragment_to_MapFragment)
        }
    }

    private fun initActivationButton() {
        val activateButton: ImageButton = mView.findViewById(R.id.activate_button)
        activateButton.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(mView.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mView.context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mView, "Morate odobriti pristup lokaciji", Snackbar.LENGTH_INDEFINITE).show()
            }
            else {
                referencesViewModel.selectedReference = reference
                if(navController.currentDestination?.id == R.id.MapFragment) {
                    navController.navigate(R.id.action_MapFragment_to_LogQSOFragment)
                }
                else if(navController.currentDestination?.id == R.id.ListFragment) {
                    navController.navigate(R.id.action_ListFragment_to_LogQSOFragment)
                }
            }
        }
    }

    private fun initEditButton() {
        val editButton: ImageButton = mView.findViewById(R.id.edit_button)
        editButton.setOnClickListener {
            referencesViewModel.selectedReference = reference
            if(navController.currentDestination?.id == R.id.MapFragment) {
                navController.navigate(R.id.action_MapFragment_to_AddOrEditFragment)
            }
            else if(navController.currentDestination?.id == R.id.ListFragment) {
                navController.navigate(R.id.action_ListFragment_to_AddOrEditFragment)
            }
        }
    }
}