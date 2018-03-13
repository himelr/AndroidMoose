package moosedroid.Presenter.Components

import dagger.Subcomponent
import dagger.android.AndroidInjector
import moosedroid.Firebase.SignupFireActivity
import moosedroid.Presenter.TestUser
import moosedroid.Views.ListenedSongActivity
import moosedroid.Views.MenuBaseActivity

/**
 * Created by HimelR on 17-Feb-18.
 */
@Subcomponent interface UserSubComponent : AndroidInjector<TestUser> {

    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<TestUser>()
}

@Subcomponent interface UserSubComponent2 : AndroidInjector<SignupFireActivity> {

    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<SignupFireActivity>()
}

@Subcomponent interface UserSubComponent3 : AndroidInjector<MenuBaseActivity> {

    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<MenuBaseActivity>()
}
@Subcomponent interface UserSubComponent4 : AndroidInjector<ListenedSongActivity> {

    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<ListenedSongActivity>()
}

