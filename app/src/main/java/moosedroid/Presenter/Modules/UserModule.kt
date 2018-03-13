package moosedroid.Presenter.Modules

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import moosedroid.Firebase.SignupFireActivity
import moosedroid.Presenter.Components.ListenedSubComponent6
import moosedroid.Presenter.Components.UserSubComponent
import moosedroid.Presenter.Components.UserSubComponent2
import moosedroid.Presenter.Components.UserSubComponent3
import moosedroid.Presenter.UserActivity
import moosedroid.Views.MenuBaseActivity

/**
 * Created by HimelR on 17-Feb-18.
 */
//Provide dependencies to activities
@Module(subcomponents = arrayOf(UserSubComponent::class))
abstract class UserModule {

    @Binds
    @IntoMap
    @ActivityKey(UserActivity::class)
    internal abstract fun bindsToDoActivityInjectorFactory(builder: UserSubComponent.Builder): AndroidInjector.Factory<out Activity>
}

@Module(subcomponents = arrayOf(UserSubComponent2::class))
abstract class UserModule2 {

    @Binds
    @IntoMap
    @ActivityKey(SignupFireActivity::class)
    internal abstract fun bindsToDoActivityInjectorFactory(builder: UserSubComponent2.Builder): AndroidInjector.Factory<out Activity>
}
@Module(subcomponents = arrayOf(UserSubComponent3::class, ListenedSubComponent6::class))
abstract class UserModule3 {

    @Binds
    @IntoMap
    @ActivityKey(MenuBaseActivity::class)
    internal abstract fun bindsToDoActivityInjectorFactory(builder: UserSubComponent3.Builder): AndroidInjector.Factory<out Activity>
    internal abstract fun bindsToDoActivityInjectorFactory2(builder: ListenedSubComponent6.Builder): AndroidInjector.Factory<out Activity>

}