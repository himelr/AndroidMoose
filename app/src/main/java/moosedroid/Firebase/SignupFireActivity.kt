package moosedroid.Firebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.acrcloud.rec.mooseb.R

import com.google.firebase.auth.FirebaseAuth
import com.acrcloud.rec.mooseb.R.layout.*
import dagger.android.AndroidInjection
import moosedroid.Presenter.UserPresenter
import moosedroid.Views.Main2Activity
import javax.inject.Inject

class SignupFireActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter: UserPresenter

    private var inputEmail: EditText? = null
    private var inputPassword:EditText? = null
    private var btnSignIn: Button? = null
    private var btnSignUp:Button? = null
    private var btnResetPassword:Button? = null
    private var progressBar: ProgressBar? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(activity_signup)

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()

        btnSignIn = findViewById(R.id.sign_in_button)
        btnSignUp = findViewById(R.id.sign_up_button)
        inputEmail = findViewById(R.id.email)
        inputPassword = findViewById(R.id.password)
        progressBar = findViewById(R.id.progressBar)
        btnResetPassword = findViewById(R.id.btn_reset_password)

        btnResetPassword!!.setOnClickListener { startActivity(Intent(this@SignupFireActivity, ResetPasswordFireActivity::class.java)) }

        btnSignIn!!.setOnClickListener { finish() }

        btnSignUp!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString().trim({ it <= ' ' })
            val password = inputPassword!!.text.toString().trim({ it <= ' ' })

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(applicationContext, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            progressBar!!.visibility = View.VISIBLE
            //create user
            auth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@SignupFireActivity) { task ->
                        Toast.makeText(this@SignupFireActivity, "createUserWithEmail:onComplete:" + task.isSuccessful, Toast.LENGTH_SHORT).show()

                        progressBar!!.visibility = View.GONE
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful) {
                            Toast.makeText(this@SignupFireActivity, "Authentication failed." + task.exception!!,
                                    Toast.LENGTH_SHORT).show()
                        } else {
                            //Add user to local db
                            presenter.addNewUser(email)
                            startActivity(Intent(this@SignupFireActivity, Main2Activity::class.java))
                            finish()
                        }
                    }
        })
    }

    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }
}