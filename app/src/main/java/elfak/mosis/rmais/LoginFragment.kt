package elfak.mosis.rmais

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment() {

    private lateinit var callSignText: EditText
    private lateinit var passwordText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews(view)
        initLoginButton(view)
        initRegisterButton(view)
    }

    private fun findViews(view: View) {
        callSignText = view.findViewById(R.id.login_call_sign_text)
        passwordText = view.findViewById(R.id.login_password_text)
    }

    private fun initLoginButton(view: View) {
        loginButton = view.findViewById(R.id.login_login_button)
        loginButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        var email: String = callSignText.text.toString()
        val password: String = passwordText.text.toString()
        if(email.isEmpty() || password.isEmpty()) {
            return
        }
        email += "@gmail.com"
        MainActivity.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity as Activity) { task ->
                if (task.isSuccessful) {
                    val i = Intent(activity as Activity, MainActivity::class.java)
                    i.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK)
                    startActivity(i)
                    requireActivity().finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Pufla", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun initRegisterButton(view: View) {
        registerButton = view.findViewById(R.id.login_register_button)
        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
        }
    }
}