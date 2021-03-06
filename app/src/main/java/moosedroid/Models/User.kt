package moosedroid.Models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by HimelR on 17-Feb-18.
 */
//Data class for user
@Entity(tableName = "User")
data class User(@ColumnInfo(name="email")var email: String){

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0

}