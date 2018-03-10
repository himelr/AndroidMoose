package moosedroid.Presentation

import dagger.Subcomponent
import dagger.android.AndroidInjector
import moosedroid.Views.*
import moosedroid.Views.Fragments.ListenedFragment

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
@Subcomponent
interface ListenedSubComponent3 : AndroidInjector<ListenedSongActivity> {

    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<ListenedSongActivity>()
}
@Subcomponent
interface ListenedSubComponent4 : AndroidInjector<ListenedDetailActivity> {

    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<ListenedDetailActivity>()
}
@Subcomponent
interface ListenedSubComponent5 : AndroidInjector<WebBoardActivity> {

    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<WebBoardActivity>()
}
/*
@Subcomponent
interface ListenedSubComponent6 : AndroidInjector<MenuBaseActivity> {

    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<MenuBaseActivity>()
}*/
