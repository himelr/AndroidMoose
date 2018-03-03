package moosedroid.Views

import java.io.File

import org.json.JSONException
import org.json.JSONObject

import com.acrcloud.rec.sdk.ACRCloudConfig
import com.acrcloud.rec.sdk.ACRCloudClient
import com.acrcloud.rec.sdk.IACRCloudListener
import com.google.firebase.auth.FirebaseAuth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
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
import kotlinx.android.synthetic.main.activity_main.*
import moosedroid.Firebase.LoginFireActivity
import moosedroid.Presentation.ListenedPresenter
import moosedroid.Room.Listened
import javax.inject.Inject



class Main2Activity : AppCompatActivity(), IACRCloudListener {
    private val TEST = "test2"
    private val mClient: ACRCloudClient = ACRCloudClient()
    private val mConfig: ACRCloudConfig = ACRCloudConfig()

    private var mVolume: TextView? = null
    private var mResult:TextView? = null
    private var tv_time:TextView? = null

    private var mProcessing = false
    private var initState = false

    private var path = ""

    private var startTime: Long = 0
    private var stopTime: Long = 0
    private val MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 23
    private val MY_PERMISSIONS_REQUEST_LOCATION_DATA = 24

    private val auth = FirebaseAuth.getInstance()
    private var compositeDisposable = CompositeDisposable()
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    @Inject
    lateinit var presenter: ListenedPresenter



    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

         Log.d(TEST, "launch")
        path = Environment.getExternalStorageDirectory().toString() + "/acrcloud/model"

        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }

        mVolume = findViewById<View>(R.id.volume) as TextView
        mResult = findViewById<View>(R.id.result) as TextView
        tv_time = findViewById<View>(R.id.time) as TextView

        val startBtn = findViewById<View>(R.id.start) as Button
        startBtn.text = resources.getString(R.string.start)

        val stopBtn = findViewById<View>(R.id.stop) as Button
        stopBtn.text = resources.getString(R.string.stop)

        findViewById<View>(R.id.stop).setOnClickListener { stop() }

        val cancelBtn = findViewById<View>(R.id.cancel) as Button
        cancelBtn.text = resources.getString(R.string.cancel)

        findViewById<View>(R.id.start).setOnClickListener { start() }

        findViewById<View>(R.id.cancel).setOnClickListener { cancel() }




        this.mConfig.acrcloudListener = this

        // If you implement IACRCloudResultWithAudioListener and override "onResult(ACRCloudResult result)", you can get the Audio data.
        //this.mConfig.acrcloudResultWithAudioListener = this;

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
        val myToolbar = findViewById<Toolbar>(R.id.my_toolbar2)
        setSupportActionBar(myToolbar)
        val ab = supportActionBar

        val bottomNavigationView:BottomNavigationView = findViewById(R.id.navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.action_item1 -> Log.d("test2", "1")
                R.id.action_item2 -> Log.d("test2", "2")
                R.id.action_item3 -> Log.d("test2", "3")
            }

            true
        }

        // Enable the Up button
        //ab!!.setDisplayHomeAsUpEnabled(true)

        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                println("Logout Firex")
                Log.d(TEST, "logout")
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this, LoginFireActivity::class.java))
                finish()
            }
        }
        auth.addAuthStateListener(authListener)

        val serverDownloadObservable:Observable<Int> = Observable.create {
            SystemClock.sleep(5000)
            it.onNext(5)
            it.onComplete()
        }
        serverDownloadObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe { integer ->
            //testBox.text = integer.toString() // this methods updates the ui
           // enables it again
        }



    }

    fun start() {
        println("aaaeweweww")
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            println("aaa")


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION) ) {


                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.RECORD_AUDIO,Manifest.permission.ACCESS_COARSE_LOCATION),
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            recognizeMusic()

        }
    }

    protected fun stop() {
        if (mProcessing && this.mClient != null) {
            this.mClient.stopRecordToRecognize()
        }
        mProcessing = false

        stopTime = System.currentTimeMillis()
    }

    protected fun cancel() {
        if (mProcessing && this.mClient != null) {
            mProcessing = false
            this.mClient.cancel()
            this.tv_time!!.text = ""
            this.mResult!!.text = ""
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.actionbar, menu)
        return true
    }

    // Old api



    override fun onResult(result: String) {
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
                tres = tres + "\n\n" + result
            } else {
                tres = result

            }
        } catch (e: JSONException) {
            tres = result

            e.printStackTrace()
        }
        saveData(tres)


    }
    private fun saveData(tres:String){

        val locationObservable:Observable<Location?> = Observable.create {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient!!.lastLocation
                        .addOnSuccessListener(this, { location ->
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                Log.d("test2",location.longitude.toString())
                                it.onNext(location)
                                it.onComplete()

                            }
                        })
            } else {
            }


        }
        locationObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe { location ->

            //Save song to use

            presenter.addNewSong(Listened("Ww ","dsd","bob",1))
            testBox.text = location?.longitude.toString()
            mResult?.text = tres
        }




    }
    override fun onVolumeChanged(volume: Double) {
        val time = (System.currentTimeMillis() - startTime) / 1000
        mVolume!!.text = resources.getString(R.string.volume) + volume + "\n\nTime：" + time + " s"

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_RECORD_AUDIO -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                    recognizeMusic()


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request.
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings ->
                // User chose the "Settings" item, show the app settings UI...
                return true

            R.id.action_favorite -> {
                val alertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("Logout")
                alertDialog.setMessage("Are you sure you want to logout?")
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO"
                ) { dialog, which -> dialog.dismiss() }
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "YES"
                ) { dialog, which ->
                    if (auth.currentUser != null) {
                        auth.signOut()
                    }

                    //dialog.dismiss();
                }
                alertDialog.show()
                return true
            }

            else ->
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item)
        }
    }
}
