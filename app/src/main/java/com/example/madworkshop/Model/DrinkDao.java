package com.example.madworkshop.Model;

import androidx.lifecycle.LiveData; import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy; import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DrinkDao {
    //This is an interface. All these methods would normally need to be implemented manually, but since we are using the Room framework, we don't have to.

    @Insert
    void insertDrink(Drink drink);

    @Query("DELETE FROM Drink")
    void deleteAll();

    @Query("SELECT * from Drink ORDER BY Name ASC")
    LiveData<List<Drink>> getAllDrinks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDrinks(List<Drink> drinks);

    @Update
    void updateDrink(Drink drink);

    @Delete
    void deleteDrink(Drink drink);

    @Query("SELECT * from Drink")
    public LiveData<List<Drink>> monitorAllDrinks();

    @Query("SELECT * from Drink WHERE drinkType = :type")
    Drink loadDrinkByType(String type);

    @Delete
    void deleteTwoDrinks(Drink drinkOne, Drink drinkTwo);
}
