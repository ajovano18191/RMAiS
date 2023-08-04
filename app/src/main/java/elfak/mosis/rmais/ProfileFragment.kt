package elfak.mosis.rmais

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.userProfileChangeRequest
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {

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

        findViews(view)
        initViews()
        initProfileImageView()
        initUpdateButton(view)
        initCancelButton(view)
    }

    private fun findViews(view: View) {
        profileImageView = view.findViewById(R.id.register_image_view)
        nameText = view.findViewById(R.id.register_name_text)
        surnameText = view.findViewById(R.id.register_surname_text)
        phoneNumberText = view.findViewById(R.id.register_phone_number_text)
    }

    private fun initViews() {
        val user = MainActivity.auth.currentUser!!
        var arr = user.displayName?.split(' ') ?: listOf("", "")
        if(arr.count() < 2) {
            arr = listOf("", "")
        }
        nameText.setText(arr[0])
        surnameText.setText(arr[1])
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
            updateProfileImage()
            updatePhoneNumber()
        }
    }

    private fun updateProfileImage() {
        profileImageView.isDrawingCacheEnabled = true
        profileImageView.buildDrawingCache()
        val bitmap = (profileImageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArr = baos.toByteArray()

        val imageReference = MainActivity.storage.child(MainActivity.auth.currentUser!!.uid)
        imageReference.putBytes(byteArr)
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
            }.addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener {
                    updateDisplayNameAndPhotoURI(it)
                }
            }.addOnCanceledListener {
                Toast.makeText(requireContext(), "Canceled profile image", Toast.LENGTH_LONG).show()
            }
    }

    private fun updateDisplayNameAndPhotoURI(photoURI: Uri) {
        val name = nameText.text.toString()
        val surname = surnameText.text.toString()
        val profileUpdates = userProfileChangeRequest {
            displayName = "$name $surname"
            photoUri = photoURI
        }

        MainActivity.auth.currentUser?.updateProfile(profileUpdates)
            ?.addOnFailureListener {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
            }
        ?.addOnCompleteListener  { task ->
            if(task.isSuccessful) {
                updatePhoneNumber()
            }
            else {
                Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }?.addOnCanceledListener {
                Toast.makeText(requireContext(), "Canceled display name", Toast.LENGTH_LONG).show()
        }
    }

    private fun updatePhoneNumber() {
        findNavController().popBackStack()
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