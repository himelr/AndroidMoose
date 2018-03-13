package moosedroid.Presentation

import android.os.SystemClock
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moosedroid.Room.Listened
import moosedroid.Room.ListenedDao
import javax.inject.Inject
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by HimelR on 24-Feb-18.
 */
class ListenedPresenter @Inject constructor(private val listenedDao: ListenedDao) {
    private val compositeDisposable = CompositeDisposable()
    var listenedList = ArrayList<Listened>()
    var presentation: ListenedPresentation? = null
    var startIntent: StartIntent? = null

    fun onCreate(listenedPresentation: ListenedPresentation, id: Long, startIntent: StartIntent? = null) {

        presentation = listenedPresentation
        this.startIntent = startIntent
        loadSongs(id)
    }

    fun addNewSong(listened: Listened) {
        listenedDao.insertSong(listened)
        Log.d("test2", "song added")
    }

    fun getListened(id: Long): Listened? {

        for (listened in listenedList) {
            if (listened.id == id) {
                return listened
            }
        }
        return null
    }

    fun deleteUsersListened(id: Long) {
        listenedDao.deleteById(id)
        presentation?.showListened(emptyList())
    }

    fun getLatest() {

        compositeDisposable.add(listenedDao.findNewest()

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    startIntent?.startIntent(id = it.id)
                }))
    }

    fun loadSongs(id: Long) {
        compositeDisposable.add(listenedDao.findSongsById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Log.d("test2", it[0].name)
                    listenedList.clear()
                    listenedList.addAll(it)
                    for (i in listenedList) {
                        // Log.d("test2", i.genre)
                    }
                    (listenedList.size - 1).takeIf { it >= 0 }?.let {

                        presentation?.listenedAddedAt(it)
                        presentation?.scrollTo(it)

                    }
                    presentation?.showListened(listenedList)
                }))
    }

    interface StartIntent {
        fun startIntent(id: Long)
    }
}