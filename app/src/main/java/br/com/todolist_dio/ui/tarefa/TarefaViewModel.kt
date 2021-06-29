package br.com.todolist_dio.ui.tarefa

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.todolist_dio.data.repository.TarefaRepository
import br.com.todolist_dio.domain.Tarefa
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author RubioAlves
 * Created 28/06/2021 at 13:26
 */
@HiltViewModel
class TarefaViewModel @Inject constructor(private val repository: TarefaRepository):ViewModel(){

    val listAll = MutableLiveData<MutableList<Tarefa>>()

    fun adicionarTarefas(tarefa: Tarefa){
        viewModelScope.launch {
            repository.insert(tarefa)
        }
    }
    fun atualizarTarefas(tarefa: Tarefa){
        viewModelScope.launch {
            repository.update(tarefa)
        }
    }
    fun listarTarefas(){
        viewModelScope.launch {
            listAll.value = repository.allTarefas()
        }
    }
    fun deleteAll(id:MutableList<Int>){
        viewModelScope.launch {
            repository.deleteAll(id)
        }
    }
}
