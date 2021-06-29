package br.com.todolist_dio.data.repository

import br.com.todolist_dio.data.dao.TarefaDao
import br.com.todolist_dio.domain.Tarefa

/**
 * @author RubioAlves
 * Created 28/06/2021 at 13:24
 */
class TarefaRepository(private val tarefaDao: TarefaDao) {

    suspend fun insert(tarefa: Tarefa) = tarefaDao.insert(tarefa)
    suspend fun update(tarefa: Tarefa) = tarefaDao.update(tarefa)
    suspend fun allTarefas():MutableList<Tarefa> = tarefaDao.allTarefas()
    suspend fun deleteAll(id:MutableList<Int>) = tarefaDao.deleteAll(id)
}