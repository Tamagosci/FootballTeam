package com.dispositivimobili.footballteam

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.row.*

class PrincipalActivity : AppCompatActivity() {

    private val TAG = "PrincipalActivity"
    private lateinit var mAuth: FirebaseAuth
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("player")

    private val mPlayer: MutableList<Player> = ArrayList()
    private val mAdapter: MyAdapter = MyAdapter(this, mPlayer)
    private var mPlayerChildListener: ChildEventListener = getPlayerChildEventListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        mAuth = FirebaseAuth.getInstance()

        listViewPrincipalActivity.adapter = mAdapter
    }

    override fun onStart(){
        super.onStart()
        Log.d(TAG, "onStart")
        if(mPlayerChildListener == null){
            mPlayerChildListener = getPlayerChildEventListener()
        }
        reference!!.addChildEventListener(mPlayerChildListener)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
        if(mPlayerChildListener != null){
            reference!!.removeEventListener(mPlayerChildListener)
        }
    }


    private fun getPlayerChildEventListener(): ChildEventListener {
        val childEventListener = object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded: " + snapshot.key!!)
                val newPlayer = snapshot.getValue(Player::class.java)
                mPlayer.add(newPlayer!!)
                mAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: " + snapshot.key!!)
                val newPlayer = snapshot.getValue(Player::class.java)
                val playerKey = snapshot.key
                mPlayer.find{e -> e.toString().equals(playerKey)}?.set(newPlayer!!)
                mAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved: " + snapshot.key!!)
                val playerKey = snapshot.key
                val p = mPlayer.find { e -> e.toString().equals(playerKey) }
                mPlayer.remove(p)
                mAdapter.notifyDataSetChanged()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved: " + snapshot.key!!)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "onCancelled: " + error.toException())
            }

        }
        return childEventListener
    }

    fun onLogout(v: View){
        mAuth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun plus(v: View?){
        val intent = Intent(this, AddFootballerActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun seePlayer(v: View){
        val intent = Intent(this, SeePlayerActivity::class.java)
        val name = textViewName.getText().toString()
        intent.putExtra("name", name)
        val surname = textViewSurname.getText().toString()
        intent.putExtra("surname", surname)
        val ruolo = textViewRuolo.getText().toString()
        intent.putExtra("ruolo", ruolo)
        val data = textViewData.getText().toString()
        intent.putExtra("data", data)
        val phone = textViewPhone.getText().toString()
        intent.putExtra("phone", phone)
        val results = textViewResults.getText().toString()
        intent.putExtra("results", results)
        val certification = textViewCertification.getText().toString()
        intent.putExtra("certification", certification)
        startActivity(intent)
        finish()
    }

    fun message(v: View){
        val message = "Ciao, sono il mister, "
        val uri: Uri = Uri.parse("smsto:")
        val it = Intent(Intent.ACTION_SENDTO, uri)
        it.putExtra("sms_body", message)
        startActivity(it)
        finish()
    }

    class MyAdapter(private val context: Context, val data: MutableList<Player>) : BaseAdapter() {
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
                val personPosition: TextView = newView.findViewById<TextView>(R.id.textViewRuolo)
                val personData: TextView = newView.findViewById<TextView>(R.id.textViewData)
                val personPhone: TextView = newView.findViewById<TextView>(R.id.textViewPhone)
                val personResults: TextView = newView.findViewById<TextView>(R.id.textViewResults)
                val personCertification: TextView = newView.findViewById<TextView>(R.id.textViewCertification)
                //val parts = data[position].split(" ")
                personSurname.text = data[position].surname
                personName.text = data[position].name
                //personPosition.text = "${data[position].ruolo}"
                personPosition.text = data[position].ruolo
                personData.text = data[position].date
                personPhone.text = data[position].phone
                personCertification.text = data[position].certification
                personResults.text = data[position].results
            }
            return newView
        }
    }
}
