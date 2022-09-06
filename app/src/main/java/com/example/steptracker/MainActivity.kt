package com.example.steptracker

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtSignUp: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtEmail=findViewById(R.id.loginEmail)
        edtPassword=findViewById(R.id.loginPassword)
        btnLogin=findViewById(R.id.btnLogin)
        txtSignUp=findViewById(R.id.txtReturn)

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val dbHelper = DatabaseHelper(this)
            val data = dbHelper.checkEmailExists(email)
            if (data!=null){
                val userID = data.getInt(0)
                val userPassword = data.getString(2)
                val userSteps = data.getInt(3)
                if (password == userPassword){
                    UserObject.ID=userID
                    UserObject.email=email
                    UserObject.password=password
                    UserObject.steps=userSteps
                    val intent = Intent(this, StepTrackerActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Successfully logged in!", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Email does not exist.", Toast.LENGTH_SHORT).show()
            }
        }

        txtSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}