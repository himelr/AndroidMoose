package moosedroid

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import moosedroid.Presenter.Components.AppComponent
import moosedroid.Presenter.Components.DaggerAppComponent
import moosedroid.Presenter.Modules.AppModule
import javax.inject.Inject

/**
 * Created by HimelR on 17-Feb-18.
 */
//Application injectors
class UserApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().appModule(AppModule(applicationContext)).build()

        appComponent.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

}