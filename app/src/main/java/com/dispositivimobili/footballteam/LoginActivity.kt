package com.dispositivimobili.footballteam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern
import kotlin.concurrent.thread


class LoginActivity : AppCompatActivity() {

    //variabili utilizzate nel codice
    private lateinit var mAuth: FirebaseAuth
    private var TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //connessione db remoto
        thread(start=true){
            mAuth = FirebaseAuth.getInstance()
            Log.d(TAG, "db: istanza ottenuta")
        }

        //listener per andare a RegisterActivity
        ButtonRegisterLoginActivity.setOnClickListener {
            thread(start = true) {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                Log.d(TAG, "click on button register, go to RegisterActivity")
            }
        }
    }

    //listener per recuperare la password dimenticata
    fun resetPassword(v:View){
        val email = textMessageAllPlayerActivity.getText().toString()
        //sse la mail inserita è corretta, inviamo una mail di ripristino password
        if(validateEmail()) {
            thread(start = true) {
                mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener({
                        val text = getString(R.string.checkemail)
                        Toast.makeText(this, "$text", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "sendPasswordResetEmail: Success")
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    })
                    .addOnFailureListener({
                        val text = getString(R.string.noresetpassword)
                        Toast.makeText(this, "$text", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "sendPasswordResetEmail: Failure")
                    })
            }
        }
    }

    //listener per controllare il login
    fun checkmessageOK(v: View?) {
        var correct_data : Boolean = false
        val email: String = textMessageAllPlayerActivity.getText().toString()
        val password: String = passwordLoginActivity.getText().toString()

        if(!validateEmail() || !validatePassword()) {
            correct_data = false
        } else {
            correct_data = true
        }

        //sse i dati sono corretti, effettuiamo il login
        if(correct_data == true) {
            thread(start = true) {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener({
                        val text = getString(R.string.loginsuccessfully)
                        Toast.makeText(this, "$text", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        Log.d(TAG, "signInUserWithEmail: Success, go to MainActivity")
                    })
                    .addOnFailureListener({
                        val text = getString(R.string.loginfailed)
                        Log.w(TAG, "signInUserWithEmail: Failure")
                        Toast.makeText(this, "$text", Toast.LENGTH_SHORT).show()
                    })
            }
        }
    }


    //metodo per controllare la validità della password
    private fun validatePassword(): Boolean {
        //val pass: String = passwordLoginActivity.text.toString()
        val password: String = passwordLoginActivity.getText().toString()
        val PASSWORD_VAL = "^" +
                //"(?=.*[0-9])" +       //at least 1 digit
                //"(?=.*[a-z])" +       //at least 1 lower case letter
                //"(?=.*[A-Z])" +       //at least 1 upper case letter
                "(?=.*[a-zA-Z0-9])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$"

        val pattern = Pattern.compile(PASSWORD_VAL)
        val matcher = pattern.matcher(password)

        if(password.isEmpty()){
            passwordLoginActivityEsterna.setError(getString(R.string.field_not_empty))
            return false
        } else if(!matcher.matches()){
            passwordLoginActivityEsterna.setError(getString(R.string.invalid_password))
            return false
        }else{
            passwordLoginActivityEsterna.setError(null)
            return true
        }
    }

    //metodo per controllare la validità della e-mail
    private fun validateEmail(): Boolean {
        val email: String = textMessageAllPlayerActivity.text.toString()
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)

        if(email.isEmpty()){
            textMessageAllPlayerActivityEsterna.setError(getString(R.string.field_not_empty))
            return false
        } else {
            if (!matcher.matches()) {
                textMessageAllPlayerActivityEsterna.setError(getString(R.string.invalid_email))
                return false
            } else {
                textMessageAllPlayerActivityEsterna.setError(null)
                return true
            }
        }
    }
}