package com.dispositivimobili.footballteam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private var TAG = "RegisterActivity"
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("users")
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun checkRegister(v: View?) {
        var start_email : Boolean = false
        var start_password : Boolean = false
        val name: String = editTextTextPersonNameRegisterActivity.text.toString()
        val surname: String = editTextTextPersonSurnameRegisterActivity.text.toString()
        val phone: String = editTextPhoneRegisterActivity.text.toString()
        val email: String = editTextTextEmailAddressRegisterActivity.getText().toString()
        val password: String = editTextTextPasswordRegisterActivity.getText().toString()

        if(name.isEmpty()){
            editTextTextPersonNameRegisterActivity.error = "Enter name"
            return
        }
        if(surname.isEmpty()){
            editTextTextPersonSurnameRegisterActivity.error = "Enter surname"
            return
        }
        if(email.isEmpty()){
            editTextTextEmailAddressRegisterActivity.error = "Enter email"
            return
        }
        if(phone.isEmpty()){
            editTextPhoneRegisterActivity.error = "Enter phone"
            return
        }
        if(password.isEmpty()){
            editTextTextPasswordRegisterActivity.error = "Enter password"
            return
        }

        if (!isValidEmail(email)) {
            start_email = false
            editTextTextEmailAddressRegisterActivity.setError(getString(R.string.invalid_email))
        } else{
            start_email = true
        }
        if (!isValidPassword(password)) {
            start_password = false
            editTextTextPasswordRegisterActivity.setError(getString(R.string.invalid_password))
        } else{
            start_password = true
        }

        //get all the values
        val helperClass: User = User(name, surname, email, phone, password, id)
        reference.child(id.toString()).setValue(helperClass)
        if(start_email == true && start_password == true){
            val intent = Intent(this, PrincipalActivity::class.java )
            startActivity(intent)
            Log.d(TAG, "createUserWithEmail: Success")
        } else{
            Log.w(TAG, "createUserWithEmail: Failure")
            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidPassword(password: String): Boolean {
        return if (password != null && password.length >= 4) {
            true
        } else false
    }

    private fun isValidEmail(email: String): Boolean {
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()

    }
}