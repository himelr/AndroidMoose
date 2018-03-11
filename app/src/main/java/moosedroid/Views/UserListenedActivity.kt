package moosedroid.Views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.acrcloud.rec.mooseb.R
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_test_user.*
import kotlinx.android.synthetic.main.activity_user_listened.*
import moosedroid.Presentation.ListenedAdapter
import moosedroid.Presentation.ListenedPresentation
import moosedroid.Presentation.ListenedPresenter
import moosedroid.Presentation.UserPresentation
import moosedroid.Room.Listened
import moosedroid.Room.User
import javax.inject.Inject

class UserListenedActivity : MenuBaseActivity(), ListenedPresentation {

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_user_listened
    }

    @Inject
    lateinit var presenter2: ListenedPresenter

    override fun showListened(listenedList: List<Listened>) {
            Log.d("test2", "set adapter")
        //Log.d("test2", listenedList[0].name)
           list2.adapter = ListenedAdapter(listenedList,this)


    }

    override fun listenedAddedAt(position: Int) {
       recyclerView?.adapter?.notifyItemInserted(position)
    }


    override fun scrollTo(position: Int) {
        recyclerView?.smoothScrollToPosition(position)
           }

    var recyclerView:RecyclerView? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        recyclerView = findViewById(R.id.list2)
        recyclerView?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView?.adapter = ListenedAdapter(emptyList(),this)
        presenter2.onCreate(this,getLoggedId()!!)

    }
}
