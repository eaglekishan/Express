package com.example.express

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Main : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var bn:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.ab))
        init()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fv,Home())
            .commit()

        bn.setOnNavigationItemSelectedListener {
            val i=it.itemId
            when(i){
                R.id.bProfil->{

                    display(Profile())
                }
                R.id.bPost->{
                    display(Post())
                }
                R.id.bHome->{
                    display(Home())
                }
                R.id.bNotification->{
                    display(Notification())
                }
                R.id.bSearch->{
                    display(Search())
                }

            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun display(fav: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fv,fav)
            .commit()

    }

    private fun init(){
        auth=Firebase.auth
        bn=findViewById(R.id.bn)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.signOut->{
                auth.signOut()
                Toast.makeText(this,"Sign Out Successfully",Toast.LENGTH_SHORT).show()
                val intent= Intent(this,login::class.java)
                startActivity(intent)
                finish()


            }
        }
        return true
    }
}