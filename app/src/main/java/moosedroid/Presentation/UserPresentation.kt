package moosedroid.Presentation

import moosedroid.Room.User

/**
 * Created by HimelR on 17-Feb-18.
 */

interface UserPresentation{
    fun showUsers(users:List<User>)

    fun userAddedAt(position:Int)

    fun scrollTo(position: Int)

}