package foodapp.com.data;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * JSON字符串和List列表数据转化
 */
public class FoodEntityConverters
{
    @TypeConverter
    public static ArrayList<String> fromString(String value)
    {
        Type listType = new TypeToken<ArrayList<String>>()
        {}.getType();
        return new Gson().fromJson(value,
                                   listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list)
    {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}