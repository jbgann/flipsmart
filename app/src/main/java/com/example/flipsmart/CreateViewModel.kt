package com.example.flipsmart

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.agog.mathdisplay.MTMathView
import com.agog.mathdisplay.parse.MTParseError
import com.agog.mathdisplay.parse.MTParseErrors
import com.example.flipsmart.database.CardDatabaseDAO
import com.example.flipsmart.database.Flashcard
import com.example.flipsmart.databinding.CreateFragmentBinding
import kotlinx.android.synthetic.main.create_fragment.*
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import android.content.Context.INPUT_METHOD_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.inputmethod.InputMethodManager


class CreateViewModel(
    val database: CardDatabaseDAO,
    application: Application,
    topic : String,
    var createFragment : CreateFragment
) : AndroidViewModel(application) {
    val topic = topic
    var currText = MutableLiveData<String>()
    var front : String? = ""
    var back : String? = ""
    var buttonText = MutableLiveData<String>()
    var editingFront : Boolean = true
    var end_button_visibility = MutableLiveData<Boolean>()
    var side_text = MutableLiveData<String>()
    var inputMode : String = "Text"
    var drawEnabled = MutableLiveData<Boolean>()
    var textEnabled = MutableLiveData<Boolean>()
    var latexEnabled = MutableLiveData<Boolean>()
    lateinit var frontType : String
    lateinit var backType : String
    lateinit var canvasMetrics: DisplayMetrics
    var canvasInitialized = false
    val textObserver = Observer<String>({typed_text -> updateLatex(typed_text)})
    private var viewModelJob = Job()
    private var FileIOJob = Job()
    private val IOScope = CoroutineScope(Dispatchers.IO + FileIOJob)
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        currText.value = ""
        buttonText.value = "Flip"
        side_text.value = "Front"
        end_button_visibility.value = true
        setMode(inputMode)
        currText.observe(createFragment,textObserver)
    }

    fun updateLatex(typed_text:String){
        createFragment.binding.reviewMathview.lastError.clear()
        createFragment.binding.reviewMathview.latex=typed_text

    }



    fun setMode(mode : String){
        inputMode = mode
        drawEnabled.value = inputMode == "Draw"
        textEnabled.value = inputMode == "Text"
        latexEnabled.value = inputMode == "Latex"
        //Maybe move this to one of the lifecycle callbacks?
        if (drawEnabled.value!!){
            if (!canvasInitialized) {
                createFragment.binding.drawCanvas.init(canvasMetrics)
            }
            createFragment.binding.drawCanvas.requestFocus()
            createFragment.hideKeyboard()
        }else{
            createFragment.binding.editText4.requestFocus()
            createFragment.showKeyboard()
        }
    }

    fun enableTextMode(){
        setMode("Text")
    }

    fun enableLatexMode(){
        setMode("Latex")
    }

    fun enableDrawMode(){
        setMode("Draw")
    }

    fun flip(){
        front=currText.value
        currText.value =""
        buttonText.value = "Submit"
        if (drawEnabled.value!!){
            front = saveAndClearCanvas()
        }
        editingFront = false
        end_button_visibility.value = false
        side_text.value = "Back"
        frontType = inputMode
    }

    fun submit(){
        back = currText.value
        currText.value = ""
        buttonText.value = "Flip"
        editingFront = true
        end_button_visibility.value = true
        side_text.value = "Front"
        backType = inputMode
        if (drawEnabled.value!!){
            back = saveAndClearCanvas()
        }
        uiScope.launch{
            val newCard = Flashcard()
            newCard.front = front!!
            newCard.back = back!!
            newCard.frontType = frontType
            newCard.backType = backType
            newCard.deck = topic
            insert(newCard)
        }

    }
    fun onFlipSubmit(){
        if (editingFront){
            flip()
        } else{
            submit()
        }
    }


    fun onEnd(view : View){
        view.findNavController().navigate(R.id.action_createFragment_to_masterFragment)
    }

    private fun saveAndClearCanvas() : String {
        val preferences = createFragment.context!!.getSharedPreferences("filenames",MODE_PRIVATE)
        var file_num = 0
        if (preferences.contains("image_file_name")){
            file_num = preferences.getInt("image_file_name",0)
            file_num++
        }
        val edit = preferences.edit()
        edit.putInt("image_file_name",file_num)
        edit.apply()
        val file_name = file_num.toString()
        Log.w("JOHNDEBUG",file_name)
        val bmp = createFragment.drawCanvas.getBitmap()
        val path = createFragment.context!!.filesDir.absolutePath
        val file = File(path,file_name)
        val bmpCopy = bmp.copy(bmp.config,true)
        IOScope.launch {
            saveBMP(bmpCopy, file)
        }
        createFragment.drawCanvas.clear()
        return file.absolutePath


    }

    private suspend fun saveBMP(bmp: Bitmap, file:File){
        withContext(Dispatchers.IO){
            var fOut : OutputStream? = null
            fOut = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG,100,fOut)
            fOut.flush()
            fOut.close()
        }

    }
    private suspend fun insert(card: Flashcard){
        withContext(Dispatchers.IO) {
            database.insert(card)
        }
    }
    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }
}
