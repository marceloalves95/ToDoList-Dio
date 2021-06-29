package br.com.todolist_dio.ui.tarefa

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.todolist_dio.R
import br.com.todolist_dio.databinding.TarefaFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author RubioAlves
 * Created 26/06/2021 at 13:51
 */
@AndroidEntryPoint
class TarefaFragment : Fragment() {

    private var _binding: TarefaFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var tarefaAdapter:TarefaAdapter
    var actionMode: ActionMode? = null
    private val viewModel:TarefaViewModel by viewModels()
    private val listarId = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TarefaFragmentBinding.inflate(inflater, container, false)

        initViews()
        setHasOptionsMenu(true)
        return binding.root

    }

    private fun initViews(){

        binding.run {

            viewModel.listarTarefas()
            viewModel.listAll.observe(viewLifecycleOwner,{ listaTarefas->

                tarefaAdapter = TarefaAdapter(this@TarefaFragment, listaTarefas)

                with(tarefaAdapter){
                    onItemLongClick={
                        enableActionMode(it)
                    }
                }
                with(recyclerView){

                    adapter = tarefaAdapter
                    layoutManager = GridLayoutManager(context, 2)
                    setHasFixedSize(true)

                }

            })
            with(fab){
                setOnClickListener {
                    findNavController().navigate(R.id.navigation_detail_tarefa_fragment)
                }
            }

        }

    }
    private fun enableActionMode(position: Int) {

        if (actionMode == null) actionMode = activity?.startActionMode(object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.menuInflater?.inflate(R.menu.menu_tarefa, menu)
                mode?.title = resources.getString(R.string.deletar_tarefa)

                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                if (item?.itemId == R.id.action_delete){
                    deletartarefa()
                    mode?.finish()
                    return true

                }
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                tarefaAdapter.selectedItems.clear()
                tarefaAdapter.lista
                    .filter { it.selected }
                    .forEach { it.selected = false }

                tarefaAdapter.notifyDataSetChanged()
                actionMode = null
            }

        })

        tarefaAdapter.toggleSelection(position)
        val size = tarefaAdapter.selectedItems.size()
        if (size == 0){
            actionMode?.finish()
        }else{
            actionMode?.subtitle = "$size tarefas(s) selecionada(s)"
            actionMode?.invalidate()

        }

    }

    fun deletartarefa(){

        val list = tarefaAdapter.lista.filter { it.selected }

        list.forEach { tarefa -> listarId.add(tarefa.id) }

        viewModel.deleteAll(listarId)
        tarefaAdapter.deletarTarefa()
        Toast.makeText(requireContext(), "Tarefa(s) deletada(s) com sucesso", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tarefaAdapter.filter.filter(newText)
                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}