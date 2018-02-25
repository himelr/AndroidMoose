package moosedroid.Presentation

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import moosedroid.Views.Main2Activity

/**
 * Created by HimelR on 24-Feb-18.
 */
@Module(subcomponents = arrayOf(SongSubComponent::class))
abstract class SongModule {

    @Binds
    @IntoMap
    @ActivityKey(Main2Activity::class)
    internal abstract fun bindsToDoActivityInjectorFactory(builder: SongSubComponent.Builder): AndroidInjector.Factory<out Activity>
}