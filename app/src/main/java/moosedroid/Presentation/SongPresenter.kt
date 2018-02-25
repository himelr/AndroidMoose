package moosedroid.Presentation

import moosedroid.Room.Song
import moosedroid.Room.SongDao
import javax.inject.Inject

/**
 * Created by HimelR on 24-Feb-18.
 */
class SongPresenter @Inject constructor(val songDao: SongDao)
{
    fun addNewSong(song: Song){
        songDao.insertSong(song)
    }

}