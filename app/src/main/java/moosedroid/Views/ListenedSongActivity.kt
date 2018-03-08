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
import dagger.android.AndroidInjection
import moosedroid.Presentation.ListenedAdapter
import moosedroid.Presentation.ListenedPresentation
import moosedroid.Presentation.ListenedPresenter
import moosedroid.Room.Listened
import moosedroid.Views.Fragments.ListenedFragment
import moosedroid.Views.Fragments.dummy.DummyContent
import javax.inject.Inject


class ListenedSongActivity : FragmentActivity(),OnMapReadyCallback,ListenedFragment.OnListFragmentInteractionListener, ListenedPresentation {

    private lateinit var mMap: GoogleMap
    @Inject
    lateinit var presenter2: ListenedPresenter
    var listenedFragment:ListenedFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listened_song)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        presenter2.onCreate(this,1)
        //presenter2.addNewSong(Listened("Moth","Metallica","Metal","spit",1,15.0,25.0))
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map

        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))

        listenedFragment = fragmentManager.findFragmentById(R.id.listFragment) as ListenedFragment
        listenedFragment!!.addList(presenter2.listenedList)



    }

    override fun onListFragmentInteraction(item: Listened?) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(item?.latitude!!,item?.longitude!!),17.0f))

        Log.d("test2",item?.genre)
    }

    override fun showListened(listenedList: List<Listened>) {
        Log.d("test2", "set adapter")
        //Log.d("test2", listenedList[0].name)
        //listenedFragment?.recyclerView?.adapter = ListenedAdapter(listenedList)
        for (location in presenter2.listenedList){
            Log.d("test2","adding")
            val markerL = LatLng(location.latitude!!, location.longitude!!)
            mMap.addMarker(MarkerOptions().position(markerL).title(location.title + "\n" + location.date))
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerL,17.0f))
        }


    }

    override fun listenedAddedAt(position: Int) {
        listenedFragment?.recyclerView?.adapter?.notifyItemInserted(position)
    }


    override fun scrollTo(position: Int) {
       listenedFragment?.recyclerView?.smoothScrollToPosition(position)
    }
}
