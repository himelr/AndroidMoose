package moosedroid.Presentation

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import moosedroid.Room.User

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by HimelR on 21-Feb-18.
 */
//foreignKeys = @ForeignKey(entity = User::class,parentColumns = "email",childColumns = "userId",onDelete = CASCADE)
@Entity(tableName = "Song")
data class Song(@ColumnInfo(name = "name") var name: String, @ColumnInfo(name = "genre") var genre: String) {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "date")
    var date: String? = null

    init {
        date = TiviTypeConverters.fromOffsetDateTime(OffsetDateTime.now())
    }
    var userId:String? = null

    object TiviTypeConverters {
        private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        @TypeConverter
        @JvmStatic
        fun toOffsetDateTime(value: String?): OffsetDateTime? {
            return value?.let {
                return formatter.parse(value, OffsetDateTime::from)
            }
        }

        @TypeConverter
        @JvmStatic
        fun fromOffsetDateTime(date: OffsetDateTime?): String? {
            return date?.format(formatter)
        }
    }

}