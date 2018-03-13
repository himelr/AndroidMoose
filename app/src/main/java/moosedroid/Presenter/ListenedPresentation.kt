package moosedroid.Presenter

import moosedroid.Models.Listened

/**
 * Created by HimelR on 02-Mar-18.
 */
//Updates the ui from presenter
interface ListenedPresentation {
    fun showListened(listenedList:List<Listened>)

    fun listenedAddedAt(position:Int)

    fun scrollTo(position: Int)

}