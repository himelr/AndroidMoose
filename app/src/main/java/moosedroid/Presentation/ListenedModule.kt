package moosedroid.Presentation

import android.app.Activity
import android.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.android.FragmentKey
import dagger.multibindings.IntoMap
import moosedroid.Views.*
import moosedroid.Views.Fragments.ListenedFragment

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


@Module(subcomponents = arrayOf(ListenedSubComponent3::class))
abstract class ListenedModule3 {

    @Binds
    @IntoMap
    @ActivityKey(ListenedSongActivity::class)
    internal abstract fun bindsToDoActivityInjectorFactory(builder: ListenedSubComponent3.Builder): AndroidInjector.Factory<out Activity>
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
/*
@Module(subcomponents = arrayOf(ListenedSubComponent6::class))
abstract class ListenedModule6 {

    @Binds
    @IntoMap
    @ActivityKey(MenuBaseActivity::class)
    internal abstract fun bindsToDoActivityInjectorFactory(builder: ListenedSubComponent6.Builder): AndroidInjector.Factory<out Activity>
}*/
