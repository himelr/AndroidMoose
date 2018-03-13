package moosedroid.Service


import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import com.acrcloud.rec.mooseb.R
import com.acrcloud.rec.mooseb.R.id.*
import com.android.volley.toolbox.NetworkImageView
import moosedroid.Models.WebSong


/**
 * Created by HimelR on 08-Feb-18.
 */
class WebAdapter constructor(private val context: Context, private val item_layout: Int, private val songs: List<WebSong>) : ListAdapter {
    private val loadImage = LoadImage(context)

    private val observerChangeSet: MutableSet<DataSetObserver> = hashSetOf()

    override fun areAllItemsEnabled(): Boolean {
        return true
    }

    override fun isEnabled(position: Int): Boolean {
        return true
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {
        observerChangeSet.add(observer)

    }

    override fun unregisterDataSetObserver(observer: DataSetObserver) {
        observerChangeSet.remove(observer)

    }

    override fun getCount(): Int {
        return songs.size
    }

    override fun getItem(position: Int): Any {
        return songs[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun isEmpty(): Boolean {
        return songs.isEmpty()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(item_layout, null)
        }

        val p = songs[position]
        convertView!!.findViewById<TextView>(item2).text = p.title
        convertView!!.findViewById<TextView>(item3).text = p.artist
        convertView!!.findViewById<TextView>(item4).text = p.count.toString() + " added"

        val avatar = convertView!!.findViewById(R.id.cover) as NetworkImageView
        avatar.setImageUrl(p.img, loadImage.getmImageLoader())

        return convertView!!
    }

}