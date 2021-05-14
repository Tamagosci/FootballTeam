package com.dispositivimobili.footballteam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_see_player.*

class SeePlayerActivity : AppCompatActivity() {
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("player")
    private var TAG = "SeePlayerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_player)

        //recupero dati da firebase
        val intent: Intent = getIntent()
        val id = intent.getIntExtra("idnumero",0)

        reference.child(id.toString()).get()
            .addOnSuccessListener {
                Log.i("firebase", "Got key ${it.key}")
                Log.i("firebase", "Got value ${it.value}")
                for(i in "${it.value}"){
                    val name = it.child("name").getValue()
                    val surname = it.child("surname").getValue()
                    val ruolo = it.child("ruolo").getValue()
                    val data = it.child("date").getValue()
                    val phone = it.child("phone").getValue()
                    val results = it.child("results").getValue()
                    val certification = it.child("certification").getValue()
                    val idnumero = it.child("idnumero").getValue()
                    namePlayerSeeActivity.setText(name.toString())
                    surnamePlayerSeeActivity.setText(surname.toString())
                    ruoloPlayerSeeActivity.setText(ruolo.toString())
                    dataPlayerSeeActivity.setText(data.toString())
                    phonePlayerSeeActivity.setText(phone.toString())
                    resultsPlayerSeeActivity.setText(results.toString())
                    certificationPlayerSeeActivity.setText(certification.toString())
                    idnumeroPlayerSeeActivity.setText(idnumero.toString())
                }
            }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
            }

        /*reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var sb = StringBuilder()
                for(i in snapshot.children) {
                    var name = i.child("name").getValue()
                    var surname = i.child("surname").getValue()
                    namePlayerSeeActivity.setText(name.toString())
                    surnamePlayerSeeActivity.setText(surname.toString())
                }

                //seconda opzione
                if(snapshot!!.exixst()){

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })*/
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
        idnumeroPlayerSeeActivity.isEnabled = true
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
            val idnumero = idnumeroPlayerSeeActivity.getText().toString()

            val helperClass: Player = Player(name, surname, date, phone, ruolo, results, certification, idnumero)
            reference.child(idnumero).setValue(helperClass)
            val intent = Intent(this, PrincipalActivity::class.java )
            startActivity(intent)
            Log.d(TAG, "modifyPlayer: Success")
            Toast.makeText(this, "ModifyPlayer success", Toast.LENGTH_SHORT).show()
        }
    }
}