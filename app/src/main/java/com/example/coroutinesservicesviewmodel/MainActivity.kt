package com.example.coroutinesservicesviewmodel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ExampleViewModel
    override fun onDestroy() {
        Log.d("VM","Activity Destroyed")
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // ViewModel Example logic
        viewModel=ViewModelProvider(this).get(ExampleViewModel::class.java)

        viewModel.currentNumber.observe(this, Observer {
            countTextView.text = it.toString()
        })
        viewModel.currentBoolean.observe(this, Observer {
            boolTextView.text = it.toString()
        })

        incrementText()


        // service example logic
        val start = findViewById<Button>( R.id.startButton );

        // assigning ID of stopButton
        // to the object stop
        val stop =  findViewById<Button>( R.id.stopButton );

        // declaring listeners for the
        // buttons to make them respond
        // correctly according to the process
        start?.setOnClickListener()
        {
            // displaying a toast message
//            Toast.makeText(this@MainActivity, R.string.message, Toast.LENGTH_LONG).show() }
            startService(Intent(this, NewService::class.java))

        }
        stop?.setOnClickListener(){
            stopService(Intent(this, NewService::class.java))

        }



        // coroutines example logic
        val scope = CoroutineScope(IO)

        cButton.setOnClickListener {
            setNewText("Click!")

            scope.launch {
                fakeApiRequest()
            }
        }
        cancelButton.setOnClickListener {
            setNewText("Cancelled!")
            scope.cancel()

        }
    }

    private fun setNewText(input: String){
        val newText = cText.text.toString() + "\n$input"
        cText.text = newText
    }
    private suspend fun setTextOnMainThread(input: String) {
        withContext (Main) {
            setNewText(input)
        }
    }

    private suspend fun fakeApiRequest() {
        logThread("fakeApiRequest")

        val result1 = getResult1FromApi() // wait until job is done

        if (result1 == "Result #1") {
            //ljljkjlkjkljlkj

            setTextOnMainThread("Got $result1")

            val result2 = getResult2FromApi() // wait until job is done

            if (result2 == "Result #2") {
                setTextOnMainThread("Got $result2")
            } else {
                setTextOnMainThread("Couldn't get Result #2")
            }
        } else {
            setTextOnMainThread("Couldn't get Result #1")
        }
    }


    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000) // Does not block thread. Just suspends the coroutine inside the thread
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        logThread("getResult2FromApi")
        delay(1000)
        return "Result #2"
    }

    private fun logThread(methodName: String){
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }







    private fun incrementText() {
    countButton.setOnClickListener{
        viewModel.increaseNumber()
        viewModel.changeBool()
    }
    }
}