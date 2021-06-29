package br.com.todolist_dio.ui.tarefa

import android.graphics.Color
import android.os.Build
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.core.util.isNotEmpty
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import br.com.todolist_dio.databinding.TarefasAdapterBinding
import br.com.todolist_dio.domain.Tarefa
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @author RubioAlves
 * Created 27/06/2021 at 07:55
 */
class TarefaAdapter(private val fragment: TarefaFragment, val lista: MutableList<Tarefa>) :
    RecyclerView.Adapter<TarefaAdapter.TarefaAdapterViewHolder>(), Filterable {

    val selectedItems = SparseBooleanArray()
    private var currentSelectedPos = -1
    private var listaAll: MutableList<Tarefa> = ArrayList()

    init {
        listaAll.addAll(lista)
    }


    inner class TarefaAdapterViewHolder(private val itemBinding: TarefasAdapterBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(tarefa: Tarefa) {

            itemBinding.run {

                cardView.apply {
                    setCardBackgroundColor(Color.parseColor(tarefa.cor))
                    isChecked = tarefa.selected
                }

                with(cardView) {
                    setOnClickListener {

                        if (selectedItems.isNotEmpty()) onItemClick?.invoke(adapterPosition)
                        fragment.actionMode?.finish()
                        listaArgumentos(tarefa)

                    }
                    setOnLongClickListener {
                        onItemLongClick?.invoke(adapterPosition)
                        return@setOnLongClickListener true
                    }
                }

                val dataAtual = initDatas(tarefa.data)

                listarCores().forEach { cores ->

                    if (tarefa.nomeCor == cores.nomeCor) {

                        tarefaTitulo.text = tarefa.titulo
                        tarefaTitulo.setTextColor(Color.parseColor(cores.textColor))
                        descricao.text = tarefa.descricao
                        descricao.setTextColor(Color.parseColor(cores.textColor))
                        horario.text = tarefa.horario
                        horario.setTextColor(Color.parseColor(cores.textColor))
                        data.text = dataAtual
                        data.setTextColor(Color.parseColor(cores.textColor))

                        with(cardView) {
                            strokeColor =
                                if (isChecked) Color.parseColor(cores.corPrimaryDark) else Color.parseColor(
                                    tarefa.cor
                                )
                        }

                    }

                }

                if (currentSelectedPos == adapterPosition) currentSelectedPos = -1
            }


        }

    }

    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int
    ): TarefaAdapterViewHolder {

        val itemBinding =
            TarefasAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TarefaAdapterViewHolder(itemBinding)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TarefaAdapterViewHolder, position: Int) {

        holder.bind(lista[position])
    }

    override fun getItemCount(): Int = lista.size

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initDatas(data: String): String {

        val local = Locale("pt", "BR")
        val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy", local)
        val localDate = LocalDate.parse(data, dtf)

        val dia = localDate.dayOfMonth.toString()
        val mes = localDate.month

        val mesReduzido = DateTimeFormatter.ofPattern("MMM", local)
        val mesCompleto = mesReduzido.format(mes)

        return "$dia de $mesCompleto"


    }

    private val onItemClick: ((Int) -> Unit)? = null
    var onItemLongClick: ((Int) -> Unit)? = null

    fun toggleSelection(position: Int) {

        currentSelectedPos = position
        if (selectedItems[position, false]) {
            selectedItems.delete(position)

            lista[position].selected = false

        } else {
            selectedItems.put(position, true)
            lista[position].selected = true
        }

        notifyItemChanged(position)


    }

    private val listaArgumentos: (Tarefa) -> Unit = { tarefa ->

        with(tarefa) {
            val tarefas = TarefaArgs(titulo, descricao, data, horario, nomeCor, status)
            val id = tarefa.id
            val action =
                TarefaFragmentDirections.actionNavigationTarefaToNavigationDetailTarefaFragment(
                    id,
                    tarefas
                )
            fragment.findNavController().navigate(action)
        }

    }


    private fun listarCores(): MutableList<Cor> {

        val cores: MutableList<Cor> = mutableListOf()

        Cor.values().forEach { cor ->

            cores.add(cor)

        }

        return cores

    }

    fun deletarTarefa() {

        lista.removeAll(lista.filter { it.selected })

        notifyDataSetChanged()
        currentSelectedPos = -1


    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filteredList: MutableList<Tarefa> = ArrayList()

                val locale = Locale("pt", "BR")

                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filteredList.addAll(listaAll)
                } else {

                    listaAll.forEach { tarefa ->

                        with(tarefa) {
                            if (titulo.lowercase(locale).contains(constraint.toString().lowercase(locale),true)
                                || descricao.lowercase(locale)
                                    .contains(constraint.toString().lowercase(locale),true)
                                || horario.lowercase(locale)
                                    .contains(constraint.toString().lowercase(locale),true)
                                || data.lowercase(locale)
                                    .contains(constraint.toString().lowercase(locale),true)
                            ) {
                                filteredList.add(tarefa)
                            }
                        }

                    }

                }
                val results = FilterResults()
                results.values = filteredList

                return results


            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                lista.clear()
                lista.addAll(results?.values as MutableList<Tarefa>)
                notifyDataSetChanged()

            }
        }

    }


}