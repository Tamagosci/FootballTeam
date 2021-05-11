package com.dispositivimobili.footballteam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
        namePlayerSeeActivity.setText(name)
        surnamePlayerSeeActivity.setText(surname)
        ruoloPlayerSeeActivity.setText(ruolo)
    }

}