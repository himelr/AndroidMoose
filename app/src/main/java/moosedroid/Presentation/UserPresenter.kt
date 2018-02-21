package moosedroid.Presentation

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moosedroid.Room.User
import moosedroid.Room.UserDao
import java.util.*
import io.reactivex.Observable
import javax.inject.Inject

class UserPresenter @Inject constructor(val userDao: UserDao) {

    var users = ArrayList<User>()
    val compositeDisposable = CompositeDisposable()


    var presentation: UserPresentation? = null

    fun onCreate(userPresentation: UserPresentation) {
        presentation = userPresentation
        loadusers()
    }
    //Avoid memory leaks
    @Override
    fun onDestroy() {
        compositeDisposable.dispose()
        presentation = null
    }

    fun loadusers() {
        compositeDisposable.add(userDao.getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    users.clear()
                    users.addAll(it)
                    (users.size - 1).takeIf { it >= 0 }?.let {
                        presentation?.userAddedAt(it)
                        presentation?.scrollTo(it)
                    }
                }))

        presentation?.showUsers(users)
    }

    fun addNewUser(email: String) {
        val newUser = User(email)
        compositeDisposable.add(Observable.fromCallable { userDao.insertUser(newUser) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
        }
    }
