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

class RegisterActivity : AppCompatActivity() {

    private var TAG = "RegisterActivity"
    private lateinit var mAuth: FirebaseAuth
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()
    }

    fun checkRegister(v: View?) {
        var correct_data : Boolean = false
        val name: String = editTextTextPersonNameRegisterActivity.text.toString()
        val surname: String = editTextTextPersonSurnameRegisterActivity.text.toString()
        val phone: String = editTextPhoneRegisterActivity.text.toString()
        val email: String = editTextTextEmailAddressRegisterActivity.text.toString()
        val password: String = editTextTextPasswordRegisterActivity.text.toString()

        if(!validateName() || !validateSurname() || !validateEmail() || !validatePhone() || !validatePassword()) {
            correct_data = false
        } else {
            correct_data = true
        }

        if(correct_data == true){
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener<AuthResult>() {
                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, PrincipalActivity::class.java)
                    startActivity(intent)
                })
                .addOnFailureListener(OnFailureListener {
                    Toast.makeText(this, "Registraion Error", Toast.LENGTH_SHORT).show()
                })
        }

        //get all the values
        /*if(correct_data == true){
            val helperClass: User = User(name, surname, email, phone, password)
            reference.child(phone.toString()).setValue(helperClass)
            val intent = Intent(this, PrincipalActivity::class.java )
            startActivity(intent)
            Log.d(TAG, "createUserWithEmail: Success")
            Toast.makeText(this, "Authentication success", Toast.LENGTH_SHORT).show()
        } else{
            Log.w(TAG, "createUserWithEmail: Failure")
            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun validateName(): Boolean{
        val name: String = editTextTextPersonNameRegisterActivity.text.toString()
        if(name.isEmpty()){
            editTextTextPersonNameRegisterActivity.setError(getString(R.string.field_not_empty))
            return false
        } else{
            editTextTextPersonNameRegisterActivity.setError(null)
            return true
        }
    }

    private fun validateSurname(): Boolean{
        val surname: String = editTextTextPersonSurnameRegisterActivity.text.toString()
        if(surname.isEmpty()){
            editTextTextPersonSurnameRegisterActivity.setError(getString(R.string.field_not_empty))
            return false
        } else{
            editTextTextPersonSurnameRegisterActivity.setError(null)
            return true
        }
    }

    private fun validatePhone(): Boolean{
        val phone: String = editTextPhoneRegisterActivity.text.toString()
        if(phone.isEmpty()){
            editTextPhoneRegisterActivity.setError(getString(R.string.field_not_empty))
            return false
        } else  if(phone.length != 10){
            editTextPhoneRegisterActivity.setError(getString(R.string.invalid_phone_number))
            return false
        } else{
            editTextPhoneRegisterActivity.setError(null)
            return true
        }
    }

    private fun validatePassword(): Boolean {
        val password: String = editTextTextPasswordRegisterActivity.text.toString()
        val PASSWORD_VAL = "^" +
                //"(?=.*[0-9])" +       //at least 1 digit
                //"(?=.*[a-z])" +       //at least 1 lower case letter
                //"(?=.*[A-Z])" +       //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$"

        /*return if (password != null && password.length >= 4) {
            true
        } else false*/

        val pattern = Pattern.compile(PASSWORD_VAL)
        val matcher = pattern.matcher(password)

        if(password.isEmpty()){
            editTextTextPasswordRegisterActivity.setError(getString(R.string.field_not_empty))
            return false
        } else if(!matcher.matches()){
            editTextTextPasswordRegisterActivity.setError(getString(R.string.invalid_password))
            return false
        }else{
            editTextTextPasswordRegisterActivity.setError(null)
            return true
        }
    }

    private fun validateEmail(): Boolean {
        val email: String = editTextTextEmailAddressRegisterActivity.text.toString()
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)

        if(email.isEmpty()){
            editTextTextEmailAddressRegisterActivity.setError(getString(R.string.field_not_empty))
            return false
        } else {
            if (!matcher.matches()) {
                editTextTextEmailAddressRegisterActivity.setError(getString(R.string.invalid_email))
                return false
            } else {
                editTextTextEmailAddressRegisterActivity.setError(null)
                return true
            }
        }
    }
}