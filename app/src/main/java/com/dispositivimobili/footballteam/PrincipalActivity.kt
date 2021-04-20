package com.dispositivimobili.footballteam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_principal.*

class PrincipalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        val users = arrayOf(
            "Virat Kohli", "Rohit Sharma", "Steve Smith",
            "Kane Williamson", "Ross Taylor", "Mario Rossi",
            "Giuseppe verdi", "Pippo Baudo",
            "Virat Kohli", "Rohit Sharma", "Steve Smith",
            "Kane Williamson", "Ross Taylor", "Mario Rossi",
            "Giuseppe verdi", "Pippo Baudo"
        )

        listViewPrincipalActivity.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, users)
    }
}