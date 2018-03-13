package moosedroid.Presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.acrcloud.rec.mooseb.R
import com.acrcloud.rec.mooseb.R.layout.activity_test_user
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_test_user.*
import moosedroid.Room.Listened
import moosedroid.Room.User
import moosedroid.Room.UserAdapter
import moosedroid.Views.MenuBaseActivity
import javax.inject.Inject

class TestUser : MenuBaseActivity(), UserPresentation {

    @Inject
    lateinit var presenter: UserPresenter

    private var recyclerView:RecyclerView? = null

    override fun userAddedAt(position: Int) {
        recyclerView?.adapter?.notifyItemInserted(position)

    }

    override fun scrollTo(position: Int) {
        recyclerView?.smoothScrollToPosition(position)

    }

    override fun showUsers(users: List<User>) {
        userList.adapter= UserAdapter(users)

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        upVar = true
        super.onCreate(savedInstanceState)
        recyclerView = findViewById(R.id.userList)
        recyclerView?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView?.adapter = UserAdapter(emptyList())
        presenter.onCreate(this)

        }
    override fun setBottomBar() {
        bottomBar = findViewById(R.id.include2)
        setItems()
    }

    override fun getLayoutResourceId(): Int {
        return activity_test_user
    }
    override fun showListened(listenedList: List<Listened>) {}

    override fun listenedAddedAt(position: Int) {}


    }


