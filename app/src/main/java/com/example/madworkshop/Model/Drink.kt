package com.example.madworkshop.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//This is a model. This is how we will build out drinks.
@Entity(tableName = "Drink")
class Drink //This is the constructor. When we want to make a new drink, we can pass on the name and price.
//Hint: See method 'createModel' in our Menu activity.
(var name: String, var price: Int, @field:ColumnInfo(name = "drinkType") var type: String) {
    //This is the type of variables we have on our drink. We could also add other things here such as alcohol percentage or an image resource.
    @kotlin.jvm.JvmField
    @PrimaryKey(autoGenerate = true)
    var id = 0

}
