package com.dispositivimobili.footballteam

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.toolbarprincipal.*
import java.lang.StringBuilder
import kotlin.concurrent.thread

class PrincipalActivityFragment : Fragment(){

    //variabili utilizzate nel codice
    private val TAG = "PrincipalActivityFragment"
    val mPlayer: MutableList<Player> = ArrayList()
    private lateinit var mAuth: FirebaseAuth
    var mPlayerChildListener: ChildEventListener = getPlayerChildEventListener()
    lateinit var mAdapter: MyAdapter
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("player")

    //callback simile a onCreate per le activity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activityprincipal_notoolbar, container, false)
        mAdapter = MyAdapter(requireContext(), mPlayer)
        thread(start=true) {
            mAuth = FirebaseAuth.getInstance()
            Log.d(TAG, "db: istanza ottenuta")
        }

        return view
    }

    //callback chiamata quando tutti gli elementi dell'interfaccia grafica sono pronti
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //listener per inviare un messaggio a tutti i giocatori presenti nella listView
        fabmessage.setOnClickListener(){
            thread(start=true) {
                var i = 1
                var finito = false
                val virgolaspazio = ", "
                val sb = StringBuilder()
                thread(start = true) {
                    while (i <= listViewPrincipalActivity.count) {
                        reference.child(i.toString()).get()
                            .addOnSuccessListener {
                                val phone = it.child("phone").getValue().toString()
                                sb.append(phone).append(virgolaspazio)
                            }
                        i++
                        if (i == listViewPrincipalActivity.count) {
                            finito = true
                        }
                    }

                    Thread.sleep(100)
                    if (finito == true) {
                        sb.delete((sb.length) - 2, sb.length)
                        val message = "Ciao ragazzi, sono il mister, "
                        val uri: Uri = Uri.parse("smsto: $sb")
                        val intent = Intent(Intent.ACTION_SENDTO, uri)
                        intent.putExtra("sms_body", message)
                        startActivity(intent)
                        Log.d(TAG, "go to send message to all player")
                    }
                }
            }
        }

        //listener per poter effettuare il logout
        /*buttonLogoutToolbar.setOnClickListener(){
            thread(start=true) {
                mAuth.signOut()
            }
            val intent = Intent(getActivity(), LoginActivity::class.java)
            startActivity(intent)
            //finish()
            Log.d(TAG, "logout, go to LoginActivity")
        }

        //listener per poter aggiungere un nuovo giocatore
        AddPlayerButtonToolbar.setOnClickListener(){
            /*val intent= Intent(getActivity(), AddFootballerActivity::class.java)
            startActivity(intent)
            Log.d(TAG, "pressed on addplayer, go to AddFootballerActivity")*/

        }*/

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listViewPrincipalActivity!!.adapter = mAdapter!!

        //listener per poter visualizzare il giocaotre selezionato dalla riga della listView
        thread(start=true) {
            listViewPrincipalActivity!!.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
                Log.d(TAG, "click on listView")
                /*val intent = Intent(getActivity(), MainActivity::class.java)
                intent.putExtra("idnumero", position + 1)
                requireActivity().startActivity(intent)
                val see = SeePlayerActivityFragment()
                val p = PrincipalActivityFragment()
                val bundle = Bundle()
                bundle.putInt("idnumero", position+1)
                Log.d(TAG, "bundle ${bundle.toString()}")
                //p.setArguments(bundle)
                see.setArguments(bundle)
                requireFragmentManager().beginTransaction()
                    .replace(R.id.fragmentSeePlayer, SeePlayerActivityFragment() )
                    .commit();
                //val valuerow = position+1
                //Toast.makeText(this, "you click on $valuerow", Toast.LENGTH_SHORT).show()
                //finish()*/


                val activity = getActivity()
                if(activity is CoordinatorFragments) {
                    activity.onRowClicked(position + 1)
                    Log.d(TAG, "position is  ${position.toString()}")
                }

                /*val count = listViewPrincipalActivity.count
                Log.d(TAG, "v is $count")
                val active = getActivity()
                if(active is CoordinatorFragments) {
                    active.countList(count)
                    Log.d(TAG, "row count is ${count.toString()}")
                }*/

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