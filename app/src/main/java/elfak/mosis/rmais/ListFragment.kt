package elfak.mosis.rmais

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import elfak.mosis.rmais.reference.data.Reference
import elfak.mosis.rmais.reference.model.ReferencesViewModel


class ListFragment : Fragment() {
    private val referencesViewModel: ReferencesViewModel by activityViewModels()

    private lateinit var mView: View
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab: FloatingActionButton = (requireView().parent.parent.parent as View).findViewById(R.id.fab)
        fab.show()

        referencesViewModel.selectedReference = null

        initReferencesList(view)
    }

    private fun initReferencesList(view: View) {
        val referencesList: ListView = requireView().findViewById(R.id.list)
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