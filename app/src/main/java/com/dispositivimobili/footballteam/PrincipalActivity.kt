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
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_principal.*
import java.lang.StringBuilder
import kotlin.concurrent.thread

class PrincipalActivity : AppCompatActivity() {

    //variabili utilizzate nel codice
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
        //connessione al db remoto per ottenere l'istanza
        thread(start=true) {
            mAuth = FirebaseAuth.getInstance()
            Log.d(TAG, "db: istanza ottenuta")
        }
        listViewPrincipalActivitynoToolbar!!.adapter = mAdapter

        //listener per la listView presente nell'activity
        //in base a quale riga e' stata premuta, passiamo alla visualizzazione del giocatore
        thread(start=true) {
            listViewPrincipalActivitynoToolbar!!.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
                Log.d(TAG, "click on listView")
                val intent = Intent(this, SeePlayerActivity::class.java)
                intent.putExtra("idnumero", position + 1)
                startActivity(intent)
                //val valuerow = position+1
                //Toast.makeText(this, "you click on $valuerow", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onStart(){
        super.onStart()
        Log.d(TAG, "onStart")
        if(mPlayerChildListener == null){
            thread(start = true) {
                mPlayerChildListener = getPlayerChildEventListener()
            }
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

    //listener per fare il logout, uscire dalla principalAcitivty e tornare al login iniziale
    fun onLogout(v: View){
        thread(start=true) {
            mAuth.signOut()
        }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        Log.d(TAG, "logout, go to LoginActivity")
    }

    //listener per poter andare ad aggiungere un giocatore
    fun plus(v: View?){
        val intent = Intent(this, AddFootballerActivity::class.java)
        intent.putExtra("count", listViewPrincipalActivitynoToolbar.count)
        startActivity(intent)
        finish()
        Log.d(TAG, "go to AddFootballerAcitvity")
    }

    //listener per spedire un messagio a tutti i giocatori presenti nella lista
    fun message(v: View){
        var i = 1
        var finito = false
        val virgolaspazio = ", "
        val sb = StringBuilder()
        thread(start = true) {
            while (i <= listViewPrincipalActivitynoToolbar.count) {
                reference.child(i.toString()).get()
                    .addOnSuccessListener {
                        val phone = it.child("phone").getValue().toString()
                        sb.append(phone).append(virgolaspazio)
                    }
                i++
                if (i == listViewPrincipalActivitynoToolbar.count) {
                    finito = true
                }
            }

            Thread.sleep(100)
            if (finito == true) {
                sb.delete((sb.length)-2, sb.length)
                val message = "Ciao ragazzi, sono il mister, "
                val uri: Uri = Uri.parse("smsto: $sb")
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.putExtra("sms_body", message)
                startActivity(intent)
                Log.d(TAG, "go to send message to all player")
            }
        }
    }

    //classe adapter per riempire la listView in fase d'esecuzione
    class MyAdapter(private val context: Context, val data: MutableList<Player>) : BaseAdapter() {
        private val TAG = "MyAdapter"
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
                //val parts = data[position].split(" ")
                personSurname.text = data[position].surname
                personName.text = data[position].name
                //personPosition.text = "${data[position].ruolo}"
                val testo = data[position].ruolo
                personPosition.text = testo[0].toUpperCase().toString()
                Log.d(TAG, "create a view")
            }
            return newView
        }
    }
}
