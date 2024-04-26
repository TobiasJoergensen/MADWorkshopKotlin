package com.example.madworkshop.Model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors
import kotlin.concurrent.Volatile

@Database(entities = [Drink::class], version = 1, exportSchema = false)
abstract class DrinkRoomDatabase : RoomDatabase() {
    //This is global variables of this class. You can access these anywhere in this class.
    //You can add accessors like private or public to enable the variables to be used outside of this class.
    //Hint: If you have an input variable in a method with the same name as a global variable, you can use the 'this' keyword to use the global variable
    abstract fun drinkDao(): DrinkDao

    companion object {
        private const val DRINK_DB_NAME = "drink_db"

        @Volatile
        private var INSTANCE: DrinkRoomDatabase? = null
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
        fun getDatabase(context: Context): DrinkRoomDatabase? {
            //If the instance have not been instantiated yet, then we will move into this clause.
            if (INSTANCE == null) {
                //Because we can start this method multiple times, we use the keyword 'synchronized' to make sure we don't crate two instances of a database connection.
                synchronized(DrinkRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        //Here we assign our instance of our dataBase connection.
                        try {
                            INSTANCE = Room.databaseBuilder(context.applicationContext,
                                DrinkRoomDatabase::class.java, DRINK_DB_NAME).addCallback(sRoomDatabaseCallback).build()
                        }
                        catch(e: Exception) {
                            println(e);
                            return null;
                        }
                    }
                    return INSTANCE
                }
            }
            return INSTANCE
        }

        //This method happens when we create our database connection. See line 30.
        private val sRoomDatabaseCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                //Careful! Since this connection is established each time we start the app, you might now want to clear it out all the time.
                //If you want it to persist, just remove or comment out the lambda below.
                databaseWriteExecutor.execute {

                    //Se the the connection to be equal to the instance.
                    val dao = INSTANCE!!.drinkDao()
                    //Clear the database.
                    dao.deleteAll()

                    //Add mock data.
                    val drink = Drink("Pina Colada", 20, "Drink")
                    dao.insertDrink(drink)
                    val beer = Drink("Carlsberg", 5, "Beer")
                    dao.insertDrink(beer)
                    val wine = Drink("Wine", 30, "Wine")
                    dao.insertDrink(wine)
                }
            }
        }
    }
}
