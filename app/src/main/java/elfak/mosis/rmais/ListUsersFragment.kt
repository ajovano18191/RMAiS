package elfak.mosis.rmais

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ListUsersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.showHideFabButtons(false)

        val usersListView: ListView = view.findViewById(R.id.list_users_list)
        FB.usersDB.orderByChild("score").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usersListView.adapter = GridAdapter(requireContext(), dataSnapshotToList(dataSnapshot))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Users", "loadUsers:onCancelled", databaseError.toException())
            }
        })
    }

    private fun dataSnapshotToList(dataSnapshot: DataSnapshot): List<Array<String>> {
        return dataSnapshot.children.map {
            val hashMap = it.value as HashMap<*, *>
            arrayOf(
                hashMap["callSign"].toString(),
                hashMap["score"].toString()
            )
        }.reversed()
    }
}