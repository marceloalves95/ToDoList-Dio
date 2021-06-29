package br.com.todolist_dio.ui.tarefa

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.todolist_dio.R
import br.com.todolist_dio.databinding.DetailTarefaFragmentBinding
import br.com.todolist_dio.domain.Tarefa
import br.com.todolist_dio.ui.main.MainActivity
import br.com.todolist_dio.utils.validator.Validador
import br.com.todolist_dio.utils.validator.ValidadorPadrao
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author RubioAlves
 * Created 26/06/2021 at 14:03
 */
class DetailTarefaFragment : Fragment() {

    private var _binding: DetailTarefaFragmentBinding? = null
    private val binding get() = _binding!!
    private val validadores: MutableList<Validador> = ArrayList()
    private val nomeCores: MutableList<String> = mutableListOf()
    private val cores: MutableList<String> = mutableListOf()
    private val viewModel: TarefaViewModel by viewModel()
    private val args by navArgs<DetailTarefaFragmentArgs>()

    private lateinit var titulo: String
    private lateinit var descricao: String
    private lateinit var data: String
    private lateinit var horario: String
    private lateinit var nomeCor: String
    private lateinit var status: String
    private lateinit var cor: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as MainActivity).supportActionBar?.setTitle(R.string.title_detail_tarefas)

        _binding = DetailTarefaFragmentBinding.inflate(inflater, container, false)

        initViews()
        initCampos()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValidadores()
        initListas()
    }

    private fun initViews() {

        binding.run {

            with(abrirCalendario) {
                setOnClickListener {
                    with(data) {

                        val calendar = Calendar.getInstance()
                        val local = Locale("pt", "BR")

                        val dataListener =
                            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

                                calendar.set(Calendar.YEAR, year)
                                calendar.set(Calendar.MONTH, month)
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                                setText(SimpleDateFormat("dd/MM/yyyy", local).format(calendar.time))

                            }

                        DatePickerDialog(
                            requireContext(),
                            dataListener,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()

                    }
                }
            }
            with(abrirTimer) {
                setOnClickListener {
                    with(horario) {

                        val local = Locale("pt", "BR")
                        val calendar = Calendar.getInstance()

                        val timePicker =
                            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->

                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                calendar.set(Calendar.MINUTE, minute)

                                setText(SimpleDateFormat("HH:mm", local).format(calendar.time))

                            }

                        TimePickerDialog(
                            requireContext(),
                            timePicker,
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    }
                }
            }


        }

    }
    private fun initListas(){

        binding.run {
            with(status) {

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    resources.getStringArray(R.array.lista_status)
                )

                setAdapter(adapter)
                //setText(resources.getText(R.string.fazer))

            }
            with(cores) {
                val adapter = CorAdapter(
                    requireContext(),
                    listarNomeCores(),
                    listarCores(),
                    android.R.layout.simple_list_item_1
                )
                setAdapter(adapter)
            }
        }

    }

    private val listarNomeCores: () -> MutableList<String> = {

        Cor.values().forEach { nomesCores -> nomeCores.add(nomesCores.nomeCor) }

        nomeCores

    }

    private fun listarCores(): MutableList<String> {

        Cor.values().forEach { cor -> cores.add(cor.cor) }

        return cores

    }

    private fun validarTodosCampos(): Boolean {

        var estaValido = true
        validadores.forEach { validador -> if (!validador.estaValido()) estaValido = false }
        return estaValido

    }

    private fun salvarTarefa() {

        camposTarefa()

        Cor.values().forEach { cores -> if (cores.nomeCor == nomeCor) cor = cores.cor }

        val status = resources.getText(R.string.fazer).toString()


        val estaValido = validarTodosCampos()

        if (estaValido) {

            val tarefa = Tarefa(0, titulo, descricao, horario, data, cor, nomeCor, status, false)
            viewModel.adicionarTarefas(tarefa)
            Toast.makeText(requireContext(), "Tarefa salva com sucesso", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_navigation_detail_tarefa_fragment_to_navigation_tarefa)

        }


    }

    private fun alterarTarefa() {

        camposTarefa()

        Cor.values().forEach { cores -> if (cores.nomeCor == nomeCor) cor = cores.cor }

        val estaValido = validarTodosCampos()

        if (estaValido) {

            val tarefa =
                Tarefa(args.id, titulo, descricao, horario, data, cor, nomeCor, status, false)
            viewModel.atualizarTarefas(tarefa)
            Toast.makeText(requireContext(), "Tarefa alterada com sucesso", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_navigation_detail_tarefa_fragment_to_navigation_tarefa)

        }


    }

    private fun camposTarefa() {

        binding.let { tarefas ->

            titulo = tarefas.titulo.text.toString()
            descricao = tarefas.descricao.text.toString().trim()
            data = tarefas.data.text.toString().trim()
            horario = tarefas.horario.text.toString().trim()
            nomeCor = tarefas.cores.text.toString().trim()
            status = tarefas.status.text.toString().trim()

        }

    }

    private fun initCampos() {

        if (args.tarefas == null) {

            (activity as MainActivity).supportActionBar?.setTitle(R.string.criar_tarefa)

            with(binding.status){
                setText(resources.getText(R.string.fazer).toString())
            }
            with(binding.campoStatus){
                isEnabled = false
            }

            with(binding.tarefa) {
                iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
                text = resources.getText(R.string.criar_tarefa)
                setIconResource(R.drawable.ic_add_task)
                setOnClickListener {
                    salvarTarefa()
                }

            }
        } else {

            (activity as MainActivity).supportActionBar?.setTitle(R.string.alterar_tarefa)

            initListas()

            args.tarefas.let { args ->

                binding.run {

                    titulo.setText(args?.titulo)
                    descricao.setText(args?.descricao)
                    data.setText(args?.data)
                    horario.setText(args?.horario)
                    cores.setText(args?.nomeCor)
                    status.setText(args?.status)

                    with(tarefa) {
                        iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
                        text = resources.getText(R.string.alterar_tarefa)
                        setIconResource(R.drawable.ic_task_alter)
                        setOnClickListener {
                            alterarTarefa()
                        }
                    }
                }


            }
        }

    }

    private fun initValidadores() {
        with(binding) {
            validadorPadrao(campoTitulo)
            validadorPadrao(campoDescricao)
            validadorPadrao(campoData)
            validadorPadrao(campoHorario)
            validadorMaterialComplete(campoCores)
            validadorMaterialComplete(campoStatus)
        }
    }

    private fun validadorPadrao(textInputLayout: TextInputLayout) {
        val campo = textInputLayout.editText

        val validado = ValidadorPadrao(textInputLayout, campo as TextInputEditText)
        validadores.add(validado)

        campo.setOnFocusChangeListener { _, hasFocus ->

            if (!hasFocus) {
                if (!validado.estaValido()) {
                    return@setOnFocusChangeListener
                } else {
                    validado.removeError(textInputLayout)
                }

            }

        }
    }

    private fun validadorMaterialComplete(textInputLayout: TextInputLayout) {

        val campo = textInputLayout.editText

        val validado = ValidadorPadrao(textInputLayout, campo as MaterialAutoCompleteTextView)
        validadores.add(validado)
        campo.setOnFocusChangeListener { _, hasFocus ->

            if (!hasFocus) {

                if (!validado.estaValido()) {
                    return@setOnFocusChangeListener
                } else {
                    validado.removeError(textInputLayout)
                }

            }

        }
    }


}