package com.dispositivimobili.footballteam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.toolbarprincipal.*
import kotlin.concurrent.thread
import com.dispositivimobili.footballteam.AddFootballerActivityFragment as AddFootballerActivityFragment

//activity principale dell'applicazione, compresa della modalità landscape
class MainActivity : AppCompatActivity(), CoordinatorFragments {

    //variabili utilizzate nel codice
    private var rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var reference: DatabaseReference = rootNode.getReference("player")
    private lateinit var mAuth: FirebaseAuth
    private val TAG = "MainActivity"

    //creazione istanze fragments
    lateinit var seePlayerDescription: SeePlayerActivityFragment
    lateinit var principalfragmentlandscape: PrincipalActivityFragmentLandscape
    lateinit var principalfragmentportrait: PrincipalActivityFragmentPortrait
    lateinit var addplayer: AddFootballerActivityFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        thread(start=true) {
            mAuth = FirebaseAuth.getInstance()
            Log.d(TAG, "db: istanza ottenuta")
        }

        //fragment principalactivityfragment landscape
        principalfragmentlandscape = PrincipalActivityFragmentLandscape()
        //fragment seeplayeractivityfragment
        seePlayerDescription = SeePlayerActivityFragment()
        //fragment principalactivityfragment portrait
        principalfragmentportrait = PrincipalActivityFragmentPortrait()
        //fragment addfootballeractivityfragment
        addplayer = AddFootballerActivityFragment()

        //aggiungere i vari fragments
        val fragTransaction = supportFragmentManager.beginTransaction()
        fragTransaction.add(R.id.fragmentPrincipalActivityLandscape, principalfragmentlandscape)
        fragTransaction.add(R.id.fragmentSeePlayer, seePlayerDescription)
        fragTransaction.add(R.id.fragmentPrincipalActivityPortrait, principalfragmentportrait)
        fragTransaction.add(R.id.fragmentAddPlayerActivity, addplayer)
        fragTransaction.commit()

        //listener per effettuare il logout (modalità landscape)
        //val butLogout = findViewById<Button>(R.id.buttonLogoutToolbar)
        buttonLogoutToolbar?.setOnClickListener() {
            thread(start = true) {
                mAuth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                Log.d(TAG, "pressed on buttonlogout, go to logout")
                startActivity(intent)
                finish()
            }
        }

        //listener per rimpiazzare il fragment seeplayer con addplayer (modalità landscape)
        //val butAddPlayer = findViewById<Button>(R.id.AddPlayerButtonToolbar)
        AddPlayerButtonToolbar?.setOnClickListener{
            thread(start=true) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    Log.d(TAG, "pressed on addplayer, replace fragment")
                    replace<AddFootballerActivityFragment>(R.id.fragmentSeePlayer)
                    //Log.d(TAG, "main, row count is ${listViewPrincipalActivitynoToolbar.count}")
                    //principalfragment.method()
                }
            }
        }
    }


    //implementazione del metodo dell'interfaccian per la comunicazione tra fragment
    override fun onRowClicked(index: Int) {
        seePlayerDescription.changeDescription(index)

        /*val frag = fragmentseeplayer
        if(frag is SeePlayerActivityFragment())
            frag.changeDescription(index)*/
    }

    //implementazione del metodo dell'interfaccia per la comunicazione tra fragment
    override fun countList(index: Int) {
        addplayer.RowCount(index)

        /*val frag = fragmentAddPlayerActivity
        if(frag is AddFootballerActivityFragment)
            frag.RowCount(index)*/
    }
}