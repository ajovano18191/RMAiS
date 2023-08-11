package elfak.mosis.rmais

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class FB {
    companion object {
        private val x = initEmulators()
        val auth: FirebaseAuth = Firebase.auth
        private val db: FirebaseDatabase = Firebase.database
        val usersDB: DatabaseReference = db.getReference("users")
        val QSOsDB: DatabaseReference = db.getReference("qsos")
        val referencesDB: DatabaseReference = db.getReference("references")
        private val storage: FirebaseStorage = Firebase.storage
        val profileImages: StorageReference = storage.reference.child("profile_images")

        private fun initEmulators() {
            Firebase.database.useEmulator("10.17.2.42", 9000)
            Firebase.auth.useEmulator("10.17.2.42", 9099)
            Firebase.storage.useEmulator("10.17.2.42", 9199)
        }
    }
}