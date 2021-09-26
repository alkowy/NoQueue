package com.example.noqueue.common

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Group


//internal object GlobalToast {
//    private var toast: Toast? = null
//    @SuppressLint("ShowToast")
//    fun showShort(context: Context?, text: CharSequence?) {
//        try {
//            if(toast!!.view!!.isShown){
//                toast!!.cancel()
//                toast = Toast.makeText(context,text,Toast.LENGTH_SHORT)
//                toast!!.show()
//            }
//        } catch (e: Exception) {
//            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
//            toast!!.show()
//        }
//    }
//    fun showLong(context: Context?, text: CharSequence?) {
//        try {
//            if(toast!!.view!!.isShown){
//                toast!!.cancel()
//                toast = Toast.makeText(context,text,Toast.LENGTH_LONG)
//                toast!!.show()
//            }
//        } catch (e: Exception) {
//            toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
//            toast!!.show()
//        }
//    }
//}


object GlobalToast {


    var toast: Toast? = null

    fun showShort(context: Context?, msg: String) {

        if (context != null) {
            if (toast != null) {
                toast!!.cancel()
            }
            toast = Toast.makeText(context, validateString(msg), Toast.LENGTH_SHORT)
            toast!!.show()
        }
    }
    fun showLong(context: Context?, msg: String) {

        if (context != null) {
            if (toast != null) {
                toast!!.cancel()
            }
            toast = Toast.makeText(context, validateString(msg), Toast.LENGTH_LONG)
            toast!!.show()
        }
    }

    private fun validateString(msg: String?): String {
        return msg ?: "null"
    }

}

fun TextView.setTextAnimation(text: String, duration: Long = 300, completion: (() -> Unit)? = null) {
    fadOutAnimation(duration) {
        this.text = text
        fadInAnimation(duration) {
            completion?.let {
                it()
            }
        }
    }
}

fun View.fadOutAnimation(duration: Long = 300, visibility: Int = View.INVISIBLE, completion: (() -> Unit)? = null) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .withEndAction {
            this.visibility = visibility
            completion?.let {
                it()
            }
        }
}

fun View.fadInAnimation(duration: Long = 300, completion: (() -> Unit)? = null) {
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .withEndAction {
            completion?.let {
                it()
            }
        }
}
fun Group.setAllOnClickListener(listener : View.OnClickListener?) {
    referencedIds.forEach { id ->
        rootView.findViewById<View>(id).setOnClickListener(listener)
    }
}