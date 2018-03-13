package moosedroid.Views

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.acrcloud.rec.mooseb.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.android.AndroidInjection
import moosedroid.Presentation.ListenedPresentation
import moosedroid.Presentation.ListenedPresenter
import moosedroid.Presentation.UserPresenter
import moosedroid.Models.Listened
import moosedroid.Models.User
import moosedroid.Views.Fragments.ListenedFragment
import javax.inject.Inject


class ListenedSongActivity : FragmentActivity(),OnMapReadyCallback,ListenedFragment.OnListFragmentInteractionListener, ListenedPresentation {
    
    private val auth = FirebaseAuth.getInstance()!!
    private lateinit var mMap: GoogleMap
    private var listenedFragment:ListenedFragment? = null
    
    @Inject
    lateinit var listenedPresenter: ListenedPresenter
    
    @Inject
    lateinit var userPresenter: UserPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listened_song)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        listenedPresenter.onCreate(this,getLoggedId())
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        listenedFragment = fragmentManager.findFragmentById(R.id.listFragment) as ListenedFragment
        listenedFragment!!.addList(listenedPresenter.listenedList)
    }

    override fun onListFragmentInteraction(item: Listened?) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(item?.latitude!!,item?.longitude!!),15.0f))
    }

    override fun showListened(listenedList: List<Listened>) {
        for (location in listenedPresenter.listenedList){
            Log.d("test2","adding")
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

    private fun getLoggedId(): Long {
        val user: User? = userPresenter.userDao.findUserByEmail(auth.currentUser?.email!!)
        return user!!.id
    }

}
