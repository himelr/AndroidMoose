package moosedroid.Views

import android.app.Fragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.acrcloud.rec.mooseb.R
import kotlinx.android.synthetic.main.activity_listened_song.view.*


/**
 * Created by HimelR on 05-Mar-18.
 */
class MapFragment : Fragment() {
    companion object {

        fun newInstance(): MapFragment {
            return newInstance()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle): View? {
        return inflater.inflate(R.layout.fragment_listened_map,
                container, false)
    }
}