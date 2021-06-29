package br.com.todolist_dio.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.todolist_dio.data.dao.TarefaDao
import br.com.todolist_dio.domain.Tarefa

/**
 * @author RubioAlves
 * Created 28/06/2021 at 13:22
 */
@Database(entities = [Tarefa::class], version = 1)
abstract class AppDatabase:RoomDatabase() {

    abstract fun tarefaDao():TarefaDao

}