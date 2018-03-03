package moosedroid.Room

import android.arch.persistence.room.*
import io.reactivex.Flowable

/**
 * Created by HimelR on 24-Feb-18.
 */
@Dao
interface ListenedDao {

    @Query("select * from Listened where userId = :id")
    fun findSongsById(id: Long): Flowable<List<Listened>>


    @Insert()
    fun insertSong(listened: Listened)


    @Delete
    fun deleteSong(listened: Listened)
}