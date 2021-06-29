package br.com.todolist_dio.data.dao

import androidx.room.*
import br.com.todolist_dio.domain.Tarefa

/**
 * @author RubioAlves
 * Created 28/06/2021 at 13:25
 */
@Dao
interface TarefaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tarefa: Tarefa)
    @Update
    suspend fun update(tarefa: Tarefa)
    @Query("SELECT * FROM tarefa")
    suspend fun allTarefas():MutableList<Tarefa>
    @Query("DELETE FROM tarefa WHERE id IN (:id)")
    suspend fun deleteAll(id:MutableList<Int>)

}
