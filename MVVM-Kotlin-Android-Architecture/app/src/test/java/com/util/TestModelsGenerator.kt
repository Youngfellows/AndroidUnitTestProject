package com.util

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.task.data.dto.recipes.Recipes
import com.task.data.dto.recipes.RecipesItem
import com.task.data.remote.moshiFactories.MyKotlinJsonAdapterFactory
import com.task.data.remote.moshiFactories.MyStandardJsonAdapters
import java.io.File
import java.lang.reflect.Type


/**
 * 数据模型创建器
 */
class TestModelsGenerator {

    private var recipes: Recipes = Recipes(arrayListOf())

    /**
     * 读取本地JSON文件并转化为对象
     */
    init {
        val moshi = Moshi.Builder()
            .add(MyKotlinJsonAdapterFactory())
            .add(MyStandardJsonAdapters.FACTORY)
            .build()
        val type: Type = Types.newParameterizedType(List::class.java, RecipesItem::class.java)
        val adapter: JsonAdapter<List<RecipesItem>> = moshi.adapter(type)
        val jsonString = getJson("RecipesApiResponse.json")
        adapter.fromJson(jsonString)?.let {
            recipes = Recipes(ArrayList(it))
        }
        print("this is $recipes")
    }

    /**
     * @return 返回菜谱实体
     */
    fun generateRecipes(): Recipes {
        return recipes
    }

    /**
     * @return 返回创建空的菜谱实体数据
     */
    fun generateRecipesModelWithEmptyList(): Recipes {
        return Recipes(arrayListOf())
    }

    /**
     * 返回获取菜谱实体项
     * @return
     */
    fun generateRecipesItemModel(): RecipesItem {
        return recipes.recipesList[0]
    }

    /**
     * 返回获取搜索的菜谱名称
     * @return
     */
    fun getStubSearchTitle(): String {
        return recipes.recipesList[0].name
    }


    /**
     * 读取本地的JSON文件
     * Helper function which will load JSON from
     * the path specified
     *
     * @param path : Path of JSON file
     * @return json : JSON from file at given path
     */
    private fun getJson(path: String): String {
        // Load the JSON response
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }
}
