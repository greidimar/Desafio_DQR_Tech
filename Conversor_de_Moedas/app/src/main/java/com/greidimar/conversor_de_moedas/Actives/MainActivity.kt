/*
Desenvolvido por Greidimar Ferreira Lagares
Data 10/05/2020
*/

package com.greidimar.conversor_de_moedas.Actives

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.greidimar.conversor_de_moedas.HTTP.Http_Retorn_Quotes
import com.greidimar.conversor_de_moedas.HTTP.Http_lista_moedas
import com.greidimar.conversor_de_moedas.R
import com.greidimar.conversor_de_moedas.modelos.list_quotes

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.round

class MainActivity : AppCompatActivity() {


    var state: String? = null
    var origem_destino: String? = null

    var quote_origem_result: String? = null
    var quote_destino_result: String? = null

    //para a quotes
    private val quoteslist = mutableListOf<list_quotes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showProgress_1(false)

        edt_valor.setOnEditorActionListener { v, keyCode, event ->
            if (((event?.action ?: -1) == KeyEvent.ACTION_DOWN)
                || keyCode == EditorInfo.IME_ACTION_DONE) {

                if (txt_moeda_origem.text.toString().length > 0 && txt_moeda_destino.text.toString().length > 0 && edt_valor.text.toString().length > 0 )
                {
                    Download_qoutes()
                }

                //ocultando teclado
                val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)


                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }


        btn_moeda_origem.setOnClickListener {

                 origem_destino = "O"
                val intent = Intent(this@MainActivity, Lista_de_Moedas::class.java)
                intent.putExtra(Lista_de_Moedas.EXTRA_STATE, state)
                startActivityForResult(intent, REQUEST_STATE)

                if(savedInstanceState != null)
                {
                    state =savedInstanceState.getString(EXTRA_STATE)
                    txt_moeda_origem.text =  state
                }
            }


        btn_destino.setOnClickListener {
            origem_destino = "D"
            val intent = Intent(this@MainActivity, Lista_de_Moedas::class.java)
            intent.putExtra(Lista_de_Moedas.EXTRA_STATE, state)
            startActivityForResult(intent, REQUEST_STATE)

            if(savedInstanceState != null)
            {
                state =savedInstanceState.getString(EXTRA_STATE)
                txt_moeda_destino.text =  state
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode ==  Activity.RESULT_OK && requestCode == REQUEST_STATE)
        {
            state = data?.getStringExtra(Lista_de_Moedas.EXTRA_RESULT)

            if (origem_destino == "O")
            {
                txt_moeda_origem.text =  state
            }
            else
            {
                txt_moeda_destino.text =  state
            }

            if (txt_moeda_origem.text.toString().length > 0  && txt_moeda_destino.text.toString().length > 0  && edt_valor.text.toString().length > 0 )
            {
                Download_qoutes()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_STATE, state)
    }


    private fun Download_qoutes()
    {
        try {
            if (Http_lista_moedas.hasConnection(this@MainActivity)) {
                showProgress_1(true)
                CoroutineScope(Dispatchers.IO).launch {
                    val listagem_quotes = Http_Retorn_Quotes.load_quotes(
                        txt_moeda_origem.text.toString().substring(
                            0,
                            3
                        ), txt_moeda_destino.text.toString().substring(0, 3)
                    )

                    withContext(Dispatchers.Main) {
                        showProgress_1(false)
                        updateListaQuotes(listagem_quotes)
                    }
                }
            } else {
                showProgress_1(false)
            }

        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun showProgress_1(show: Boolean)
    {
        if (show)
        {
            progressCalculando.visibility = View.VISIBLE
        }
        else
        {
            progressCalculando.visibility = View.GONE
        }
    }

    private fun updateListaQuotes(result: List<list_quotes>?)
    {
        if (result != null)
        {
            quoteslist.clear()
            quoteslist.addAll(result)
            Calculando_Cotacao(quoteslist.get(0).cotacao.toString(), quoteslist.get(1).cotacao.toString())
        }
    }

    private fun Calculando_Cotacao(quote_origem: String, quote_destino: String)
    {
        //calculo da cotação, transformo em dolar a primeira moeda e depois o destino de dolar para a moeda de destino
        try {
            var v_quote_origem: Double = quote_origem.toDouble()
            var v_quote_destino: Double = quote_destino.toDouble()


            var v_valor: Double = edt_valor.text.toString().toDouble()
            var valor_convert_dolar_origem: Double = (v_valor / v_quote_origem)

            var valor_convert_destino: Double = (valor_convert_dolar_origem * v_quote_destino)

            valor_convert_destino = round2(valor_convert_destino)

            txt_valor.text = valor_convert_destino.toString()

        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Erro nos calculos", Toast.LENGTH_LONG).show()
        }
    }


    fun round2(x: Double) = round(x * 100) / 100

    companion object
    {
        private const val REQUEST_STATE = 1
        private const val EXTRA_STATE =  "estado"
    }

}
