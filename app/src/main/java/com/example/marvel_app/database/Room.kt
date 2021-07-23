package com.example.marvel_app.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Dao
interface ComicDao {
    @Query("SELECT * FROM DatabaseComic")
    fun getAll(): LiveData<List<DatabaseComic>>

    @Insert
    fun insertAll(vararg comics: DatabaseComic)

    @Delete
    fun delete(comic: DatabaseComic)
}

@Database(entities = [DatabaseComic::class], version = 1)
abstract class ComicsDatabase : RoomDatabase() {
    abstract val comicDao: ComicDao
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideDatabase(context: Context): ComicsDatabase {
        var database: ComicsDatabase
        synchronized(ComicsDatabase::class.java) {
            database = Room.databaseBuilder(
                context.applicationContext,
                ComicsDatabase::class.java,
                "comics"
            ).build()
        }
        return database
    }
}

