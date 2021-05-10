
/*
Desenvolvido por Greidimar Ferreira Lagares
Data 10/05/2020
*/

package com.greidimar.conversor_de_moedas.HTTP

import android.content.Context
import android.net.ConnectivityManager
import com.greidimar.conversor_de_moedas.modelos.list_moedas
import org.json.JSONException
import org.json.JSONObject

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset



object Http_lista_moedas {

    val url_lista_moedas = "http://api.currencylayer.com/list?access_key=5bd566dd2635bc1f6a730dcc794e8127"


    @Throws(IOException::class)
    private fun connect(urlAddress: String): HttpURLConnection {
        val second = 1000
        val url = URL(urlAddress)
        val connection = (url.openConnection() as HttpURLConnection).apply {

            readTimeout = 10 * second
            connectTimeout = 15 * second
            requestMethod = "GET"
            doInput = true
            doOutput = false
        }
        connection.connect()
        return connection
    }

    fun hasConnection(ctx: Context): Boolean {
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isConnected
    }

    fun load_moedas(): List<list_moedas>? {
        try {
            val connection = connect(url_lista_moedas)
            val respondeCode = connection.responseCode
            if (respondeCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val json = JSONObject(streamToString(inputStream))
                return readMoedasJson(json)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    @Throws(JSONException::class)
    fun readMoedasJson(json: JSONObject): List<list_moedas> {
        val moedasList = mutableListOf<list_moedas>()
        val jsonCotacaos = json.getJSONObject("currencies")


        for (i in 0 until jsonCotacaos.names().length()) {


            val moedas = list_moedas(
                jsonCotacaos.names().get(i).toString(),
                jsonCotacaos.getString(jsonCotacaos.names().get(i).toString())
            )
            moedasList.add(moedas)
        }
        return moedasList
    }


    @Throws(IOException::class)
    private fun streamToString(inputStream: InputStream): String
    {
        val buffer = ByteArray(1024)
        val bigBuffer = ByteArrayOutputStream()
        var bytesRead: Int
        while(true)
        {
            bytesRead = inputStream.read(buffer)
            if(bytesRead == -1) break
            bigBuffer.write(buffer, 0,bytesRead)
        }
        return  String(bigBuffer.toByteArray(), Charset.forName("UTF-8"))
    }
}