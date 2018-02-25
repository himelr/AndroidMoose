package moosedroid.Presentation

import dagger.Component
import moosedroid.Room.AppDatabase
import moosedroid.Room.User
import moosedroid.UserApplication

/**
 * Created by HimelR on 17-Feb-18.
 */
@Component(modules = arrayOf(AppModule::class,
        UserModule::class,SongModule::class))

interface AppComponent {

    fun inject(application: UserApplication)
}