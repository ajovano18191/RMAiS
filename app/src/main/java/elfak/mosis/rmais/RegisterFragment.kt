package elfak.mosis.rmais

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.userProfileChangeRequest


class RegisterFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var callSignText: EditText
    private lateinit var passwordText: EditText
    private lateinit var passwordAgainText: EditText
    private lateinit var nameText: EditText
    private lateinit var surnameText: EditText
    private lateinit var phoneNumberText: EditText
    private lateinit var registerButton: Button
    private val CAMERA_REQUEST = 1888

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImageView = view.findViewById(R.id.register_image_view)
        profileImageView.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }

        findViews(view)
        initRegisterButton(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val photo = data?.extras!!["data"] as Bitmap?
            profileImageView.setImageBitmap(photo)
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

    private fun initRegisterButton(view: View) {
        registerButton = view.findViewById(R.id.register_register_button)
        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        var email = callSignText.text.toString()
        val password = passwordText.text.toString()
        val passwordAgain = passwordAgainText.text.toString()
        val name = nameText.text.toString()
        val surname = surnameText.text.toString()
        val phoneNumber = phoneNumberText.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty() && password == passwordAgain) {
            email += "@gmail.com"
            MainActivity.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity as Activity) { task ->
                    if (task.isSuccessful) {
                        var user = MainActivity.auth.currentUser
                        val profileUpdates = userProfileChangeRequest {
                            displayName = "$name $surname"
                        }

                        user!!.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val i = Intent(activity as Activity, MainActivity::class.java)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(i)
                                    requireActivity().finish()
                                }
                                else {
                                    Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}