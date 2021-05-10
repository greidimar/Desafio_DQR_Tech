
/*
Desenvolvido por Greidimar Ferreira Lagares
Data 10/05/2020
*/

package com.greidimar.conversor_de_moedas.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.greidimar.conversor_de_moedas.HTTP.Http_lista_moedas
import com.greidimar.conversor_de_moedas.R
import com.greidimar.conversor_de_moedas.modelos.list_moedas
import kotlinx.android.synthetic.main.fragment_moedas.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class Moedas_Fragment : Fragment(), CoroutineScope {

    private val moedaslist = mutableListOf<list_moedas>()
    private var list_adapter: ArrayAdapter<list_moedas>? = null

    private lateinit var job: Job
    private var downloadJob: Job? = null

    override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        retainInstance = true
        job =Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_moedas, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        list_adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, moedaslist)
        list_view_moedas.emptyView = txt_titulo_lista
        list_view_moedas.adapter = list_adapter
        list_view_moedas.choiceMode = AbsListView.CHOICE_MODE_SINGLE

        try {


        if (moedaslist.isNotEmpty()) {
            showProgress_1(false)
        } else {
            if (downloadJob == null) {
                if (Http_lista_moedas.hasConnection(requireContext())) {
                    Dwnload_Moedas()
                } else {
                    showProgress_1(false)
                }
            } else if (downloadJob?.isActive == true) {
                showProgress_1(true)
            }
        }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Erro nos calculos", Toast.LENGTH_LONG).show()
        }
    }



    private fun Dwnload_Moedas() {
        downloadJob = launch {
            showProgress_1(show = true)
            val moedastask = withContext(Dispatchers.IO)
            {
                Http_lista_moedas.load_moedas()
            }
            updateListaMoedas(moedastask)
            showProgress_1(false)
        }
    }


    private fun showProgress_1(show: Boolean)
    {
        if (show)
        {
            txt_titulo_lista.setText("Escolha uma moeda")
        }

        if (show) {
            txt_titulo_lista.visibility = View.VISIBLE
            progressBar2.visibility = View.VISIBLE
        }
        else
        {
            txt_titulo_lista.visibility = View.GONE
            progressBar2.visibility = View.GONE
        }
    }

    private fun updateListaMoedas(result: List<list_moedas>?)
    {
        if (result != null)
        {
            moedaslist.clear()
            moedaslist.addAll(result)
        }
        else
        {
            txt_titulo_lista.setText("Erro")
        }

        list_adapter?.notifyDataSetChanged()
        downloadJob = null
    }
}