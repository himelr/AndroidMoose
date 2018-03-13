package moosedroid.Presentation

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import com.acrcloud.rec.mooseb.R.id.item2
import moosedroid.Models.User


/**
 * Created by HimelR on 08-Feb-18.
 */
class CustomAdapter constructor(private val context: Context, private val item_layout: Int, private val users: List<User>): ListAdapter  {
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
        return users.size
    }

    override fun getItem(position: Int): Any {
        return users[position]
    }

    override fun getItemId(position: Int): Long {
        return users[position].id
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
        return users.isEmpty()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(item_layout, null)
        }

        val p = users[position]
        convertView!!.findViewById<TextView>(item2).text = p.email



        return convertView!!
    }
}