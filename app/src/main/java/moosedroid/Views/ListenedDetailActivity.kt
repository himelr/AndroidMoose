package moosedroid.Views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.acrcloud.rec.mooseb.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_listened_detail.*
import moosedroid.Firebase.Config
import moosedroid.Presenter.ListenedPresentation
import moosedroid.Presenter.ListenedPresenter
import moosedroid.Models.Listened
import javax.inject.Inject

//Has the listened details. Map + Youtube
class ListenedDetailActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener, ListenedPresentation, OnMapReadyCallback {


    private val RECOVERY_REQUEST = 1
    private var youTubeView: YouTubePlayerView? = null
    private var listenedId: Long? = null
    private var listened: Listened? = null
    private lateinit var mMap: GoogleMap
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

        share.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "I just listened to " + listened?.title + " by " + listened?.artist + " on MooseDroid")
            sendIntent.type = "text/plain"

            if (sendIntent.resolveActivity(packageManager) != null) {
                startActivity(sendIntent)
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        val loca = LatLng(listened?.latitude!!, listened?.longitude!!)
        mMap.addMarker(MarkerOptions().position(loca).title("Song Found"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loca, 15.0f))
        mMap.isBuildingsEnabled = true

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

    override fun listenedAddedAt(position: Int) {}

    override fun scrollTo(position: Int) {}

}
