package com.dean.ojekonlinefirebase.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dean.ojekonlinefirebase.R
import com.dean.ojekonlinefirebase.model.Users
import com.dean.ojekonlinefirebase.utils.Constan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class SignupActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        btn_signup.onClick {
            if (et_signup_email.text.isNotEmpty() &&
                    et_signup_name.text.isNotEmpty() &&
                    et_signup_hp.text.isNotEmpty() &&
                    et_signup_password.text.isNotEmpty() &&
                    et_signup_confirm_password.text.isNotEmpty()
            ){
                authUserSignUp(
                    et_signup_email.text.toString(),
                    et_signup_password.text.toString()
                )
            }
        }
    }

    //proses autentication
    private fun authUserSignUp(email: String, pass: String): Boolean?{

        auth = FirebaseAuth.getInstance()
        var status: Boolean? = null
        val TAG = "tag"

        auth?.createUserWithEmailAndPassword(email, pass)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    if (insertUser(
                            et_signup_name.text.toString(),
                            et_signup_email.text.toString(),
                            et_signup_hp.text.toString(),
                            task.result?.user!!
                        )){
                        startActivity<LoginActivity>()
                    }
                } else {
                    status = false
                }
            }

        return status
    }

    //menambahkan datauser ke database
    private fun insertUser(name: String, email: String, hp: String, users: FirebaseUser): Boolean {
        var user = Users()
        user.uid = users.uid
        user.name = name
        user.email = email
        user.hp = hp

        val database = FirebaseDatabase.getInstance()
        var key = database.reference.push().key
        val myRef = database.getReference(Constan.tb_uaser)

        myRef.child(key!!).setValue(user)

        return true
    }
}