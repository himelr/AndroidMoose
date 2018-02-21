package moosedroid.Room

import android.view.View
import android.widget.TextView
import com.acrcloud.rec.mooseb.R
import com.acrcloud.rec.mooseb.R.id.*
import kotlinx.android.synthetic.main.activity_test_user.view.*

/**
 * Created by HimelR on 18-Feb-18.
 */
    class UserAdapter(var users: List<User>) : android.support.v7.widget.RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup, type: Int): UserViewHolder {
        return UserViewHolder(parent)
    }

    override fun onBindViewHolder(viewHolder: UserViewHolder, position: Int) {

        viewHolder.bind(users[position])
        viewHolder.itemView.setOnClickListener({
            println("CLicked")
        })
    }

    override fun getItemCount(): Int = users.size

    inner class UserViewHolder(parent: android.view.ViewGroup) : android.support.v7.widget.RecyclerView.ViewHolder(android.view.LayoutInflater.from(parent.context).inflate(com.acrcloud.rec.mooseb.R.layout.president_item, parent, false)) {

        fun bind(user: User) = with(itemView) {
            //val taskCb = findViewById(R.id.recycleUser) as android.widget.CheckBox
            val userView = findViewById<TextView>(com.acrcloud.rec.mooseb.R.id.item2)
            userView.text = user.email
        }
    }
}