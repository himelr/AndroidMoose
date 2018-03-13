package moosedroid.Presenter.Components

import dagger.Component
import moosedroid.Presenter.Modules.*
import moosedroid.UserApplication

/**
 * Created by HimelR on 17-Feb-18.
 */
//@Component: enable selected modules and used for performing dependency injection
@Component(modules = arrayOf(AppModule::class,
        UserModule::class, ListenedModule::class, ListenedModule2::class, ListenedModule3::class,
        ListenedModule4::class, ListenedModule5::class, UserModule2::class, UserModule3::class))

interface AppComponent {

    fun inject(application: UserApplication)
}