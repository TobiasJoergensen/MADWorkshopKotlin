package com.example.madworkshop.Model

import android.app.Application
import androidx.lifecycle.LiveData

class DrinkRepository(application: Application?) {
    //We create our drink dao and a list of drinks
    private var mDrinkDao: DrinkDao? = null
    private var mAllDrinks: LiveData<List<Drink?>?>? = null

    //The constructor for the repository
    init {
        //We get the database from our Database class
        val db = application?.let { DrinkRoomDatabase.getDatabase(it) }
        //We instantiate our drinkDatabase
        if (db != null) {
            mDrinkDao = db.drinkDao()
        }
    }

    val allDrinks: LiveData<List<Drink?>?>?
        get() {
            //Get all the drinks in our database and then return them.
            mAllDrinks = mDrinkDao?.allDrinks
            return mAllDrinks
        }

    fun insertDrink(drink: Drink?) {
        //Insert a new drink into our database
        DrinkRoomDatabase.databaseWriteExecutor.execute { mDrinkDao?.insertDrink(drink) }
    }
}
