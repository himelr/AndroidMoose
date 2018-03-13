package moosedroid.Presenter

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import moosedroid.Views.*

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


@Module(subcomponents = arrayOf(ListenedSubComponent3::class,UserSubComponent4::class))
abstract class ListenedModule3 {

    @Binds
    @IntoMap
    @ActivityKey(ListenedSongActivity::class)
    internal abstract fun bindsToDoActivityInjectorFactory(builder: ListenedSubComponent3.Builder): AndroidInjector.Factory<out Activity>
    internal abstract fun bindsToDoActivityInjectorFactory2(builder: UserSubComponent4.Builder): AndroidInjector.Factory<out Activity>
}

@Module(subcomponents = arrayOf(ListenedSubComponent4::class))
abstract class ListenedModule4 {

    @Binds
    @IntoMap
    @ActivityKey(ListenedDetailActivity::class)
    internal abstract fun bindsToDoActivityInjectorFactory(builder: ListenedSubComponent4.Builder): AndroidInjector.Factory<out Activity>
}
@Module(subcomponents = arrayOf(ListenedSubComponent5::class))
abstract class ListenedModule5 {

    @Binds
    @IntoMap
    @ActivityKey(WebBoardActivity::class)
    internal abstract fun bindsToDoActivityInjectorFactory(builder: ListenedSubComponent5.Builder): AndroidInjector.Factory<out Activity>
}

