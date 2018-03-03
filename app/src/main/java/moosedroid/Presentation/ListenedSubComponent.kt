package moosedroid.Presentation

import dagger.Subcomponent
import dagger.android.AndroidInjector
import moosedroid.Views.Main2Activity
import moosedroid.Views.UserListenedActivity

/**
 * Created by HimelR on 24-Feb-18.
 */
@Subcomponent
interface ListenedSubComponent : AndroidInjector<Main2Activity> {

    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<Main2Activity>()
}

@Subcomponent
interface ListenedSubComponent2 : AndroidInjector<UserListenedActivity> {

    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<UserListenedActivity>()
}