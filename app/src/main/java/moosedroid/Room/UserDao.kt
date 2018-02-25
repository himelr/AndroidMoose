package moosedroid.Room

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import io.reactivex.Flowable

/**
 * Created by HimelR on 17-Feb-18.
 */
@Dao interface UserDao {
    @Query("select * from User")
        fun getAllUsers(): Flowable<List<User>>

    @Query("select * from User where id = :id")
    fun findUserById(id: Long): User

    @Query("select * from User where email = :id")
    fun findUserByEmail(id: String): User

    @Insert(onConflict = REPLACE)
    fun insertUser(User: User)


    @Update(onConflict = REPLACE)
    fun updateUser(User: User)

    @Delete
    fun deleteUser(User: User)
}