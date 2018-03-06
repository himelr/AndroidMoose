package moosedroid.Room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by HimelR on 06-Mar-18.
 */

public class ListenedViewModel extends AndroidViewModel {
    private LiveData<List<Listened>> listenedList;
    private Context context;
    private AppDatabase appDatabase;

    public ListenedViewModel(@NonNull Application application) {
        super(application);
        appDatabase.userDao().findUserByEmail("w");

    }

}
