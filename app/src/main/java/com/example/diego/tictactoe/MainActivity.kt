package com.example.diego.tictactoe

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private var mFirebaseAnalytics:FirebaseAnalytics?=null

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference

    private var myEmail:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        var b:Bundle=intent.extras
        myEmail = b.getString("email")
        incomingCalls()

    }

    fun buClick(boton: View)
    {
        val botonSeleccionado = boton as Button
        var cellID=0

        when(botonSeleccionado.id)
        {
            R.id.bu1->cellID=1
            R.id.bu2->cellID=2
            R.id.bu3->cellID=3
            R.id.bu4->cellID=4
            R.id.bu5->cellID=5
            R.id.bu6->cellID=6
            R.id.bu7->cellID=7
            R.id.bu8->cellID=8
            R.id.bu9->cellID=9

        }

       // Toast.makeText(this, "ID-> " + cellID, Toast.LENGTH_SHORT).show()

        jugar(cellID,botonSeleccionado)

        checkarGanador()
    }

    var player1=ArrayList<Int>()
    var player2=ArrayList<Int>()
    var activePlayer=1;

    fun jugar(cellID:Int, botonSeleccionado:Button)
    {
        if(activePlayer==1)
        {
            botonSeleccionado.text = "X"
            botonSeleccionado.setBackgroundResource(R.color.darkgreen)
            player1.add(cellID)

            activePlayer = 2
            jugarConCelular()
        }
        else
        {
            botonSeleccionado.text = "O"
            botonSeleccionado.setBackgroundResource(R.color.blue)
            player2.add(cellID)
            activePlayer = 1
        }

        botonSeleccionado.isEnabled = false;
    }

    fun checkarGanador()
    {
        var ganador = -1

        //fila 1
        if(player1.contains(1) && player1.contains(2) && player1.contains(3))
            ganador = 1
        if(player2.contains(1) && player2.contains(2) && player2.contains(3))
            ganador = 2

        //fila 2
        if(player1.contains(4) && player1.contains(5) && player1.contains(6))
            ganador = 1
        if(player2.contains(4) && player2.contains(5) && player2.contains(6))
            ganador = 2

        //fila 3
        if(player1.contains(7) && player1.contains(8) && player1.contains(9))
            ganador = 1
        if(player2.contains(7) && player2.contains(8) && player2.contains(9))
            ganador = 2

        //col 1
        if(player1.contains(1) && player1.contains(4) && player1.contains(7))
            ganador = 1
        if(player2.contains(1) && player2.contains(4) && player2.contains(7))
            ganador = 2

        //fila 2
        if(player1.contains(2) && player1.contains(5) && player1.contains(8))
            ganador = 1
        if(player2.contains(2) && player2.contains(5) && player2.contains(8))
            ganador = 2

        //fila 3
        if(player1.contains(3) && player1.contains(6) && player1.contains(9))
            ganador = 1
        if(player2.contains(3) && player2.contains(6) && player2.contains(9))
            ganador = 2

        if(ganador != -1)
        {
            if(ganador==1)
                Toast.makeText(this, "El jugador 1 gana", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "El jugador 2 gana", Toast.LENGTH_SHORT).show()

        }

    }

    fun jugarConCelular()
    {
        var celdaVacia=ArrayList<Int>()
        for(cellID in 1..9)
        {
            if(!(player1.contains(cellID) || player2.contains(cellID)))
            {
                celdaVacia.add(cellID)
            }
        }

        val r = Random()

        var randIndex = r.nextInt(celdaVacia.size-0)+0
        val celdaID = celdaVacia[randIndex]

        var bSeleccionado:Button?

        when(celdaID)
        {
            1->bSeleccionado=bu1
            2->bSeleccionado=bu2
            3->bSeleccionado=bu3
            4->bSeleccionado=bu4
            5->bSeleccionado=bu5
            6->bSeleccionado=bu6
            7->bSeleccionado=bu7
            8->bSeleccionado=bu8
            9->bSeleccionado=bu9
            else->bSeleccionado=bu1

        }

        jugar(celdaID,bSeleccionado)
    }

    fun bAceptarEvent(view:android.view.View)
    {
        var email = etEmail.text.toString()
        myRef.child("Users").child(splitString(email)).child("Request").push().setValue(myEmail)

    }

    fun bEnviarEvent(view:android.view.View)
    {
        var email = etUserEmail.text.toString()
        myRef.child("Users").child(splitString(email)).child("Request").push().setValue(myEmail)
    }

    fun incomingCalls()
    {
        myRef.child("Users").child(splitString(myEmail!!)).child("Request")
            .addValueEventListener(object:ValueEventListener
            {
                override fun onDataChange(dataSnapshot: DataSnapshot)
                {
                    try
                    {
                        val td = dataSnapshot!!.value as HashMap<String, Any>

                        if(td!=null)
                        {
                            var value:String

                            for(key in td.keys)
                            {
                                value=td[key] as String
                                etUserEmail.setText(value)

                                myRef.child("Users").child(splitString(myEmail!!)).child("Request").setValue(true)

                            }
                        }
                    }
                    catch (ex:Exception)
                    {

                    }
                }

                override fun onCancelled(p0: DatabaseError)
                {

                }
            })
    }

    fun splitString(str:String):String
    {
        var split = str.split("@")
        return split[0]
    }
}
