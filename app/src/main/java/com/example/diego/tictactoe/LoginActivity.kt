package com.example.diego.tictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth?=null
    //private var database:FirebaseDatabase?=null
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        //database= FirebaseDatabase.getInstance()
    }

    fun bLoginEvent(view: View)
    {
        loginWithFirebase(etEmail.text.toString(), etPassword.text.toString())
    }

    fun loginWithFirebase(email:String, password:String)
    {
        mAuth!!.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                task ->
                if(task.isSuccessful)
                {
                    Toast.makeText(this, "Succedfull login", Toast.LENGTH_SHORT).show()

                    var currentUser = mAuth!!.currentUser
                    if(currentUser!=null)
                    {
                        myRef.child("Users").child(splitString(currentUser.email.toString())).setValue(currentUser.uid)
                    }

                    loadMain()
                }
                else
                {
                    Toast.makeText(this, "Fail login", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        loadMain()
    }

    fun loadMain()
    {
        var currentUser = mAuth!!.currentUser


        if(currentUser!=null)
        {

            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", currentUser!!.email.toString())
            intent.putExtra("uid", currentUser!!.uid.toString())
            startActivity(intent)
        }

    }

    fun splitString(str:String):String
    {
        var split = str.split("@")
        return split[0]
    }
}
