package moosedroid.Room

import android.arch.persistence.room.*
import io.reactivex.Flowable

/**
 * Created by HimelR on 24-Feb-18.
 */
@Dao
interface SongDao {

    @Query("select * from Song where userId = :id")
    fun findSongsById(id: Long): List<Song>

    @Insert()
    fun insertSong(song: Song)


    @Delete
    fun deleteSong(song: Song)
}