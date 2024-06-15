package com.example.skylearns.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.skylearns.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class signup_page : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "GoogleSignIn"
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)

        findViewById<TextView>(R.id.gotosignin).setOnClickListener {
            startActivity(Intent(this,login_page::class.java))
            finish();
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Obtain this from Firebase Console
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        val googleSignUp:ImageView = findViewById(R.id.googlesignup)

        googleSignUp.setOnClickListener {
            googleSignInClient.revokeAccess().addOnCompleteListener(this) {
                signUp()
            }
        }

        val register:Button = findViewById(R.id.register)
        val name:EditText = findViewById(R.id.namesignup)
        val email:EditText = findViewById(R.id.emailsignup)
        val password:EditText = findViewById(R.id.passwordsignup)

        register.setOnClickListener {
            val nameString = name.text.toString()
            val emailString = email.text.toString()
            val passwordString = password.text.toString()

            if(nameString == "" || emailString == "" || passwordString == ""){
                Toast.makeText(this,"Please fill up all the details!",Toast.LENGTH_SHORT).show()
            }

            else{
                firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                            // Navigate to another activity if needed
                            startActivity(Intent(this,dashboard::class.java))
                            finish()
                        } else {
                            Log.d(TAG,"${task.exception?.message}")
                            Toast.makeText(this, "Email ID is already in use! Please sign in", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }

    private fun signUp() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, login_page.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == login_page.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)!!
            Log.d(login_page.TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w(login_page.TAG, "Google sign in failed", e)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(login_page.TAG, "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    // Update UI with the signed-in user's information
                    Toast.makeText(this, "Google Sign-In Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,dashboard::class.java))
                    finish()
                } else {
                    Log.w(login_page.TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}