package moosedroid.Views

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acrcloud.rec.mooseb.R



/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ListenedMapFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ListenedMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListenedMapFragment : Fragment() {

    // TODO: Rename and change types of parameters


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listened_map, container, false)
    }



}// Required empty public constructor
