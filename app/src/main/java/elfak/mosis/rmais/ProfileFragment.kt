package elfak.mosis.rmais

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.lang.Exception

class ProfileFragment : Fragment() {

    private lateinit var navController: NavController

    private lateinit var profileImageView: ImageView
    private lateinit var callSignText: EditText
    private lateinit var passwordText: EditText
    private lateinit var passwordAgainText: EditText
    private lateinit var nameText: EditText
    private lateinit var surnameText: EditText
    private lateinit var phoneNumberText: EditText

    private lateinit var updateButton: Button
    private val CAMERA_REQUEST = 1888

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        findViews(view)
        initViews()
        initProfileImageView()
        initUpdateButton(view)
        initCancelButton(view)
    }

    private fun findViews(view: View) {
        callSignText = view.findViewById(R.id.register_call_sign_text)
        profileImageView = view.findViewById(R.id.register_image_view)
        nameText = view.findViewById(R.id.register_name_text)
        surnameText = view.findViewById(R.id.register_surname_text)
        phoneNumberText = view.findViewById(R.id.register_phone_number_text)
    }

    private fun initViews() {
        val user = MainActivity.auth.currentUser!!

        callSignText.setText(user.email?.substringBefore('@')?.uppercase())

        var arr = user.displayName?.split(' ') ?: listOf("", "")
        if(arr.count() < 2) {
            arr = listOf("", "")
        }
        nameText.setText(arr[0])
        surnameText.setText(arr[1])

        MainActivity.storage.child("${user.uid}.jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
            profileImageView.setImageBitmap(bmp)
        }
    }

    private fun initProfileImageView() {
        profileImageView.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }
    }

    private fun initUpdateButton(view: View) {
        val updateButton: Button = view.findViewById(R.id.profile_update_button)
        updateButton.setOnClickListener {
            try {
                CoroutineScope(Dispatchers.Main).launch {
                    updateUser()
                }
            } catch (e: Exception) {
                Snackbar.make(requireView(), e.toString(), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun updateUser() {
        try {
            updateCallSign().await()
            updateProfileImage().await()
            updateDisplayName().await()
            // updatePhoneNumber().await()
            navController.popBackStack()
        }
        catch (e: Exception) {
            Snackbar.make(requireView(), e.toString(), Snackbar.LENGTH_INDEFINITE).show()
        }
    }

    private fun updateCallSign(): Task<Void> {
        val callSign = callSignText.text.toString()
        return MainActivity.auth.currentUser!!.updateEmail("$callSign@gmail.com")
    }

    private fun updateProfileImage(): UploadTask {
        profileImageView.isDrawingCacheEnabled = true
        profileImageView.buildDrawingCache()
        val bitmap = (profileImageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArr = baos.toByteArray()

        val imageReference = MainActivity.storage.child("${MainActivity.auth.currentUser!!.uid}.jpg")
        return imageReference.putBytes(byteArr)
    }

    private fun updateDisplayName(): Task<Void> {
        val name = nameText.text.toString()
        val surname = surnameText.text.toString()
        val profileUpdates = userProfileChangeRequest {
            displayName = "$name $surname"
        }

        return MainActivity.auth.currentUser!!.updateProfile(profileUpdates)
    }

    private fun updatePhoneNumber() {
        Snackbar.make(requireView(), "User was successfully updated.", Snackbar.LENGTH_SHORT).show()
    }

    private fun initCancelButton(view: View) {
        val cancelButton: Button = view.findViewById(R.id.profile_cancel_button)
        cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val photo = data?.extras!!["data"] as Bitmap?
            profileImageView.setImageBitmap(photo)
        }
    }
}