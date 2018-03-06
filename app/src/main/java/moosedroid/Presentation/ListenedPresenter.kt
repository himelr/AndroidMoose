package moosedroid.Presentation

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moosedroid.Room.Listened
import moosedroid.Room.ListenedDao
import javax.inject.Inject
import io.reactivex.Observable
/**
 * Created by HimelR on 24-Feb-18.
 */
class ListenedPresenter @Inject constructor(val listenedDao: ListenedDao) {
    val compositeDisposable = CompositeDisposable()
    var listenedList = ArrayList<Listened>()
    var presentation: ListenedPresentation? = null


    fun onCreate(listenedPresentation: ListenedPresentation, id : Long){

        presentation = listenedPresentation
        loadSongs(id)
    }

    fun addNewSong(listened: Listened){
        listenedDao.insertSong(listened)
        Log.d("test2","song added")
    }

    fun loadSongs(id: Long){
        compositeDisposable.add(listenedDao.findSongsById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                   // Log.d("test2", it[0].name)
                    listenedList.clear()
                    listenedList.addAll(it)
                    for(i in listenedList){
                       // Log.d("test2", i.genre)
                    }
                    (listenedList.size - 1).takeIf { it >= 0 } ?.let {

                        presentation?.listenedAddedAt(it)
                        presentation?.scrollTo(it)

                    }
                    presentation?.showListened(listenedList)
                }))
       // Log.d("test2", "rd")


    }
}