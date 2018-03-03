package moosedroid.Room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by HimelR on 17-Feb-18.
 */

@Database(entities = arrayOf(User::class, Listened::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun listenedDao(): ListenedDao


}