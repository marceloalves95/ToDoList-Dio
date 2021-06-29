package br.com.todolist_dio.ui.tarefa

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author RubioAlves
 * Created 27/06/2021 at 12:34
 */
@Parcelize
data class TarefaArgs(val titulo:String, val descricao:String,val data:String, val horario:String, val nomeCor:String, val status:String):Parcelable
