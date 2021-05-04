package com.dispositivimobili.footballteam

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_principal.*

class PrincipalActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("player")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        mAuth = FirebaseAuth.getInstance()

        val users = arrayOf(
                "Virat Kohli", "Rohit Sharma", "Steve Smith",
                "Kane Williamson", "Ross Taylor", "Mario Rossi",
                "Giuseppe verdi", "Pippo Baudo",
                "Virat Kohli", "Rohit Sharma", "Steve Smith",
                "Kane Williamson", "Ross Taylor", "Mario Rossi",
                "Giuseppe verdi", "Pippo Baudo"
        )

        //listViewPrincipalActivity.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, users)
        listViewPrincipalActivity.adapter = MyAdapter(this, users)
    }

    fun onLogout(v: View){
        mAuth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    class MyAdapter(private val context: Context, val data: Array<String>) : BaseAdapter() {
        override fun getCount(): Int {
            return data.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var newView = convertView
            if (convertView == null)
                newView = LayoutInflater.from(context).inflate(R.layout.row, parent, false)
            if (newView != null) {
                val personSurname: TextView = newView.findViewById<TextView>(R.id.textViewSurname)
                val personName: TextView = newView.findViewById<TextView>(R.id.textViewName)
                val personPosition: TextView = newView.findViewById<TextView>(R.id.textViewPosition)
                val parts = data[position].split(" ")
                personSurname.text = parts[1]
                personName.text = parts[0]
                personPosition.text = "${position + 1}"
            }
            return newView
        }
    }
}
