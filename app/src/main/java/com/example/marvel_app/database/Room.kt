package com.example.marvel_app.database

import android.content.Context
import androidx.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Dao
interface ComicDao {

    @Transaction
    @Query("SELECT * FROM comics")
    suspend fun getAll(): List<DatabaseComicWithAuthors>

    @Transaction
    @Query("SELECT * FROM comics where id = :id")
    suspend fun getComic(id: Int): DatabaseComicWithAuthors?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertComic(comic: DatabaseComic)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAuthors(authors: List<Author>)

    @Delete
    suspend fun deleteComic(comic: DatabaseComic)

    @Delete
    suspend fun deleteAuthors(authors: List<Author>)
}

@Database(entities = [DatabaseComic::class, Author::class], version = 1)
abstract class ComicsDatabase : RoomDatabase() {
    abstract val comicDao: ComicDao
}

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideComicDao(comicsDatabase: ComicsDatabase): ComicDao {
        return comicsDatabase.comicDao
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): ComicsDatabase {
        return Room.databaseBuilder(
            appContext,
            ComicsDatabase::class.java,
            "comics"
        ).build()
    }
}

