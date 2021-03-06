package foodapp.com.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import foodapp.com.data.model.FoodItem
import foodapp.com.data.store.local.FoodDao

@Database(entities = [(FoodItem::class)], version = 1, exportSchema = false)
@TypeConverters(FoodEntityConverters::class)
abstract class FoodDatabase : RoomDatabase() {
    /**
     * 获取食品列表表
     * @return
     */
    abstract fun foodItemsDao(): FoodDao
}
