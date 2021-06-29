package br.com.todolist_dio.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author RubioAlves
 * Created 27/06/2021 at 07:57
 */
@Entity(tableName = "tarefa")
data class Tarefa(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val titulo: String,
    val descricao: String,
    val horario: String,
    val data: String,
    val cor: String,
    val nomeCor: String,
    val status:String,
    var selected: Boolean
)
