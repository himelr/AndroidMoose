package moosedroid.Presentation

import android.app.Activity
import com.acrcloud.rec.mooseb.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import moosedroid.Firebase.SignupFireActivity

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

@Module(subcomponents = arrayOf(UserSubComponent2::class))
abstract class UserModule2 {

    @Binds
    @IntoMap
    @ActivityKey(SignupFireActivity::class)
    internal abstract fun bindsToDoActivityInjectorFactory(builder: UserSubComponent2.Builder): AndroidInjector.Factory<out Activity>
}