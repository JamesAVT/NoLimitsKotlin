// Ruta: app/src/main/java/com/example/nolimits/data/local/model/database/AppDatabase.kt
package com.example.nolimits.data.local.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nolimits.data.local.dao.AppUserDao
import com.example.nolimits.data.local.dao.UserDao
import com.example.nolimits.data.local.model.AppUser
import com.example.nolimits.data.local.model.User

@Database(
    entities = [
        AppUser::class,
        User::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appUserDao(): AppUserDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Devuelve una instancia única de la base de datos.
         * Se llama getDatabase para que coincida con:
         *   AppDatabase.getDatabase(context)
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nolimits.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Si quieres, puedes dejar este alias por si en otra parte usas getInstance
        fun getInstance(context: Context): AppDatabase = getDatabase(context)
    }
}