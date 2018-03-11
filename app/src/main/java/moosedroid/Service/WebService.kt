package moosedroid.Service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import com.acrcloud.rec.mooseb.R
import moosedroid.Views.WebBoardActivity
import org.json.JSONObject

/**
 * Created by HimelR on 09-Mar-18.
 */
class WebService(val name:String) : IntentService(name) {

    val URL = "https://moosebeat.herokuapp.com/api/albums/get/all"
    val NOTIFICATION = "com.moosebeat"

    constructor() : this("WebService")

    override fun onHandleIntent(intent: Intent?) {
        val jParser = JSONParser()
        val json = jParser.getJSONFromUrl2(URL)
        publishResults(json)


    }

    private fun publishResults(json: String?) {
        val intent = Intent(NOTIFICATION)
        intent.putExtra("json", json ?: "")
        notifier()
        sendBroadcast(intent)
    }

    private fun notifier() {

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        val id = "my_channel_01"

        val name = getString(R.string.app_name)

        val description = "Data loaded"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(id, name, importance)
        mChannel.description = description
        mChannel.enableLights(true)
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)
        mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        mNotificationManager!!.createNotificationChannel(mChannel)
        val mBuilder = NotificationCompat.Builder(this, id)
                .setSmallIcon(R.drawable.ic_file_download_black_24dp)
                .setContentTitle("Web")
                .setContentText("Download complete")
        val resultIntent = Intent(this, WebBoardActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(WebBoardActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        mBuilder.setContentIntent(resultPendingIntent)

        mNotificationManager.notify(3, mBuilder.build())

    }
}