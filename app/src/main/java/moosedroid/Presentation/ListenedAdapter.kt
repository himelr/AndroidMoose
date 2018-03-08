package moosedroid.Presentation

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity

import android.util.Log
import android.widget.TextView
import moosedroid.Room.Listened
import moosedroid.Views.ListenedDetailActivity


class ListenedAdapter(var listenedList: List<Listened>,var context: Context) : android.support.v7.widget.RecyclerView.Adapter<ListenedAdapter.ListenedViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, type: Int): ListenedViewHolder {
        return ListenedViewHolder(parent)
    }

    override fun onBindViewHolder(viewHolder: ListenedViewHolder, position: Int) {

        viewHolder.bind(listenedList[position])
        viewHolder.itemView.setOnClickListener({
            val intent = Intent(context,ListenedDetailActivity::class.java)
            intent.putExtra("id",listenedList[position].id.toString() + "")
            intent.putExtra("userId",listenedList[position].userId.toString() + "")

            startActivity(context,intent,null)
        })
    }

    override fun getItemCount(): Int = listenedList.size

    inner class ListenedViewHolder(parent: android.view.ViewGroup) : android.support.v7.widget.RecyclerView.ViewHolder(android.view.LayoutInflater.from(parent.context).inflate(com.acrcloud.rec.mooseb.R.layout.president_item, parent, false)) {

        fun bind(listened: Listened) = with(itemView) {
            //val taskCb = findViewById(R.id.recycleUser) as android.widget.CheckBox
            val listenedView = findViewById<TextView>(com.acrcloud.rec.mooseb.R.id.item2)
            listenedView.text = listened.title + "--" + listened.artist

        }
    }
}