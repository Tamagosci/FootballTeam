package com.dispositivimobili.footballteam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
    }

    fun checkRegister(v: View?) {
        var start_email : Boolean = false
        var start_password : Boolean = false
        val name: String = editTextTextPersonNameRegisterActivity.text.toString()
        val surname: String = editTextTextPersonSurnameRegisterActivity.text.toString()
        val phone: String = editTextPhoneRegisterActivity.text.toString()
        val email: String = editTextTextEmailAddressRegisterActivity.getText().toString()
        if(name.isEmpty()){
            editTextTextPersonNameRegisterActivity.error = "Enter name"
            return
        }
        if(surname.isEmpty()){
            editTextTextPersonSurnameRegisterActivity.error = "Enter surname"
            return
        }
        if(phone.isEmpty()){
            editTextPhoneRegisterActivity.error = "Enter phone"
            return
        }
        if (!isValidEmail(email)) {
            start_email = false
            editTextTextEmailAddressRegisterActivity.setError(getString(R.string.invalid_email))
        } else{
            start_email = true
        }
        val password: String = editTextTextPasswordRegisterActivity.getText().toString()
        if (!isValidPassword(password)) {
            start_password = false
            editTextTextPasswordRegisterActivity.setError(getString(R.string.invalid_password))
        } else{
            start_password = true
        }

        createUser(name, surname, email, phone, password)
    }

    private fun createUser(name: String, surname: String, email: String, phone: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
            if(task.isSuccessful){
                //Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail: success")
                val currentUser = auth.currentUser
                val uid = currentUser!!.uid
                val userMap = HashMap<String, String>()
                userMap["name"] = name
                userMap["surname"] = surname
                userMap["phone"] = phone
                val database = FirebaseDatabase.getInstance().getReference("Users").child(uid)
                database.setValue(userMap).addOnCompleteListener{ task ->
                    if(task.isSuccesful){
                       val intent = Intent(this, PrincipalActivity::class.java)
                        startActivity(intent)
                    }
                }
            } else{
                Log.w(TAG, "createUserWithEmail: Failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
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