package com.example.steptracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirm: EditText
    private lateinit var btnSignUp: Button
    private lateinit var txtReturn: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        edtEmail=findViewById(R.id.regEmail)
        edtPassword=findViewById(R.id.regPassword)
        edtConfirm=findViewById(R.id.regConfirm)
        btnSignUp=findViewById(R.id.btnSignIn)
        txtReturn=findViewById(R.id.txtReturn)

        btnSignUp.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val confirm = edtConfirm.text.toString()
            val dbHelper = DatabaseHelper(this)
            if (dbHelper.checkEmailExists(email)==null){
                if (password == confirm){
                    var userModel: UserModel
                    try{
                        userModel = UserModel(-1, email, password, 0)
                    }catch (e:Exception){
                        userModel = UserModel(-1, "Error", "Error", 0)
                    }

                    val success: Boolean = dbHelper.createAccount(userModel)
                    if (success){
                        val intent = Intent(this, StepTrackerActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Successfully registered account!", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Email already exists.", Toast.LENGTH_SHORT).show()
            }
        }

        txtReturn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}