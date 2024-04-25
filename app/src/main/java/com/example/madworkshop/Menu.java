package com.example.madworkshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.madworkshop.Model.Drink;
import com.example.madworkshop.ViewModel.DrinkViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;

public class Menu extends AppCompatActivity {

    //This is global variables of this class. You can access these anywhere in this class.
    //You can add accessors like private or public to enable the variables to be used outside of this class.
    //Hint: If you have an input variable in a method with the same name as a global variable, you can use the 'this' keyword to use the global variable
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Drink> drink;
    private List<Drink> drinksDb;
    private static Context context;
    private static final Integer NEW_DRINK_ACTIVITY_REQUEST_CODE = 1;
    private DrinkViewModel drinkViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.context = getApplicationContext();

        //We like to break things down to methods. It's much cleaner!
        CreateDrinkList();
        CreateRecyclerView();
    }

    private void CreateRecyclerView() {
        //We assign to the recycler view variable we have.
        this.recyclerView = (RecyclerView) findViewById(R.id.rv_menu);
        //This is a performance thing. If the recyclerview is not gonna change, we can save some computation time.
        this.recyclerView.setHasFixedSize(true);

        //This is line splitting each item. Here view just have a straigh vertical line.
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        this.recyclerView.addItemDecoration(itemDecoration);

        //We are using a linear layout manager, because we want each of these items to be shown in a list vertically. Se also that we assign the order of the manager to be vertical.
        //Hint: Try to change it to horizontal just for fun! Remember to scroll. Now you can also make a horizontal scroll list.
        this.layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        this.recyclerView.setLayoutManager(layoutManager);

        //The recyclerview needs an adapter, otherwise it's just an empty list.
        //We have created a class just for this specific purpose.
        this.mAdapter = new MenuAdapter(drink, new MenuAdapter.OnItemClickListener() {
            //We have implemented a click listener on our items for our recyclerView. Here we are using an anonymous function to show that the item was clicked
            @Override
            public void onItemClick(Drink item) {
                Toast.makeText(context, "Du har trykket på " + item.Name, Toast.LENGTH_LONG).show();
            }});

        this.recyclerView.setAdapter(mAdapter);
    }

    public void UseDataBaseDrinks(View view) {
        drinkViewModel = new ViewModelProvider(this).get(DrinkViewModel.class);
        drinkViewModel.getAllDrinks().observe(this, new Observer<List<Drink>>() {
            @Override
            public void onChanged(@Nullable final List<Drink> drinks) {
                ((MenuAdapter) mAdapter).setMyDrinks(drinks);
            }
        });
    }

    public void AddNewDrink(View view) {
        Intent intent = new Intent(context, AddDrink.class);
        startActivityForResult(intent, NEW_DRINK_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_DRINK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            Integer age = data.getIntExtra("age", 1);
            String type = data.getStringExtra("type");

            Drink drink = new Drink(name, age, type);
            drinkViewModel.insertDrink(drink);
        }
        else {
            Toast.makeText(context, "Not saved", Toast.LENGTH_LONG).show();
        }
    }

    private void CreateDrinkList() {
        //We are instantiating our list, so we don't try to assign to a null pointing variables.
        //Otherwise we're gonna have a baaad time with crashes. :(
        this.drink = new ArrayList<Drink>();

        //Normally I would advice STRONGLY against using while loops as there is great risk of loops running while causing memory leaks and after time a crash. :(
        //Try to use for loops instead.
        int i = 0;
        while(i < 5) {
            drink.add(new Drink("Drink", i, "Type"));
            i = i + 1;
        }

        //This is the correct loop!
        //Because we have all our logic in the required syntax, chances are we won't have an infinite loop.
        for(int dummy = 0; dummy < 5; dummy++) {
            drink.add(new Drink("Drink name here!", dummy, "Type"));
        }
    }
}