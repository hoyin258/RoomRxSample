package com.yintro.roomrxsample

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    lateinit var adapter: ArrayAdapter<Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { addPerson(editText.text.toString(), editText2.text.toString()) }

        adapter = ArrayAdapter<Person>(this, android.R.layout.simple_list_item_1)
        listView.adapter = adapter

        registerAllPersonListener()
    }

    fun addPerson(firstName: String, lastName: String) {
        val person = Person(0, firstName, lastName)

        Single.fromCallable { MyApp.database?.personDao()?.insert(person) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun registerAllPersonListener() {

        MyApp.database?.personDao()?.getAllPeople()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { listOfPeople ->
                    adapter.clear()
                    adapter.addAll(listOfPeople)
                }
    }


}