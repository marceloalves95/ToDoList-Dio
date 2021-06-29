package br.com.todolist_dio.di

import androidx.room.Room
import br.com.todolist_dio.data.db.AppDatabase
import br.com.todolist_dio.data.repository.TarefaRepository
import br.com.todolist_dio.ui.tarefa.TarefaViewModel
import br.com.todolist_dio.utils.others.Constants.BD_NAME
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author RubioAlves
 * Created 28/06/2021 at 12:15
 */
val appModules = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            BD_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }
    single{
        get<AppDatabase>().tarefaDao()
    }
    single {
        TarefaRepository(get())
    }
    viewModel {
        TarefaViewModel(get())
    }

}