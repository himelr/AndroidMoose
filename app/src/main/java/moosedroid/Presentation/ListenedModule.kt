package moosedroid.Presentation

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import moosedroid.Views.Main2Activity
import moosedroid.Views.UserListenedActivity

/**
 * Created by HimelR on 24-Feb-18.
 */
@Module(subcomponents = arrayOf(ListenedSubComponent::class))
abstract class ListenedModule {

    @Binds
    @IntoMap
    @ActivityKey(Main2Activity::class)

    internal abstract fun bindsToDoActivityInjectorFactory(builder: ListenedSubComponent.Builder): AndroidInjector.Factory<out Activity>
}

@Module(subcomponents = arrayOf(ListenedSubComponent2::class))
abstract class ListenedModule2 {

    @Binds
    @IntoMap
    @ActivityKey(UserListenedActivity::class)
    internal abstract fun bindsToDoActivityInjectorFactory(builder: ListenedSubComponent2.Builder): AndroidInjector.Factory<out Activity>
}