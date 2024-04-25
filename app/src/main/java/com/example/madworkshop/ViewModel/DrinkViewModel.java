package com.example.madworkshop.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.madworkshop.Model.Drink;
import com.example.madworkshop.Model.DrinkRepository;
import java.util.List;

public class DrinkViewModel extends AndroidViewModel {
    //This is a viewModel. This is used to handle logic in applications.
    //Often we should avoid having logic in our activities and instead move it into our viewModels.
    private LiveData<List<Drink>> allDrinks;
    private DrinkRepository mRepository;

    public DrinkViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DrinkRepository(application);
        allDrinks = mRepository.getAllDrinks();
    }

    //This is a method we can use to get a list of all the drinks we have in our database.
    public LiveData<List<Drink>> getAllDrinks() {
        return allDrinks;
    }

    //This is a method we can use to insert a new drink into our database.
    public void insertDrink(Drink drink) {
        mRepository.insertDrink(drink);
    }
}
