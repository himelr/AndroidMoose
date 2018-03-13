package moosedroid.Views

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import com.acrcloud.rec.mooseb.R
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_web_board.*
import moosedroid.Models.Listened
import moosedroid.Service.WebAdapter
import moosedroid.Service.WebService
import moosedroid.Models.WebSong
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList

class WebBoardActivity : MenuBaseActivity() {

    override fun setBottomBar() {
        bottomBar = findViewById(R.id.include)
        setItems()
    }

    private var songList: List<WebSong>? = null

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val bundle = intent.extras
            if (bundle != null) {
                val json = bundle.getString("json")
                try {
                    list(JSONArray(json))

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        }
    }

    override fun getLayoutResourceId(): Int {
        return  R.layout.activity_web_board
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        upVar = true
        super.onCreate(savedInstanceState)
        val intent = Intent(this, WebService::class.java)
        startService(intent)
    }

    fun list(json: JSONArray)  {
        try {

            val tempList = ArrayList<WebSong>()


            for (i in 0 until json.length()) {
                val rec = json.getJSONObject(i)
                val title = rec.getString("title")
                val artist = rec.getString("artist")
                val added = rec.getInt("added")

                try {
                    val cover = rec.getString("cover")
                    tempList.add(WebSong(artist = artist, count = added, img = cover, title = title))
                }
                catch (e:JSONException){}

                Log.d("test2",title)

            }

            songList = tempList
            webList.adapter = WebAdapter(applicationContext,R.layout.list_item,songList!!)

        } catch (e: JSONException) {
            e.printStackTrace()
        }


    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, IntentFilter(
                WebService().NOTIFICATION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }
    override fun showListened(listenedList: List<Listened>) {}

    override fun listenedAddedAt(position: Int) {}

    override fun scrollTo(position: Int) {}

}
