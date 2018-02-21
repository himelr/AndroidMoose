package moosedroid.Presentation

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.widget.LinearLayout
import android.widget.ListView
import com.acrcloud.rec.mooseb.R
import com.acrcloud.rec.mooseb.R.layout.activity_test_user
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_test_user.*
import kotlinx.android.synthetic.main.toolbar_main.*
import moosedroid.Room.User
import moosedroid.Room.UserAdapter
import javax.inject.Inject

class TestUser : AppCompatActivity(),UserPresentation {


    @Inject
    lateinit var presenter: UserPresenter

    var recyclerView:RecyclerView? = null

    override fun userAddedAt(position: Int) {
        recyclerView?.adapter?.notifyItemInserted(position)

    }

    override fun scrollTo(position: Int) {
        recyclerView?.smoothScrollToPosition(position)

    }

    override fun showUsers(users: List<User>) {
        userList.adapter= UserAdapter(users)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(activity_test_user)
        this.setSupportActionBar(findViewById(R.id.my_toolbar))

        recyclerView = findViewById(R.id.userList)
        recyclerView?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView?.adapter = UserAdapter(emptyList())

        val song = Song("name","Genre")

        Log.d("test2", song.date.toString())
        //setSupportActionBar(my_toolbar)
        //val ab = supportActionBar
        //ab!!.setDisplayHomeAsUpEnabled(true)
        presenter.onCreate(this)
        }

    }


