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
import moosedroid.Presentation.ListenedPresentation
import moosedroid.Presentation.ListenedPresenter
import moosedroid.Room.Listened
import moosedroid.Views.Fragments.ListenedFragment
import moosedroid.Views.Fragments.dummy.DummyContent
import javax.inject.Inject


class ListenedSongActivity : FragmentActivity(),OnMapReadyCallback,ListenedFragment.OnListFragmentInteractionListener, ListenedPresentation {
    override fun onListFragmentInteraction(item: Listened?) {
        Log.d("test2",item?.genre)
    }
    private lateinit var mMap: GoogleMap
    @Inject
    lateinit var presenter2: ListenedPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listened_song)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        presenter2.onCreate(this,1)
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        val listenedFragment:ListenedFragment =  fragmentManager.findFragmentById(R.id.listFragment) as ListenedFragment
        listenedFragment.addList(presenter2.listenedList)

    }

    override fun showListened(listenedList: List<Listened>) {
        Log.d("test2", "set adapter")
        //Log.d("test2", listenedList[0].name)
        //listenedListRC.adapter = ListenedAdapter(listenedList)


    }

    override fun listenedAddedAt(position: Int) {
        //recyclerView?.adapter?.notifyItemInserted(position)
    }


    override fun scrollTo(position: Int) {
        //recyclerView?.smoothScrollToPosition(position)
    }
}
