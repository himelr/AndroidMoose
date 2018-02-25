package moosedroid.Presentation

import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created by HimelR on 17-Feb-18.
 */
@Subcomponent interface UserSubComponent : AndroidInjector<TestUser> {

    @Subcomponent.Builder abstract class Builder : AndroidInjector.Builder<TestUser>()
}
