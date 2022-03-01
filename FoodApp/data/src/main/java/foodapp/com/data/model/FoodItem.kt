package foodapp.com.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * 食品列表表
 * @property id
 * @property profileImage
 * @property name
 * @property heroImage
 * @property votes
 * @property foodImages
 * @property foodDescription
 * @property date
 */
@Entity(tableName = "food_item")
@Parcelize
data class FoodItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val profileImage: String,
    val name: String,
    val heroImage: String,
    val votes: Int,
    val foodImages: ArrayList<String> = arrayListOf(),
    val foodDescription: String,
    var date: String
) : Parcelable