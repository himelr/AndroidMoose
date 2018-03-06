package moosedroid.Presentation

import android.util.Log
import android.widget.TextView
import moosedroid.Room.Listened


class ListenedAdapter(var listenedList: List<Listened>) : android.support.v7.widget.RecyclerView.Adapter<ListenedAdapter.ListenedViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, type: Int): ListenedViewHolder {
        return ListenedViewHolder(parent)
    }

    override fun onBindViewHolder(viewHolder: ListenedViewHolder, position: Int) {

        viewHolder.bind(listenedList[position])
        viewHolder.itemView.setOnClickListener({
            println("CLicked")
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