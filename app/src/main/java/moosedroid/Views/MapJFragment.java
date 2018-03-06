package moosedroid.Views;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acrcloud.rec.mooseb.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapJFragment extends Fragment implements OnMapReadyCallback {


    public MapJFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_listened_map, container, false);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
