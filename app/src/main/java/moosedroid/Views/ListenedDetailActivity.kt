package moosedroid.Views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.acrcloud.rec.mooseb.R
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import moosedroid.Firebase.Config
import android.widget.Toast
import com.google.android.youtube.player.YouTubeInitializationResult
import android.content.Intent
import android.content.res.Configuration
import android.support.v7.widget.Toolbar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_listened_detail.*
import moosedroid.Presentation.ListenedPresentation
import moosedroid.Presentation.ListenedPresenter
import moosedroid.Room.Listened
import moosedroid.Views.Fragments.ListenedFragment
import javax.inject.Inject


class ListenedDetailActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener, ListenedPresentation, OnMapReadyCallback {


    private val RECOVERY_REQUEST = 1
    private var youTubeView: YouTubePlayerView? = null
    private var listenedId: Long? = null
    private var listened: Listened? = null
    @Inject
    lateinit var presenter: ListenedPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listened_detail)


        val intent = intent
        val message = intent.getStringExtra("id")
        val message2 = intent.getStringExtra("userId")

        listenedId = message.toLong()
        presenter.onCreate(this, message2.toLong())

        youTubeView = findViewById(R.id.youtube_view)
        youTubeView?.initialize(Config().YOUTUBE_API_KEY, this)

        mapView3.onCreate(savedInstanceState)
        mapView3.getMapAsync(this)


    }

    override fun onMapReady(map: GoogleMap) {
        val loca = LatLng(listened?.latitude!!, listened?.longitude!!)
        map.addMarker(MarkerOptions().position(loca).title("Song Found"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loca, 15.0f))
        map.isBuildingsEnabled = true
    }


    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, wasRestored: Boolean) {
        if (!wasRestored) {
            val listened: Listened? = presenter.getListened(listenedId!!)
            this.listened = listened
            player.cueVideo(listened?.youtubeId)

        }
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider, errorReason: YouTubeInitializationResult) {
        if (errorReason.isUserRecoverableError) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show()
        } else {
            val error = String.format(getString(R.string.player_error), errorReason.toString())
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider()?.initialize(Config().YOUTUBE_API_KEY, this)
        }
    }

    private fun getYouTubePlayerProvider(): YouTubePlayer.Provider? {
        return youTubeView
    }


    private fun updateData() {
        text_artist.text = listened?.title + " by " + listened?.artist
        text_genre.text = listened?.genre
        text_date.text = listened?.date
        text_album.text = listened?.album

    }

    override fun showListened(listenedList: List<Listened>) {
        for (liste in listenedList) {
            if (liste.id == listenedId) {
                listened = liste
            }
        }
        updateData()
    }

    override fun listenedAddedAt(position: Int) {
        return
    }

    override fun scrollTo(position: Int) {
        return
    }

}
