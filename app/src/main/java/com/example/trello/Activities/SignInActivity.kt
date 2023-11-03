package com.example.trello.Activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.trello.R
import com.example.trello.databinding.ActivitySignInBinding
import com.example.trello.firebase.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import com.example.trello.models.User

class SignInActivity : BaseActivity() {
    private lateinit var toolbar_sign_in_activity : Toolbar
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)

        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )

        setUpActionBar()

        binding.btnSignIn.setOnClickListener {
            signInRegisteredUser()
        }
    }
    private fun setUpActionBar() {

        toolbar_sign_in_activity = findViewById(R.id.toolbar_sign_in_activity)
        setSupportActionBar(toolbar_sign_in_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_black_24dp)
        }
        toolbar_sign_in_activity.setNavigationOnClickListener { onBackPressed()}

    }
    private fun signInRegisteredUser() {
        // Here we get the text from editText and trim the space
        val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
        val password: String = binding.etPassword.text.toString().trim { it <= ' ' }

        if (validateForm(email, password)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

           // FirebaseApp.initializeApp(this)

            // Sign-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {

                        FirestoreClass().loadUserData(this@SignInActivity)

                    } else {
                        Toast.makeText(
                            this@SignInActivity,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
    // END

    private fun validateForm(email: String, password: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            showErrorSnackBar("Please enter email.")
            false
        } else if (TextUtils.isEmpty(password)) {
            showErrorSnackBar("Please enter password.")
            false
        } else {
            true
        }
    }

    fun signInSuccess(user: User) {

        hideProgressDialog()

        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        this.finish()
    }

}