package moosedroid.Room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * Created by HimelR on 17-Feb-18.
 */

@Database(entities = arrayOf(User::class,Song::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun songDao(): SongDao


}