package moosedroid.Room

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by HimelR on 21-Feb-18.
 */
//foreignKeys = @ForeignKey(entity = User::class,parentColumns = "email",childColumns = "userId",onDelete = CASCADE)
@Entity(tableName = "Listened",foreignKeys = [(ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("userId"), onDelete = CASCADE))])


data class Listened(@ColumnInfo(name = "title") var title: String, @ColumnInfo(name = "artist") var artist: String, @ColumnInfo(name = "genre") var genre: String, @ColumnInfo(name = "album") var album: String, @ColumnInfo(name = "userId")
var userId:Long, @ColumnInfo(name = "latitude") var latitude:Double? = 0.0,  @ColumnInfo(name = "longitude") var longitude:Double? = 0.0,@ColumnInfo(name = "youtubeId") var youtubeId:String?) {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "date")
    var date: String? = null

    init {
        date = TiviTypeConverters.fromOffsetDateTime(OffsetDateTime.now())
    }

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