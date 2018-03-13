package moosedroid.Views

import java.io.File

import org.json.JSONException
import org.json.JSONObject

import com.acrcloud.rec.sdk.ACRCloudConfig
import com.acrcloud.rec.sdk.ACRCloudClient
import com.acrcloud.rec.sdk.IACRCloudListener
import com.google.firebase.auth.FirebaseAuth

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.acrcloud.rec.mooseb.R
import com.acrcloud.rec.mooseb.R.layout.*
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.location.LocationServices
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_listened_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import moosedroid.Firebase.LoginFireActivity
import moosedroid.Presentation.ListenedPresenter
import moosedroid.Presentation.TestUser
import moosedroid.Room.Listened
import moosedroid.Room.User
import org.json.JSONArray
import java.lang.IllegalStateException
import javax.inject.Inject


class Main2Activity : MenuBaseActivity(), IACRCloudListener, ListenedPresenter.StartIntent {

    private val TEST = "test2"
    private val mClient: ACRCloudClient = ACRCloudClient()
    private val mConfig: ACRCloudConfig = ACRCloudConfig()

    private var mVolume: TextView? = null
    private var mResult: TextView? = null
    private var tv_time: TextView? = null

    private var mProcessing = false
    private var initState = false

    private var path = ""

    private var startTime: Long = 0
    private var stopTime: Long = 0
    private val MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 23
    private val MY_PERMISSIONS_REQUEST_LOCATION_DATA = 24

    private var compositeDisposable = CompositeDisposable()
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mlocationManager: LocationManager? = null
    private var switch = true

    @Inject
    lateinit var presenter: ListenedPresenter

    override fun getLayoutResourceId(): Int {
        return activity_main
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        presenter.onCreate(this, getLoggedId()!!, this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mlocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        path = Environment.getExternalStorageDirectory().toString() + "/acrcloud/model"

        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }

        mVolume = findViewById<View>(R.id.volume) as TextView
        mResult = findViewById<View>(R.id.result) as TextView
        tv_time = findViewById<View>(R.id.time) as TextView


        imageButton.setOnClickListener {
            if (switch) {
                switch = false
                it.setBackgroundResource(R.drawable.ic_mic_red_64dp)
                start()
            } else {
                switch = true
                it.setBackgroundResource(R.drawable.ic_mic_none_black_64dp)
                stop()
            }
        }

        this.mConfig.acrcloudListener = this
        this.mConfig.context = this
        this.mConfig.host = "identify-eu-west-1.acrcloud.com"
        this.mConfig.dbPath = path // offline db path, you can change it with other path which this app can access.
        this.mConfig.accessKey = "9b9af828f7503bd9b56c202090bf94ee"
        this.mConfig.accessSecret = "E45NF3Y3RdqDPEJB6NR2bx6mDQwXl7C5EsrvxuUW"
        this.mConfig.protocol = ACRCloudConfig.ACRCloudNetworkProtocol.PROTOCOL_HTTP // PROTOCOL_HTTPS
        this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_REMOTE
        //this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_LOCAL;
        //this.mConfig.reqMode = ACRCloudConfig.ACRCloudRecMode.REC_MODE_BOTH;


        // If reqMode is REC_MODE_LOCAL or REC_MODE_BOTH,
        // the function initWithConfig is used to load offline db, and it may cost long time.
        this.initState = this.mClient.initWithConfig(this.mConfig)
        if (this.initState) {
            this.mClient.startPreRecord(3000) //start prerecord, you can call "this.mClient.stopPreRecord()" to stop prerecord.
        }

    }

    private fun start() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO)
            }
        } else {
            recognizeMusic()

        }
    }

    private fun stop() {
        if (mProcessing && this.mClient != null) {
            this.mClient.stopRecordToRecognize()
        }
        mProcessing = false

        stopTime = System.currentTimeMillis()
    }

    private fun cancel() {
        if (mProcessing && this.mClient != null) {
            mProcessing = false
            this.mClient.cancel()
            this.tv_time!!.text = ""
            this.mResult!!.text = ""
        }
    }

    override fun onResult(result: String) {
        switch = true
        imageButton.setBackgroundResource(R.drawable.ic_mic_none_black_64dp)
        Log.d("test2", result)
        if (this.mClient != null) {
            this.mClient.cancel()
            mProcessing = false
        }

        var tres = "\n"

        try {
            val j = JSONObject(result)
            val j1 = j.getJSONObject("status")
            val j2 = j1.getInt("code")

            if (j2 == 0) {
                val metadata = j.getJSONObject("metadata")
                //
                if (metadata.has("humming")) {
                    val hummings = metadata.getJSONArray("humming")
                    for (i in 0 until hummings.length()) {
                        val tt = hummings.get(i) as JSONObject
                        val title = tt.getString("title")
                        val artistt = tt.getJSONArray("artists")
                        val art = artistt.get(0) as JSONObject
                        val artist = art.getString("name")
                        tres = tres + (i + 1) + ".  " + title + "\n"
                    }
                }
                if (metadata.has("music")) {
                    val musics = metadata.getJSONArray("music")
                    for (i in 0 until musics.length()) {
                        val tt = musics.get(i) as JSONObject
                        val title = tt.getString("title")
                        val artistt = tt.getJSONArray("artists")
                        val art = artistt.get(0) as JSONObject
                        val artist = art.getString("name")
                        tres = tres + (i + 1) + ".  Title: " + title + "    Artist: " + artist + "\n"
                        volume.text = title + " by " + artist
                    }
                }
                if (metadata.has("streams")) {
                    val musics = metadata.getJSONArray("streams")
                    for (i in 0 until musics.length()) {
                        val tt = musics.get(i) as JSONObject
                        val title = tt.getString("title")
                        val channelId = tt.getString("channel_id")
                        tres = tres + (i + 1) + ".  Title: " + title + "    Channel Id: " + channelId + "\n"
                    }
                }
                if (metadata.has("custom_files")) {
                    val musics = metadata.getJSONArray("custom_files")
                    for (i in 0 until musics.length()) {
                        val tt = musics.get(i) as JSONObject
                        val title = tt.getString("title")
                        tres = tres + (i + 1) + ".  Title: " + title + "\n"
                    }
                }

                val musics = metadata.getJSONArray("music")
                tres = tres + "\n\n" + result
                saveData(musics, result)
            } else {
                tres = result
                mResult?.text = j.toString(3)
                volume.text = "Song not found!"
            }
        } catch (e: JSONException) {
            tres = result
            mResult?.text = "Error occurred"
            e.printStackTrace()
        }
    }

    private fun saveData(musics: JSONArray, tres: String) {

        val locationObservable: Observable<Location?> = Observable.create {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient!!.lastLocation
                        .addOnSuccessListener(this, { location ->
                            // Got last known location. In some rare situations this can be null.
                            // Logic to handle location object
                            it.onNext(location)
                            it.onComplete()
                        })
            } else {
            }
        }
        locationObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe { location ->
            val j = JSONObject(tres)
            val metadata = j.getJSONObject("metadata")
            val music = metadata.getJSONArray("music")
            val external: JSONObject? = music.getJSONObject(0)
            var video = ""
            try {
                val youtube: JSONObject? = external?.getJSONObject("external_metadata")
                val youtubeId: JSONObject? = youtube?.getJSONObject("youtube")
                val youtString: String? = youtubeId?.getString("vid")
                video = youtString ?: ""

            } catch (e: JSONException) {
            }

            val album: JSONObject? = external?.getJSONObject("album")
            val alName: String? = album?.getString("name")

            val locationFine = mlocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            //Save song to use
            val tt = musics.get(0) as JSONObject
            val title: String = tt.getString("title")
            val artistt = tt.getJSONArray("artists")
            val art = artistt.get(0) as JSONObject
            val artist: String = art.getString("name")
            var genre = ""
            try {
                val genres: JSONArray? = external?.getJSONArray("genres")
                val genreObj: JSONObject? = genres?.getJSONObject(0)
                val genreT: String? = genreObj?.getString("name")
                genre = genreT ?: ""
            } catch (e: JSONException) {
            }

            if (locationFine != null) {
                presenter.addNewSong(Listened(title + "", artist + "", genre, alName
                        ?: "", getLoggedId()!!, locationFine.latitude, locationFine.longitude, video))
                presenter.getLatest()

            } else {
                presenter.addNewSong(Listened(title + "", artist + "", genre, "-", getLoggedId()!!, location.latitude, location.longitude, video))
                presenter.getLatest()
            }

            mResult?.text = j.toString(3)
        }

    }

    override fun onVolumeChanged(volume: Double) {
        val time = (System.currentTimeMillis() - startTime) / 1000
        mVolume!!.text = "Time：" + time + " s\n" + "\n" + resources.getString(R.string.volume) + " " + volume

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_RECORD_AUDIO -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("MainActivity", "release")
        if (this.mClient != null) {
            this.mClient.release()
            this.initState = false

        }

    }

    private fun recognizeMusic() {

        if (!this.initState) {
            Toast.makeText(this, "init error", Toast.LENGTH_SHORT).show()
            return
        }

        if (!mProcessing) {
            mProcessing = true
            mVolume!!.text = ""
            mResult!!.text = ""
            if (this.mClient == null || !this.mClient.startRecognize()) {
                mProcessing = false
                mResult!!.text = "start error"
            }
            startTime = System.currentTimeMillis()
        }

    }

    override fun startIntent(id: Long) {

        val intent = Intent(applicationContext, ListenedDetailActivity::class.java)
        intent.putExtra("id", id.toString() + "")
        intent.putExtra("userId", getLoggedId().toString() + "")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        ContextCompat.startActivity(applicationContext, intent, null)
    }

    override fun setBottomBar() {
        bottomBar = findViewById(R.id.include2)
        setItems()
    }

    override fun showListened(listenedList: List<Listened>) {}

    override fun listenedAddedAt(position: Int) {}

    override fun scrollTo(position: Int) {}

}
