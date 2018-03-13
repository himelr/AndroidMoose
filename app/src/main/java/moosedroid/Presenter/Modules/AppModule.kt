package moosedroid.Presenter.Modules

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import moosedroid.Room.AppDatabase

/**
 * Created by HimelR on 17-Feb-18.
 */
//@Module annotation tells Dagger that the AppModule class will provide dependencies for a part of the application.
@Module
class AppModule(private val context: Context) {

    @Provides
    fun providesAppContext() = context

    @Provides
    fun providesAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "moosedroid-db").allowMainThreadQueries().build()

    @Provides
    fun providesUserDao(database: AppDatabase) = database.userDao()
    @Provides
    fun providesSongDao(database: AppDatabase) = database.listenedDao()

}