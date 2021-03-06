package io.github.boopited.droidutils.thread

import android.os.Handler
import android.os.Looper
import io.github.boopited.droidutils.rx.subscribeOnIO
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

val uiHandler = Handler(Looper.getMainLooper())
inline fun runOnUi(crossinline block:()->Unit) {
    uiHandler.post{
        block.invoke()
    }
}

inline fun runOnUiDelay(delay:Long, crossinline block:()->Unit) {
    uiHandler.postDelayed({
        block.invoke()
    }, delay)
}


inline fun runOnWork(crossinline block:()->Unit) {
    Flowable.fromCallable{
        block.invoke()
    }.subscribeOnIO().subscribe()
}

inline fun runOnWorkDelay(delay: Long, crossinline block:()->Unit) {
    Flowable.fromCallable{ block.invoke() }
            .delay(delay, TimeUnit.MILLISECONDS)
            .subscribeOnIO().subscribe()
}