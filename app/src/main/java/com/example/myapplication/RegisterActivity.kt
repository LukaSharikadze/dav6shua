package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordConfirmationEditText: EditText
    private lateinit var submitButton: Button

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
//        if (mAuth.currentUser != null){
//            goToMain()
//        }


        setContentView(R.layout.activity_register)

        this.init()
        this.registerListener()
    }

    private fun init() {

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        passwordConfirmationEditText = findViewById(R.id.editTextPassword2)
        submitButton = findViewById(R.id.submitButton)
    }

    private fun registerListener() {
        submitButton.setOnClickListener {
            val email: String = emailEditText.text.toString()
            val password: String = passwordEditText.text.toString()
            val confpassword: String = passwordConfirmationEditText.text.toString()

            if (email.isEmpty() || password.isEmpty() || confpassword.isEmpty()){
                Toast.makeText(this, "Fill every form!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confpassword) {
                Toast.makeText(this, "Password confirmation - unsuccessful!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!EmailValidity(email)){
                Toast.makeText(this, "Email is incorrect!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!PasswordValidity(password)) {
                Toast.makeText(this, "Password compiled incorrectly. Check if it meets the requirements!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    goToMain()
                }
                else{
                    Toast.makeText(this, "User was unable to create!", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    fun EmailValidity(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun PasswordValidity(password: String): Boolean{
        val reg = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{9,}".toRegex()
        return reg.matches(password)
    }

    private fun goToMain(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

