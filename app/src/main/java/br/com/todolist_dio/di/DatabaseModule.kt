package br.com.todolist_dio.di

import android.content.Context
import androidx.room.Room
import br.com.todolist_dio.data.dao.TarefaDao
import br.com.todolist_dio.data.db.AppDatabase
import br.com.todolist_dio.utils.others.Constants.BD_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author RubioAlves
 * Created 29/06/2021 at 13:21
 */
@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, BD_NAME).fallbackToDestructiveMigration().build()


    @Provides
    fun provideTarefaDao(appDatabase: AppDatabase): TarefaDao{
        return appDatabase.tarefaDao()
    }


}