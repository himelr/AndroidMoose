package moosedroid.Views

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.acrcloud.rec.mooseb.R
import com.google.firebase.auth.FirebaseAuth
import dagger.android.AndroidInjection
import moosedroid.Firebase.LoginFireActivity
import moosedroid.Presentation.ListenedPresenter
import moosedroid.Presentation.TestUser
import moosedroid.Presentation.UserPresenter
import javax.inject.Inject

/**
 * Created by HimelR on 04-Mar-18.
 */
abstract class MenuBaseActivity : AppCompatActivity(){
    protected val auth = FirebaseAuth.getInstance()!!
    protected abstract fun getLayoutResourceId(): Int

    @Inject
    protected lateinit var userPresenter: UserPresenter






    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.action_item1 -> startActivity(Intent(this,Main2Activity::class.java))
                R.id.action_item2 -> startActivity(Intent(this,ListenedSongActivity::class.java))
                R.id.action_item3 -> startActivity(Intent(this,WebBoardActivity::class.java))
            }

            true
        }

        val myToolbar = findViewById<Toolbar>(R.id.my_toolbar2)
        setSupportActionBar(myToolbar)
        //val ab = supportActionBar

        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                println("Logout Firex")
                Log.d("test2", "logout")
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this, LoginFireActivity::class.java))
                finish()
            }
        }
        auth.addAuthStateListener(authListener)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.actionbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings ->
                // User chose the "Settings" item, show the app settings UI...
                return true

            R.id.action_favorite -> {
                val alertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("Logout")
                alertDialog.setMessage("Are you sure you want to logout?")
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "YES"
                ) { dialog, which ->
                    if (auth.currentUser != null) {
                        auth.signOut()
                    }

                    //dialog.dismiss();
                }
                alertDialog.show()
                return true
            }
            R.id.users -> {

                startActivity(Intent(this, TestUser::class.java))
                return true

            }
            R.id.listened -> {

                startActivity(Intent(this, UserListenedActivity::class.java))
                return true

            }
            R.id.listenedMap -> {

                startActivity(Intent(this, ListenedSongActivity::class.java))
                return true

            }

            else ->
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item)
        }
    }

}