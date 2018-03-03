package moosedroid.Presentation

import dagger.Component
import moosedroid.UserApplication

/**
 * Created by HimelR on 17-Feb-18.
 */
@Component(modules = arrayOf(AppModule::class,
        UserModule::class, ListenedModule::class,ListenedModule2::class))

interface AppComponent {

    fun inject(application: UserApplication)
}