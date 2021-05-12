
/*
Desenvolvido por Greidimar Ferreira Lagares
Data 10/05/2020
*/

package com.greidimar.conversor_de_moedas.Fragments

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.forEach
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import com.greidimar.conversor_de_moedas.HTTP.Http_lista_moedas
import com.greidimar.conversor_de_moedas.R
import com.greidimar.conversor_de_moedas.modelos.list_moedas
import kotlinx.android.synthetic.main.fragment_moedas.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import android.content.Context.INPUT_METHOD_SERVICE
import androidx.core.content.ContextCompat.getSystemService

import android.content.Context
import android.graphics.Color
import android.widget.AbsListView
import android.widget.AdapterView
import androidx.core.view.get
import java.util.*


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


        val arrayOrdenacao= listOf<String>("Sigla Crescente","Sigla Decrescente","País Crescente","País Decrescente")
        val arrayAdapter_Ordem = ArrayAdapter<String>(requireContext(), R.layout.support_simple_spinner_dropdown_item, arrayOrdenacao)
        sp_ordenar.adapter = arrayAdapter_Ordem

        /////

        sp_ordenar?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val type = parent?.getItemAtPosition(position).toString()
                Ordenacao(type)
            }
        }
        ////

        list_adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, moedaslist)
        list_view_moedas.emptyView = txt_titulo_lista
        list_view_moedas.adapter = list_adapter
        list_view_moedas.choiceMode = AbsListView.CHOICE_MODE_SINGLE


        btn_pesquisar.setOnClickListener {
            //seleciona a moeda deixa no topo da lista
            Pesquisar()
        }

        edt_pesquisar.setOnEditorActionListener { v, keyCode, event ->
            if (((event?.action ?: -1) == KeyEvent.ACTION_DOWN)
                || keyCode == EditorInfo.IME_ACTION_DONE) {

                //seleciona a moeda deixa no topo da lista
                Pesquisar()

                //ocultando teclado
                val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }


        //download lista de moedas
        Download_Moedas("Sigla Crescente")
    }



    private fun Download_Moedas(ordem:String) {
        try {
                showProgress_1(false)
                if (downloadJob == null) {
                    if (Http_lista_moedas.hasConnection(requireContext())) {
                       //
                        downloadJob = launch {
                            showProgress_1(show = true)
                            val moedastask = withContext(Dispatchers.IO)
                            {
                                Http_lista_moedas.load_moedas()
                            }
                            updateListaMoedas(moedastask, ordem)
                            showProgress_1(false)
                        }
                       //
                    } else {
                        showProgress_1(false)
                    }
                } else if (downloadJob?.isActive == true) {
                    showProgress_1(true)
                }
        } catch (e: Exception) {
            showProgress_1(false)
            Toast.makeText(requireContext(), "Erro nos calculos", Toast.LENGTH_LONG).show()
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

    private fun updateListaMoedas(result: List<list_moedas>?, ordem:String)
    {
        if (result != null)
        {
            moedaslist.clear()
            moedaslist.addAll(result)
            if(ordem == "Sigla Crescente")
            {
                moedaslist.sortWith(compareBy({it.sigla}, {it.sigla}))
            }
            //
            if(ordem == "Sigla Decrescente")
            {
                moedaslist.sortWith(compareBy({it.sigla}, {it.sigla}))
                moedaslist.reverse()
            }
            //
            if(ordem == "País Crescente")
            {
                moedaslist.sortWith(compareBy({it.pais}, {it.pais}))
            }
            //
            if(ordem == "País Decrescente")
            {
                moedaslist.sortWith(compareBy({it.pais}, {it.pais}))
                moedaslist.reverse()
            }
        }
        else
        {
            txt_titulo_lista.setText("Erro")
        }

        list_adapter?.notifyDataSetChanged()


        downloadJob = null
    }


    //procura a moeda
    private fun Pesquisar() {
        try {
            for ((index, value) in moedaslist.withIndex()) {
                var p_ls: String = moedaslist.get(index).sigla.toUpperCase()
                var p_lp: String = moedaslist.get(index).pais.toUpperCase()
                var p_e: String = edt_pesquisar.text.toString().toUpperCase()

                //se tem 3 caracteres procuro na sigla se tem mias de 3 caractese procuro no pais
                if (p_e.length <= 3) {
                    if (p_ls == p_e) {
                        list_view_moedas.setSelection(index)
                    }
                } else if (p_e.length > 3) {
                    if (p_lp.indexOf(p_e) >= 0) {
                        list_view_moedas.setSelection(index)
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Erro ao pesquisar", Toast.LENGTH_LONG).show()
        }
    }

    //ordenação
    private fun Ordenacao(ordem:String)
    {
        Download_Moedas(ordem)
    }
}