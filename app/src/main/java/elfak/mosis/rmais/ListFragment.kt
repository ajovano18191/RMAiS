package elfak.mosis.rmais

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import elfak.mosis.rmais.data.IReference
import elfak.mosis.rmais.model.ReferencesViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {
    private val referencesViewModel: ReferencesViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val referencesList: ListView = requireView().findViewById<ListView>(R.id.list)
        referencesList.adapter = ArrayAdapter<IReference>(view.context, android.R.layout.simple_list_item_1, referencesViewModel.referencesList)

        referencesList.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                val inflater =
                    requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

                var reference: IReference = p0?.adapter?.getItem(p2) as IReference

                val mView = inflater.inflate(R.layout.info_window, null)
                ReferencesViewModel.updateView(mView, reference)
                var x = AlertDialog.Builder(context)
                    .setView(mView)
                    .show();
            }
    }
}

/*
                val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                val alertDialog: AlertDialog = dialogBuilder.create()
                alertDialog.setContentView(inflater.inflate(R.layout.info_window, null))
                val editText = alertDialog.findViewById(R.id.infoview_title_text) as EditText
                editText.setText("test label")
                alertDialog.show()
 */