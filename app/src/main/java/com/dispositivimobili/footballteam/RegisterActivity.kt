package com.dispositivimobili.footballteam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern
import kotlin.concurrent.thread

//activity per registrarsi nell'applicazione come nuovo utente
class RegisterActivity : AppCompatActivity() {

    //variabili utilizzate nel codice
    private var TAG = "RegisterActivity"
    private lateinit var mAuth: FirebaseAuth

    //nel caso in cui si volessero registrare anche i dati degli utenti
    //private lateinit var rootNode: FirebaseDatabase
    //private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        thread(start=true){
            //rootNode = FirebaseDatabase.getInstance()
            //reference = rootNode.getReference("users")
            mAuth = FirebaseAuth.getInstance()
            Log.d(TAG, "db: istanza ottenuta")
        }
    }

    //listener per tornare ad effettuare il login in LoginActivity
    fun returnLogin(v: View?){
        thread(start=true) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Log.d(TAG, "click on login button, go to LoginActivity")
        }
    }

    //listener per controllare i dati inseriti e se la registrazione viene eseguita in modo corretto
    fun checkRegister(v: View?) {
        var correct_data : Boolean = false
        val email: String = emailRegisterActivity.getText().toString()
        val password: String = passwordRegisterActivity.getText().toString()

        if(!validateName() || !validateSurname() || !validateEmail() || !validatePhone() || !validatePassword()) {
            correct_data = false
        } else {
            correct_data = true
        }

        //se e solo se i dati sono corretti, effettuiamo la registrazione
        if(correct_data == true) {
            thread(start = true) {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(OnCompleteListener<AuthResult>() {
                        //val helperClass: User = User(name, surname, email, phone, password)
                        //reference.child(phone.toString()).setValue(helperClass)
                        val text = getString(R.string.authenticationsuccess)
                        Toast.makeText(this, "$text", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        Log.d(TAG, "createUserWithEmail: Success")
                    })
                    .addOnFailureListener(OnFailureListener() {
                        val text = getString(R.string.authenticationfailed)
                        Log.w(TAG, "createUserWithEmail: Failure")
                        Toast.makeText(this, "$text", Toast.LENGTH_SHORT).show()
                    })
            }
        }
    }


    //metodo privato per controllare la validità del nome
    private fun validateName(): Boolean{
        val name: String = nameRegisterActivity.getText().toString()
        if(name.isEmpty()){
            nameRegisterActivityEsterna.setError(getString(R.string.field_not_empty))
            return false
        } else{
            nameRegisterActivityEsterna.setError(null)
            return true
        }
    }

    //metodo privato per controllare la validità del cognome
    private fun validateSurname(): Boolean{
        val surname: String = surnameRegisterActivity.getText().toString()
        if(surname.isEmpty()){
            surnameRegisterActivityEsterna.setError(getString(R.string.field_not_empty))
            return false
        } else{
            surnameRegisterActivityEsterna.setError(null)
            return true
        }
    }

    //metodo privato per controllare la validità del telefono
    private fun validatePhone(): Boolean{
        val phone: String = phoneRegisterActivity.getText().toString()
        if(phone.isEmpty()){
            phoneRegisterActivityEsterna.setError(getString(R.string.field_not_empty))
            return false
        } else  if(phone.length != 10){
            phoneRegisterActivityEsterna.setError(getString(R.string.invalid_phone_number))
            return false
        } else{
            phoneRegisterActivityEsterna.setError(null)
            return true
        }
    }

    //metodo privato per controllare la validità della password
    private fun validatePassword(): Boolean {
        val password: String = passwordRegisterActivity.getText().toString()
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
            passwordRegisterActivityEsterna.setError(getString(R.string.field_not_empty))
            return false
        } else if(!matcher.matches()){
            passwordRegisterActivityEsterna.setError(getString(R.string.invalid_password))
            return false
        }else{
            passwordRegisterActivityEsterna.setError(null)
            return true
        }
    }

    //metodo privato per controllare la validità della e-mail
    private fun validateEmail(): Boolean {
        val email: String = emailRegisterActivity.getText().toString()
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)

        if(email.isEmpty()){
            emailRegisterActivityEsterna.setError(getString(R.string.field_not_empty))
            return false
        } else {
            if (!matcher.matches()) {
                emailRegisterActivityEsterna.setError(getString(R.string.invalid_email))
                return false
            } else {
                emailRegisterActivityEsterna.setError(null)
                return true
            }
        }
    }
}