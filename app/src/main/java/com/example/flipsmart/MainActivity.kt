//Code for the Google Sheets sign in is mostly derived from the example at
// https://code.luasoftware.com/tutorials/google-sheets-api/setup-google-sheets-api-for-android/

package com.example.flipsmart

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flipsmart.database.Flashcard
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var FileIOJob = Job()
    private val IOScope = CoroutineScope(Dispatchers.IO + FileIOJob)
    lateinit var cardsToWrite : LiveData<List<Flashcard>>

    companion object {
        private const val REQUEST_SIGN_IN = 6942
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MainActivity.REQUEST_SIGN_IN && cardsToWrite != null) {
            if (resultCode == RESULT_OK) {
                GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnSuccessListener { account ->
                        val scopes = listOf(SheetsScopes.SPREADSHEETS)
                        val credential = GoogleAccountCredential.usingOAuth2(this, scopes)
                        credential.selectedAccount = account.account

                        val jsonFactory = JacksonFactory.getDefaultInstance()
                        // GoogleNetHttpTransport.newTrustedTransport()
                        val httpTransport =  AndroidHttp.newCompatibleTransport()
                        val service = Sheets.Builder(httpTransport, jsonFactory, credential)
                            .setApplicationName(getString(R.string.app_name))
                            .build()
                        Log.e("JOHNDEBUG","About to call createSpreadsheet")
                        createSpreadsheet(service)
                    }
                    .addOnFailureListener { e ->
                        Log.e("JOHNDEBUG",e.message)
                    }
            }
        }
    }

    fun requestSignIn(context: Context) {
        cardsToWrite.removeObservers(this)
        /*
        GoogleSignIn.getLastSignedInAccount(context)?.also { account ->
            Timber.d("account=${account.displayName}")
        }
         */

        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            // .requestScopes(Scope(SheetsScopes.SPREADSHEETS_READONLY))
            .requestScopes(Scope(SheetsScopes.SPREADSHEETS))
            .build()
        val client = GoogleSignIn.getClient(context, signInOptions)

        startActivityForResult(client.signInIntent, MainActivity.REQUEST_SIGN_IN)
    }
    private fun checkDeckForImages() : Boolean {
        for (card in cardsToWrite!!.value!!){
            if (card.frontType=="Draw" || card.backType=="Draw"){
                return true
            }
        }
        return false

    }
    private fun createSpreadsheet(service: Sheets) {
        if (checkDeckForImages()){
            val text = "Cards cannot be exported as they contain images."
            val duration = Toast.LENGTH_LONG

            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
            return
        }

        val outerList = MutableList<MutableList<Any>>(cardsToWrite!!.value!!.size,
            {idx -> MutableList<Any>(2,
                {i -> if (i==0) cardsToWrite!!.value!![idx].front else cardsToWrite!!.value!![idx].back})})
        val body = ValueRange().setValues(outerList)
        var spreadsheet = Spreadsheet()
            .setProperties(
                SpreadsheetProperties()
                    .setTitle(cardsToWrite!!.value!![0].deck)
            )
        IOScope.launch(Dispatchers.IO) {
            spreadsheet = service.spreadsheets().create(spreadsheet).execute()
            Log.d("JOHNDEBUG","ID: ${spreadsheet.spreadsheetId}")
            val id = spreadsheet.spreadsheetId
            val result = service.spreadsheets().values()
                .update(id,"A1", body)
                .setValueInputOption("RAW")
                .execute()
            IOScope.launch(Dispatchers.Main){
            val text = "Cards exported to Google Sheets"
            val duration = Toast.LENGTH_LONG

            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()}
        }

    }

}
