package br.com.todolist_dio.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.todolist_dio.data.dao.TarefaDao
import br.com.todolist_dio.domain.Tarefa
import br.com.todolist_dio.utils.others.Constants

/**
 * @author RubioAlves
 * Created 28/06/2021 at 13:22
 */
@Database(entities = [Tarefa::class], version = 1, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {

    abstract fun tarefaDao():TarefaDao

}