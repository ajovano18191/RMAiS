package elfak.mosis.rmais

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.model.ReferencesViewModel


class ListFragment : Fragment() {
    private val referencesViewModel: ReferencesViewModel by activityViewModels()

    private lateinit var mView: View
    private lateinit var alertDialog: AlertDialog
    private lateinit var referencesList: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.showHideFabButtons(true)

        referencesViewModel.selectedReference = null

        initReferencesList(view)
        initSearchView(view)
    }

    private fun initSearchView(view: View) {
        val searchView = view.findViewById<SearchView>(R.id.list_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                var arrAdapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, referencesViewModel.referencesList)

                if(newText.isNotEmpty()) {
                    val filteredReferences = referencesViewModel.referencesList.filter {
                        it.toString().contains(newText)
                    }
                    arrAdapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, filteredReferences)
                }
                else {
                    referencesViewModel.arrayAdapter = arrAdapter
                }

                referencesList.adapter = arrAdapter
                return true
            }
        })
    }

    private fun initReferencesList(view: View) {
        referencesList = requireView().findViewById(R.id.list)
        val arrAdapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, referencesViewModel.referencesList)
        referencesViewModel.arrayAdapter = arrAdapter
        referencesList.adapter = arrAdapter

        referencesList.onItemClickListener =
            AdapterView.OnItemClickListener { p0, _, p2, _ ->
                val reference: Reference = p0?.adapter?.getItem(p2) as Reference

                mView = inflateView()
                alertDialog = AlertDialog.Builder(context)
                    .setView(mView)
                    .show()

                reference.initViews(mView)

                initMapButton(reference)
                initActivateButton(reference)
                initEditButton(reference)
                initDeleteButton(reference)
                initCloseButton()
            }
    }

    private fun inflateView(): View {
        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflater.inflate(R.layout.info_window, null)
    }

    private fun initMapButton(reference: Reference) {
        val mapButton = mView.findViewById<ImageButton>(R.id.map_button)
        mapButton.visibility = View.VISIBLE
        mapButton.setOnClickListener {
            referencesViewModel.selectedReference = reference
            alertDialog.cancel()
            findNavController().navigate(R.id.action_ListFragment_to_MapFragment)
        }
    }

    private fun initActivateButton(reference: Reference) {
        val activateButton: ImageButton = mView.findViewById(R.id.activate_button)
        activateButton.setOnClickListener {
            alertDialog.cancel()
            if(ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(requireView(), "Morate odobriti pristup lokaciji", Snackbar.LENGTH_INDEFINITE).show()
            }
            else {
                referencesViewModel.selectedReference = reference
                findNavController().navigate(R.id.action_ListFragment_to_LogQSOFragment)
            }
        }
    }

    private fun initEditButton(reference: Reference) {
        val editButton = mView.findViewById<ImageButton>(R.id.edit_button)
        editButton.setOnClickListener {
            referencesViewModel.selectedReference = reference
            alertDialog.cancel()
            findNavController().navigate(R.id.action_ListFragment_to_AddOrEditFragment)
        }
    }

    private fun initDeleteButton(reference: Reference) {
        val deleteButton = mView.findViewById<ImageButton>(R.id.delete_button)
        deleteButton.setOnClickListener {
            referencesViewModel.deleteReference(reference)
            alertDialog.cancel()
        }
    }

    private fun initCloseButton() {
        val closeButton: ImageButton = mView.findViewById(R.id.close_button)
        closeButton.setOnClickListener {
            alertDialog.cancel()
        }
    }
}