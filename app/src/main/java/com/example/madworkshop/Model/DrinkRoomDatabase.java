package com.example.madworkshop.Model;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.concurrent.ExecutorService; import java.util.concurrent.Executors;
import io.reactivex.annotations.NonNull;

@Database(entities = {Drink.class}, version = 1, exportSchema = false)
public abstract class DrinkRoomDatabase extends RoomDatabase {

    //This is global variables of this class. You can access these anywhere in this class.
    //You can add accessors like private or public to enable the variables to be used outside of this class.
    //Hint: If you have an input variable in a method with the same name as a global variable, you can use the 'this' keyword to use the global variable
    public abstract DrinkDao drinkDao();
    private static final String DRINK_DB_NAME = "drink_db";
    private static volatile DrinkRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static DrinkRoomDatabase getDatabase(final Context context) {
        //If the instance have not been instantiated yet, then we will move into this clause.
        if (INSTANCE == null) {
            //Because we can start this method multiple times, we use the keyword 'synchronized' to make sure we don't crate two instances of a database connection.
            synchronized (DrinkRoomDatabase.class) {
                if (INSTANCE == null) {
                    //Here we assign our instance of our dataBase connection.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DrinkRoomDatabase.class, DRINK_DB_NAME).addCallback(sRoomDatabaseCallback).build();
                }
                return INSTANCE;
            }
        }
        return INSTANCE;
    }

    //This method happens when we create our database connection. See line 30.
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //Careful! Since this connection is established each time we start the app, you might now want to clear it out all the time.
            //If you want it to persist, just remove or comment out the lambda below.
            databaseWriteExecutor.execute(() -> {
                //Se the the connection to be equal to the instance.
                DrinkDao dao = INSTANCE.drinkDao();
                //Clear the database.
                dao.deleteAll();

                //Add mock data.
                Drink drink = new Drink("Pina Colada", 20, "Drink");
                dao.insertDrink(drink);
                Drink beer = new Drink("Carlsberg", 5, "Beer");
                dao.insertDrink(beer);
                Drink wine = new Drink("Wine", 30, "Wine");
                dao.insertDrink(wine);
            });
        }

    };
}

