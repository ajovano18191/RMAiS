package elfak.mosis.rmais

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
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
import android.widget.Toast
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var callSignText: EditText
    private lateinit var passwordText: EditText
    private lateinit var passwordAgainText: EditText
    private lateinit var nameText: EditText
    private lateinit var surnameText: EditText
    private lateinit var phoneNumberText: EditText
    private lateinit var registerButton: Button
    private val CAMERA_REQUEST = 1888

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImageView = view.findViewById(R.id.register_image_view)
        profileImageView.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }

        findViews(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val photo = data?.extras!!["data"] as Bitmap?
            profileImageView.setImageBitmap(photo)
            // Get the data from an ImageView as bytes
            profileImageView.isDrawingCacheEnabled = true
            profileImageView.buildDrawingCache()
            val bitmap = (profileImageView.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val byteArr = baos.toByteArray()

            MainActivity.storage.putBytes(byteArr)
                .addOnFailureListener {
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
                }.addOnSuccessListener {
                    Toast.makeText(requireContext(), "Slika uploadovana uspesno", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun findViews(view: View) {
        callSignText = view.findViewById(R.id.register_call_sign_text)
        passwordText = view.findViewById(R.id.register_password_text)
        passwordAgainText = view.findViewById(R.id.register_password_again_text)
        nameText = view.findViewById(R.id.register_name_text)
        surnameText = view.findViewById(R.id.register_surname_text)
        phoneNumberText = view.findViewById(R.id.register_phone_number_text)
    }
}