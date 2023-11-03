package com.example.trello.Activities
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.trello.R
import com.example.trello.databinding.ActivitySignUpBinding
import com.example.trello.firebase.FirestoreClass
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.trello.models.User

class SignUpActivity : BaseActivity() {

 private lateinit var toolbar_sign_up_activity :Toolbar
 private lateinit var binding : ActivitySignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )

        setUpActionBar()



        binding.btnSignUp.setOnClickListener {
            //FirebaseApp.initializeApp(this)
            registerUser()
        }
    }

    private fun setUpActionBar() {
        toolbar_sign_up_activity=findViewById(R.id.toolbar_sign_up_activity)
        setSupportActionBar(toolbar_sign_up_activity)
        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_black_24dp)
        }
        toolbar_sign_up_activity.setNavigationOnClickListener { onBackPressed()}
    }

    private fun registerUser() {
        // Here we get the text from editText and trim the space
        val name: String = binding.etName.text.toString().trim { it <= ' ' }
        val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
        val password: String = binding.etPassword.text.toString().trim { it <= ' ' }

        if (validateForm(name, email, password)) {
            // Show the progress dialog.

            showProgressDialog(resources.getString(R.string.please_wait))

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // Hide the progress dialog
                        hideProgressDialog()

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            // Registered Email
                            val registeredEmail = firebaseUser.email!!

                            val user = User(
                                firebaseUser.uid, name, registeredEmail
                            )

                            // call the registerUser function of FirestoreClass to make an entry in the database.
                            FirestoreClass().registerUser(this@SignUpActivity, user)
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }
    // END

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter name.")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password.")
                false
            }
            else -> {
                true
            }
        }
    }

    /**
     * A function to be called the user is registered successfully and entry is made in the firestore database.
     */
    fun userRegisteredSuccess() {

        Toast.makeText(
            this@SignUpActivity,
            "You have successfully registered.",
            Toast.LENGTH_SHORT
        ).show()

        // Hide the progress dialog
        hideProgressDialog()

        /**
         * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
         * and send him to Intro Screen for Sign-In
         */
        FirebaseAuth.getInstance().signOut()
        // Finish the Sign-Up Screen
        finish()
    }

}