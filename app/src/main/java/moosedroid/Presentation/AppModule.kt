package moosedroid.Presentation

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import moosedroid.Room.AppDatabase

/**
 * Created by HimelR on 17-Feb-18.
 */
@Module
class AppModule(private val context: Context) {

    @Provides
    fun providesAppContext() = context

    @Provides
    fun providesAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "moosedroid-db").allowMainThreadQueries().build()

    @Provides
    fun providesToDoDao(database: AppDatabase) = database.userDao()
    @Provides
    fun providesSongDao(database: AppDatabase) = database.songDao()

}