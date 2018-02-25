package moosedroid.Firebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.acrcloud.rec.mooseb.R
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.acrcloud.rec.mooseb.ResetPasswordActivity
import com.acrcloud.rec.mooseb.SignupActivity

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import moosedroid.Views.Main2Activity


class LoginFireActivity : AppCompatActivity() {

    private var inputEmail: EditText? = null
    private var inputPassword:EditText? = null
    private var auth: FirebaseAuth? = null
    private var progressBar: ProgressBar? = null
    private var btnSignup: Button? = null
    private var btnLogin:Button? = null
    private var btnReset:Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()


        if (auth!!.currentUser != null) {
            println("ss")
            startActivity(Intent(this, Main2Activity::class.java))
            finish()
        }

        // set the view now
        setContentView(R.layout.activity_login)

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        inputEmail = findViewById(R.id.email)
        inputPassword = findViewById<EditText>(R.id.password)
        progressBar = findViewById(R.id.progressBar)
        btnSignup = findViewById(R.id.btn_signup)
        btnLogin = findViewById<Button>(R.id.btn_login)
        btnReset = findViewById<Button>(R.id.btn_reset_password)

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance()

        btnSignup!!.setOnClickListener { startActivity(Intent(this, SignupActivity::class.java)) }

        btnReset!!.setOnClickListener(View.OnClickListener { startActivity(Intent(this, ResetPasswordActivity::class.java)) })

        btnLogin!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString()
            val password = inputPassword!!.getText().toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter email address!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Enter password!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            progressBar!!.visibility = View.VISIBLE

            //authenticate user
            auth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar!!.visibility = View.GONE
                        if (!task.isSuccessful) {
                            // there was an error
                            if (password.length < 6) {
                                inputPassword!!.setError(getString(R.string.minimum_password))
                            } else {
                                Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show()
                            }
                        } else {
                            val intent = Intent(this, Main2Activity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
        })
    }
}
