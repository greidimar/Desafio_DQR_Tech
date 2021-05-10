/*
Desenvolvido por Greidimar Ferreira Lagares
Data 10/05/2020
*/

package com.greidimar.conversor_de_moedas.modelos

data class list_quotes(
    var sigla: String = "",
    var cotacao: String = ""
)
{
    override fun toString()= sigla + " - " + cotacao
}