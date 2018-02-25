package moosedroid.Presentation

import android.app.Activity
import com.acrcloud.rec.mooseb.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by HimelR on 17-Feb-18.
 */
@Module(subcomponents = arrayOf(UserSubComponent::class))
abstract class UserModule {

    @Binds
    @IntoMap
    @ActivityKey(TestUser::class)
    internal abstract fun bindsToDoActivityInjectorFactory(builder: UserSubComponent.Builder): AndroidInjector.Factory<out Activity>
}