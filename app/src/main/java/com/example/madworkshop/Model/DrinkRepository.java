package com.example.madworkshop.Model;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class DrinkRepository {
    //We create our drink dao and a list of drinks
    private DrinkDao mDrinkDao;
    private LiveData<List<Drink>> mAllDrinks;

    //The constructor for the repository
    public DrinkRepository(Application application) {
        //We get the database from our Database class
        DrinkRoomDatabase db = DrinkRoomDatabase.getDatabase(application);
        //We instantiate our drinkDatabase
        mDrinkDao = db.drinkDao();
    }

    public LiveData<List<Drink>> getAllDrinks() {
        //Get all the drinks in our database and then return them.
        mAllDrinks = mDrinkDao.getAllDrinks();
        return mAllDrinks;
    }

    public void insertDrink(Drink drink) {
        //Insert a new drink into our database
        DrinkRoomDatabase.databaseWriteExecutor .execute(() -> {
            mDrinkDao.insertDrink(drink);
        });
    }
}
