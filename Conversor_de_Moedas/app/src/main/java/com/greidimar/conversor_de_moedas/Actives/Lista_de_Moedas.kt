package com.greidimar.conversor_de_moedas.Actives

/*
Desenvolvido por Greidimar Ferreira Lagares
Data 10/05/2020
*/


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.greidimar.conversor_de_moedas.R

import com.greidimar.conversor_de_moedas.modelos.list_moedas
import kotlinx.android.synthetic.main.fragment_moedas.*

class Lista_de_Moedas : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_de_moedas)

        try {

            list_view_moedas.setOnItemClickListener { list, _, position, _ ->

                val result = list.getItemAtPosition(position) as list_moedas

                val itt = Intent()
                itt.putExtra(EXTRA_RESULT, result.sigla + " - " + result.pais)
                setResult(Activity.RESULT_OK, itt)
                finish()
            }
        }
        catch (e: Exception) {
                Toast.makeText(this@Lista_de_Moedas, e.toString(), Toast.LENGTH_LONG).show()
            }
    }


    companion object
    {
        const val EXTRA_STATE = "estado"
        const val EXTRA_RESULT = "result"
    }
}
