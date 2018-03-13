package moosedroid.Room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import moosedroid.Models.Listened
import moosedroid.Models.User

/**
 * Created by HimelR on 17-Feb-18.
 */
//Database
@Database(entities = arrayOf(User::class, Listened::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun listenedDao(): ListenedDao



}