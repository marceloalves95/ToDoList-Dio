package br.com.todolist_dio.utils.validator

import android.widget.EditText
import br.com.todolist_dio.utils.others.Constants.CAMPO_OBRIGATORIO
import com.google.android.material.textfield.TextInputLayout

/**
 * @author RubioAlves
 * Created 28/06/2021 at 16:45
 */
class ValidadorPadrao(private val textInputCampo:TextInputLayout, private var campo:EditText):Validador{

    init {
        campo = textInputCampo.editText!!
    }

    override fun removeError(textInputLayout: TextInputLayout) {
        textInputLayout.error = null
        textInputLayout.isErrorEnabled = false
    }

    override fun validaCampoObrigatorio(): Boolean {

        val texto = campo.text.toString()

        if (texto.isEmpty()){
            textInputCampo.error = CAMPO_OBRIGATORIO
            return false
        }

        return true

    }

    override fun estaValido(): Boolean = validaCampoObrigatorio()
}