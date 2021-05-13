package com.dispositivimobili.footballteam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_see_player.*
import kotlinx.android.synthetic.main.add_player.*
import kotlinx.android.synthetic.main.row.*

class SeePlayerActivity : AppCompatActivity() {
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("player")
    private var TAG = "SeePlayerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_player)

        val intent: Intent = getIntent()
        val name = intent.getStringExtra("name")
        val surname = intent.getStringExtra("surname")
        val ruolo = intent.getStringExtra("ruolo")
        val data = intent.getStringExtra("data")
        val phone = intent.getStringExtra("phone")
        val results = intent.getStringExtra("results")
        val certification = intent.getStringExtra("certification")
        namePlayerSeeActivity.setText(name)
        surnamePlayerSeeActivity.setText(surname)
        ruoloPlayerSeeActivity.setText(ruolo)
        dataPlayerSeeActivity.setText(data)
        phonePlayerSeeActivity.setText(phone)
        resultsPlayerSeeActivity.setText(results)
        certificationPlayerSeeActivity.setText(certification)
    }

    fun onReturn(v: View){
        val intent = Intent(this, PrincipalActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onModify(v: View){
        namePlayerSeeActivity.isEnabled = true
        surnamePlayerSeeActivity.isEnabled = true
        ruoloPlayerSeeActivity.isEnabled = true
        dataPlayerSeeActivity.isEnabled = true
        phonePlayerSeeActivity.isEnabled = true
        resultsPlayerSeeActivity.isEnabled = true
        certificationPlayerSeeActivity.isEnabled = true
        eliminabutton.setText("CONFERMA")
    }

    fun deleteORconfirm(v: View){
        val phone = intent.getStringExtra("phone")
        val testo = eliminabutton.getText().toString()
        if(testo == "ELIMINA") {
            reference.child(phone.toString()).removeValue()
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val name = namePlayerSeeActivity.getText().toString()
            val surname = surnamePlayerSeeActivity.getText().toString()
            val date = dataPlayerSeeActivity.getText().toString()
            val phone = phonePlayerSeeActivity.getText().toString()
            val ruolo = ruoloPlayerSeeActivity.getText().toString()
            val results = resultsPlayerSeeActivity.getText().toString()
            val certification = certificationPlayerSeeActivity.getText().toString()

            val helperClass: Player = Player(name, surname, date, phone, ruolo, results, certification)
            reference.child(phone).setValue(helperClass)
            val intent = Intent(this, PrincipalActivity::class.java )
            startActivity(intent)
            Log.d(TAG, "modifyPlayer: Success")
            Toast.makeText(this, "ModifyPlayer success", Toast.LENGTH_SHORT).show()
        }
    }
}