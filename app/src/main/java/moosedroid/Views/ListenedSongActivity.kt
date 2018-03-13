package moosedroid.Views

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.Menu
import com.acrcloud.rec.mooseb.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.android.AndroidInjection
import moosedroid.Presenter.ListenedPresentation
import moosedroid.Presenter.ListenedPresenter
import moosedroid.Presenter.UserPresenter
import moosedroid.Models.Listened
import moosedroid.Models.User
import moosedroid.Views.Fragments.ListenedFragment
import javax.inject.Inject


class ListenedSongActivity : MenuBaseActivity(), OnMapReadyCallback, ListenedFragment.OnListFragmentInteractionListener, ListenedPresentation {

    private lateinit var mMap: GoogleMap
    private var listenedFragment: ListenedFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        upVar = true
        super.onCreate(savedInstanceState)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        listenedFragment = fragmentManager.findFragmentById(R.id.listFragment) as ListenedFragment
        listenedFragment!!.addList(listenedPresenter.listenedList)
        listenedPresenter.onCreate(this, getLoggedId()!!)
    }

    override fun onListFragmentInteraction(item: Listened?) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(item?.latitude!!, item?.longitude!!), 15.0f))
    }

    override fun showListened(listenedList: List<Listened>) {
        mMap.clear()
        listenedFragment!!.addList(listenedList)
        for (location in listenedPresenter.listenedList) {
            Log.d("test2", "adding")
            val markerL = LatLng(location.latitude!!, location.longitude!!)
            mMap.addMarker(MarkerOptions().position(markerL).title(location.title + "\n" + location.date))
        }
    }

    override fun listenedAddedAt(position: Int) {
        listenedFragment?.recyclerView?.adapter?.notifyItemInserted(position)
    }

    override fun scrollTo(position: Int) {
        listenedFragment?.recyclerView?.smoothScrollToPosition(position)
    }
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_listened_song
    }

    override fun setBottomBar() {
        bottomBar = findViewById(R.id.include3)
        setItems()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.actionbar_user_listened, menu)
        return true
    }

}
