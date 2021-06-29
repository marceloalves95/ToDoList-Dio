package br.com.todolist_dio.utils.validator

import com.google.android.material.textfield.TextInputLayout

/**
 * @author RubioAlves
 * Created 28/06/2021 at 16:45
 */
interface Validador {

    fun estaValido():Boolean
    fun validaCampoObrigatorio():Boolean
    fun removeError(textInputLayout: TextInputLayout)

}