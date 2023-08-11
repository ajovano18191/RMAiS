package elfak.mosis.rmais

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ListUsersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usersListView: ListView = view.findViewById(R.id.list_users_list)
        usersDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items: List<Array<String>> = dataSnapshot.children.map {
                    val hashMap = it.value as HashMap<*, *>
                    arrayOf(
                        hashMap["callSign"].toString(),
                        hashMap["score"].toString()
                    )
                }.reversed()

                usersListView.adapter = GridAdapter(requireContext(), items)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Users", "loadUsers:onCancelled", databaseError.toException())
            }
        })
    }

    companion object {
        val usersDB: Query = Firebase.database.getReference("users").orderByChild("score")
    }
}