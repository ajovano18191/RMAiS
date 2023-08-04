package elfak.mosis.rmais

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class RegisterFragment : Fragment() {

    private lateinit var callSignText: EditText
    private lateinit var passwordText: EditText
    private lateinit var passwordAgainText: EditText
    private lateinit var registerButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews(view)
        initRegisterButton(view)
    }

    private fun findViews(view: View) {
        callSignText = view.findViewById(R.id.register_call_sign_text)
        passwordText = view.findViewById(R.id.register_password_text)
        passwordAgainText = view.findViewById(R.id.register_password_again_text)
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

        if(email.isNotEmpty() && password.isNotEmpty() && password == passwordAgain) {
            email += "@gmail.com"
            MainActivity.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity as Activity) { task ->
                if (task.isSuccessful) {
                    val i = Intent(activity as Activity, MainActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}