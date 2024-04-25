package com.example.madworkshop.Model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DrinkDao {
    //This is an interface. All these methods would normally need to be implemented manually, but since we are using the Room framework, we don't have to.
    @Insert
    fun insertDrink(drink: Drink?)

    @Query("DELETE FROM Drink")
    fun deleteAll()

    @JvmField
    @get:Query("SELECT * from Drink ORDER BY Name ASC")
    val allDrinks: LiveData<List<Drink?>?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDrinks(drinks: List<Drink?>?)

    @Update
    fun updateDrink(drink: Drink?)

    @Delete
    fun deleteDrink(drink: Drink?)

    @Query("SELECT * from Drink")
    fun monitorAllDrinks(): LiveData<List<Drink?>?>?

    @Query("SELECT * from Drink WHERE drinkType = :type")
    fun loadDrinkByType(type: String?): Drink?

    @Delete
    fun deleteTwoDrinks(drinkOne: Drink?, drinkTwo: Drink?)
}
