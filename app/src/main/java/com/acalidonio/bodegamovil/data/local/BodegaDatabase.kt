package com.acalidonio.bodegamovil.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.acalidonio.bodegamovil.data.local.dao.ProductDao
import com.acalidonio.bodegamovil.data.local.entity.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class BodegaDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var Instance: BodegaDatabase? = null

        fun getDatabase(context: Context): BodegaDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, BodegaDatabase::class.java, "bodega_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
