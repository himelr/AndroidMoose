package moosedroid.Views

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import com.acrcloud.rec.mooseb.R
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_user_listened.*
import moosedroid.Presentation.ListenedAdapter
import moosedroid.Presentation.ListenedPresentation
import moosedroid.Presentation.ListenedPresenter
import moosedroid.Models.Listened
import javax.inject.Inject

class UserListenedActivity : MenuBaseActivity(), ListenedPresentation {


    override fun showListened(listenedList: List<Listened>) {
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
        upVar = true
        super.onCreate(savedInstanceState)

        recyclerView = findViewById(R.id.list2)
        recyclerView?.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView?.adapter = ListenedAdapter(emptyList(),this)
        listenedPresenter.onCreate(this,getLoggedId()!!)
    }

    override fun setBottomBar() {
        bottomBar = findViewById(R.id.include3)
        setItems()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.activity_user_listened
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.actionbar_user_listened, menu)
        return true
    }
}
