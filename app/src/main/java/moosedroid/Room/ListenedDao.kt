package moosedroid.Room

import android.arch.persistence.room.*
import io.reactivex.Flowable
import moosedroid.Models.Listened


/**
 * Created by HimelR on 24-Feb-18.
 */
@Dao
interface ListenedDao {

    @Query("select * from Listened where userId = :id")
    fun findSongsById(id: Long): Flowable<List<Listened>>

    @Query("SELECT * FROM Listened ORDER BY id DESC LIMIT 1;")
    fun findNewest() : Flowable<Listened>

    @Query("DELETE FROM Listened WHERE userId = :id")
    fun deleteById(id: Long)

    @Insert()
    fun insertSong(listened: Listened)


    @Delete
    fun deleteSong(listened: Listened)
}