package com.example.express

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class login : AppCompatActivity() {
    private lateinit var cd: CardView
    private lateinit var edtemail: EditText
    private lateinit var edtpass:EditText
    private lateinit var btnsignin: Button
    private lateinit var btnSignup:Button
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()

        if(auth.currentUser!=null){
            val intent=Intent(this,Main::class.java)
            startActivity(intent)
            finish()
        }

        btnsignin.setOnClickListener {
            val email=edtemail.text.toString()
            val pass=edtpass.text.toString()
            signIn(email,pass)
        }


        btnSignup.setOnClickListener {
            val intent=Intent(this,SignUp::class.java)
                    startActivity(intent)

        }
        
    }

//    private fun signUp(email: String, pass: String) {
//        auth.createUserWithEmailAndPassword(email,pass)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//
//                    val listofposts= mutableListOf<String>()
//                    listofposts.add("")
//                    val listoffollowing= mutableListOf<String>()
//                    listoffollowing.add("")
//
//                    val user=User(
//                        name = "user",
//                        listofPost = listofposts,
//                        listofFollowing = listoffollowing,
//                        uid = auth.uid.toString(),
//                        email =email,
//                        profilPic = ""
//                    )
//
//                    addUser(user)
//                    val intent=Intent(this,Home::class.java)
//                    startActivity(intent)
//                    finish() //to distroy the current signin activity
//
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
//                }
//            }
//
//    }
//
//    private fun addUser(user: User) {
//        Firebase.database.getReference("users").child(user.uid).setValue(user)
//    }

    private fun signIn(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"Sign In Successfully", Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,Main::class.java)
                    startActivity(intent)
                    finish() //to distroy the current signin activity

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun init(){
        edtemail=findViewById(R.id.edtEmail)
        edtpass=findViewById(R.id.edtPass)
        btnsignin=findViewById(R.id.btnSignin)
        btnSignup=findViewById(R.id.btnSignup)
        auth = Firebase.auth
    }
}