package com.keelim.bus

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

class SampleActivity : Activity() {
    private lateinit var compositeDisposable: CompositeDisposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 이벤트를 보낼 때
        RxEventBus.getInstance().sendEvent("testEvent")


        // 이벤트를 받을 때
        compositeDisposable = CompositeDisposable()

        val disposable: Disposable = RxEventBus.getInstance()
                .events
                .subscribe({ t -> Toast.makeText(this@SampleActivity, t, Toast.LENGTH_SHORT).show() }, object : Consumer<Throwable> {
                    override fun accept(t: Throwable?) {
                        Toast.makeText(this@SampleActivity, "error", Toast.LENGTH_SHORT).show()
                    }

                })
        compositeDisposable.addAll(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}