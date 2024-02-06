package com.example.express

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.express.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var name:EditText
    private lateinit var email:EditText
    private lateinit var pass:EditText
    private lateinit var pass2:EditText

    private lateinit var signup:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        init()

        signup.setOnClickListener {
            val e=email.text.toString()
            val p=pass.text.toString()
            val p1=pass.text.toString()
            val n=name.text.toString()
            if(p==p1){
                signUp(e,p,n)
            }
            else{
                Toast.makeText(this,"Password Didn't Match", Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun init(){
        auth=Firebase.auth
        name=findViewById(R.id.upName)
        email=findViewById(R.id.upEmail)
        pass=findViewById(R.id.upPass)
        pass2=findViewById(R.id.up1Pass)
        signup=findViewById(R.id.btn1Signup)

    }
    private fun signUp(email: String, pass: String,name:String) {
        auth.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val listofposts= mutableListOf<String>()
                    listofposts.add("")
                    val listoffollowing= mutableListOf<String>()
                    listoffollowing.add("")
                    val listofnotification= mutableListOf<String>()
                    listofnotification.add("")

                    val user= User(
                        name = name,
                        listofPost = listofposts,
                        listofFollowing = listoffollowing,
                        listofNotification = listofnotification,
                        uid = auth.uid.toString(),
                        email =email,
                        profilPic = ""
                    )

                    addUser(user)
                    Toast.makeText(this,"Sign Up Successfully", Toast.LENGTH_SHORT).show()
                    val intent= Intent(this,Main::class.java)
                    startActivity(intent)
                    finish() //to distroy the current signin activity

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this,task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun addUser(user: User) {
        Firebase.database.getReference("users").child(user.uid).setValue(user)
    }
}