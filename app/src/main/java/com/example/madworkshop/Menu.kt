package com.example.madworkshop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.madworkshop.Model.Drink
import com.example.madworkshop.ViewModel.DrinkViewModel

class Menu : AppCompatActivity() {
    //This is global variables of this class. You can access these anywhere in this class.
    //You can add accessors like private or public to enable the variables to be used outside of this class.
    //Hint: If you have an input variable in a method with the same name as a global variable, you can use the 'this' keyword to use the global variable
    private var recyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var drink: ArrayList<Drink>? = null
    private val drinksDb: List<Drink>? = null
    private var drinkViewModel: DrinkViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        context = applicationContext

        //We like to break things down to methods. It's much cleaner!
        CreateDrinkList()
        CreateRecyclerView()
    }

    private fun CreateRecyclerView() {
        //We assign to the recycler view variable we have.
        recyclerView = findViewById<View>(R.id.rv_menu) as RecyclerView
        //This is a performance thing. If the recyclerview is not gonna change, we can save some computation time.
        recyclerView!!.setHasFixedSize(true)

        //This is line splitting each item. Here view just have a straigh vertical line.
        val itemDecoration: ItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView!!.addItemDecoration(itemDecoration)

        //We are using a linear layout manager, because we want each of these items to be shown in a list vertically. Se also that we assign the order of the manager to be vertical.
        //Hint: Try to change it to horizontal just for fun! Remember to scroll. Now you can also make a horizontal scroll list.
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager

        //The recyclerview needs an adapter, otherwise it's just an empty list.
        //We have created a class just for this specific purpose.
        mAdapter = drink!!.let {
            MenuAdapter(it) { item ->
            //We have implemented a click listener on our items for our recyclerView. Here we are using an anonymous function to show that the item was clicked
                if(item is Drink) {
                    Toast.makeText(context, "Du har trykket på " + item.name, Toast.LENGTH_LONG).show()
                }
        }
        }
        recyclerView!!.adapter = mAdapter
    }

    fun UseDataBaseDrinks(view: View?) {
        drinkViewModel = ViewModelProvider(this).get(DrinkViewModel::class.java)
        drinkViewModel!!.allDrinks.observe(this) { drinks -> (mAdapter as MenuAdapter?)!!.setMyDrinks(drinks) }
    }

    fun AddNewDrink(view: View?) {
        val intent = Intent(context, AddDrink::class.java)
        startActivityForResult(intent, NEW_DRINK_ACTIVITY_REQUEST_CODE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_DRINK_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val name = data!!.getStringExtra("name")
            val age = data.getIntExtra("age", 1)
            val type = data.getStringExtra("type")
            val drink = Drink(name!!, age, type!!)
            drinkViewModel!!.insertDrink(drink)
        } else {
            Toast.makeText(context, "Not saved", Toast.LENGTH_LONG).show()
        }
    }

    private fun CreateDrinkList() {
        //We are instantiating our list, so we don't try to assign to a null pointing variables.
        //Otherwise we're gonna have a baaad time with crashes. :(
        drink = ArrayList()

        //Normally I would advice STRONGLY against using while loops as there is great risk of loops running while causing memory leaks and after time a crash. :(
        //Try to use for loops instead.
        var i = 0
        while (i < 5) {
            drink!!.add(Drink("Drink", i, "Type"))
            i = i + 1
        }

        //This is the correct loop!
        //Because we have all our logic in the required syntax, chances are we won't have an infinite loop.
        for (dummy in 0..4) {
            drink!!.add(Drink("Drink name here!", dummy, "Type"))
        }
    }

    companion object {
        private var context: Context? = null
        private const val NEW_DRINK_ACTIVITY_REQUEST_CODE = 1
    }
}