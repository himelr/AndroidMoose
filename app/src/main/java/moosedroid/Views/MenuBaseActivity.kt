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
import moosedroid.Presenter.ListenedPresentation
import moosedroid.Presenter.ListenedPresenter
import moosedroid.Presenter.UserActivity
import moosedroid.Presenter.UserPresenter
import moosedroid.Models.Listened
import moosedroid.Models.User
import javax.inject.Inject

/**
 * Created by HimelR on 04-Mar-18.
 */
//Abstract base class for activities.
abstract class MenuBaseActivity : AppCompatActivity(), ListenedPresentation {

    private val auth = FirebaseAuth.getInstance()!!
    protected abstract fun getLayoutResourceId(): Int
    protected abstract fun setBottomBar()
    protected lateinit var bottomBar: BottomNavigationView
    protected var upVar = false
    //Injected presenters.
    @Inject
    protected lateinit var userPresenter: UserPresenter

    @Inject
    protected lateinit var listenedPresenter: ListenedPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())
        setBottomBar()
        val myToolbar = findViewById<Toolbar>(R.id.my_toolbar2)
        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upVar)


        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
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
    //Handles actionbar clicks.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

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
                }
                alertDialog.show()
                return true
            }
            R.id.users -> {
                startActivity(Intent(this, UserActivity::class.java))
                return true
            }
            R.id.listened -> {
                startActivity(Intent(this, UserListenedActivity::class.java))
                return true
            }

            R.id.populate -> {
                val alertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("Populate")
                alertDialog.setMessage("Are you sure you want to add to songs?")
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "YES"
                ) { dialog, which ->
                    val id = getLoggedId()!!
                    listenedPresenter.onCreate(this, id)
                    listenedPresenter.addNewSong(Listened("Battery", "Metallica", "Metal", "Master Of Puppets", id, 40.710008, -74.005322, "md3B3I7Nmvw"))
                    listenedPresenter.addNewSong(Listened("Comfortably Numb", "Pink Floyd", "Rock", "The Wall", id, 60.451813, 22.266630, "_FrOQC-zEog"))

                }
                alertDialog.show()
                return true
            }
            android.R.id.home -> {
                super.finish()
                return true
            }
            R.id.delete -> {
                val alertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("Delete")
                alertDialog.setMessage("Are you sure you want to delete listened?")
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "YES"
                ) { dialog, which ->
                    val id = getLoggedId()!!
                    listenedPresenter.deleteUsersListened(id)
                }
                alertDialog.show()
                return true
            }
            else ->

                return super.onOptionsItemSelected(item)
        }
    }
    //Set bottombar items.
    fun setItems() {
        bottomBar.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.action_item1 -> startActivity(Intent(this, Main2Activity::class.java))
                R.id.action_item2 -> startActivity(Intent(this, ListenedSongActivity::class.java))
                R.id.action_item3 -> startActivity(Intent(this, WebBoardActivity::class.java))
            }
            true
        }
    }
    //Logged user id.
    fun getLoggedId(): Long? {
        val user: User? = userPresenter.userDao.findUserByEmail(auth.currentUser?.email!!)
        return user?.id
    }

    override fun onDestroy() {
        super.onDestroy()
        listenedPresenter.onDestroy()
        userPresenter.onDestroy()

    }

}