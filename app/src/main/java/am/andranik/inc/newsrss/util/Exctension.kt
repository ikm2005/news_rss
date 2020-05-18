package am.andranik.inc.newsrss.util

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun String.Companion.empty() = ""

fun Fragment.showMessage(message: String) {
    val errorSnackBar: Snackbar? =
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG) }
    errorSnackBar?.show()
}

fun Fragment.showMessage(messageResId: Int) {
    val errorSnackBar: Snackbar? =
        view?.let { Snackbar.make(it, messageResId, Snackbar.LENGTH_LONG) }
    errorSnackBar?.show()
}

fun View.showMessage(message: String) {
    val errorSnackBar: Snackbar? =
        Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    errorSnackBar?.show()
}

fun View.showMessage(messageResId: Int) {
    val errorSnackBar: Snackbar? =
        Snackbar.make(this, messageResId, Snackbar.LENGTH_LONG)
    errorSnackBar?.show()
}