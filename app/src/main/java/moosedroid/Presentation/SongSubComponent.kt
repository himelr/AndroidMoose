package moosedroid.Presentation

import dagger.Subcomponent
import dagger.android.AndroidInjector
import moosedroid.Views.Main2Activity

/**
 * Created by HimelR on 24-Feb-18.
 */
@Subcomponent
interface SongSubComponent : AndroidInjector<Main2Activity> {

    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<Main2Activity>()
}