package moosedroid.Presenter

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moosedroid.Models.Listened
import moosedroid.Room.ListenedDao
import javax.inject.Inject

/**
 * Created by HimelR on 24-Feb-18.
 */
//Handles the business logic and interaction with data base. Rx Java
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
    //Adds a song
    fun addNewSong(listened: Listened) {
        listenedDao.insertSong(listened)
        Log.d("test2", "song added")
    }
    //Returns listened
    fun getListened(id: Long): Listened? {

        for (listened in listenedList) {
            if (listened.id == id) {
                return listened
            }
        }
        return null
    }
    //Deletes all
    fun deleteUsersListened(id: Long) {
        listenedDao.deleteById(id)
        listenedList.clear()
        presentation?.showListened(emptyList())
    }
    //Gets latest added listened. Start detail activity.
    fun getLatest() {

        compositeDisposable.add(listenedDao.findNewest()

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    startIntent?.startIntent(id = it.id)
                }))
    }
    //loads all the songs. Calls presentation
    fun loadSongs(id: Long) {
        compositeDisposable.add(listenedDao.findSongsById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listenedList.clear()
                    listenedList.addAll(it)
                    for (i in listenedList) {
                    }
                    (listenedList.size - 1).takeIf { it >= 0 }?.let {

                        presentation?.listenedAddedAt(it)
                        presentation?.scrollTo(it)

                    }
                    presentation?.showListened(listenedList)
                }))
    }

    fun onDestroy() {
        compositeDisposable.dispose()
        presentation = null
    }

    //start the intent
    interface StartIntent {
        fun startIntent(id: Long)
    }
}