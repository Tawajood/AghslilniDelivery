package com.dotjoo.aghslilnidelivery.util.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import java.io.File

fun Fragment.replaceFragment(containerId: Int, fragment: Fragment) {
    activity?.supportFragmentManager?.commit {
        replace(containerId, fragment)
    }
}

fun Activity.getUriFromFileProvider(file: File): Uri = FileProvider.getUriForFile(
    this,
    "com.example.DotShop.provider",
    file
)

fun Fragment.showKeyboard(): Unit? = activity?.let(FragmentActivity::showKeyboard)
fun Fragment.hideKeyboard() = activity?.hideKeyboard()

fun Activity.showKeyboard() = WindowInsetsControllerCompat(window, window.decorView).show(
    WindowInsetsCompat.Type.ime()
)

fun Activity.hideKeyboard() =
    WindowInsetsControllerCompat(window, window.decorView).hide(WindowInsetsCompat.Type.ime())


fun LifecycleOwner.getStatusBarHeight(): Int {
    val res: Resources = if (this is AppCompatActivity) {
        resources
    } else {
        this as Fragment
        resources
    }
    val statusBarRes = res.getIdentifier("status_bar_height", "dimen", "android")
    return res.getDimensionPixelSize(statusBarRes)
}
fun openMap(context: Context, latitude :String, longitude :String){


    val uri = "https://www.google.com.tw/maps/place/$latitude,$longitude"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    context. startActivity(intent)


    /*   val uri: String = java.lang.String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude)
       val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
       context.startActivity(intent)*/
}
fun Fragment.setActivityResult(bundle: Bundle? = null, resultCode: Int = Activity.RESULT_OK) {
    activity?.setResult(resultCode, Intent().apply {
        if (bundle != null)
            putExtras(bundle)
    })
    activity?.finish()
}

/*This called when method onBackClick in activity is called */
fun Fragment.onBackClick(callback: (onBackPressedCallback: OnBackPressedCallback) -> Unit) {
    // This callback will only be called when Fragment is at least Started.
    activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
        isEnabled = true
        callback.invoke(this)
    }
}

val Fragment.supportFragmentManager: FragmentManager
    get() = requireActivity().supportFragmentManager

//Binding View (Activity - Fragment) on the fly
//usually used in base Fragment/Activity
@Suppress("UNCHECKED_CAST")
fun <B : ViewDataBinding> LifecycleOwner.bindView(container: ViewGroup? = null): B {
    return if (this is Activity) {
        val inflateMethod = getTClass<B>().getMethod("inflate", LayoutInflater::class.java)
        val invokeLayout = inflateMethod.invoke(null, this.layoutInflater) as B
        this.setContentView(invokeLayout.root)
        invokeLayout
    } else {
        val fragment = this as Fragment
        val inflateMethod = getTClass<B>().getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            error("Cannot access view bindings. View lifecycle is ${lifecycle.currentState}!")
        }
        val invoke: B = inflateMethod.invoke(null, layoutInflater, container, false) as B
        invoke
    }
}


fun AppCompatActivity.setWindowLayoutDirection(layoutDirection: Int) {
    window.decorView.layoutDirection = layoutDirection
}

fun <T : AppCompatActivity> Fragment.showActivity(
    activity: Class<T>,
    args: Bundle? = null,
    clearAllStack: Boolean = false
) {
    val intent = Intent(requireContext(), activity).apply {
        if (args != null)
            putExtras(args)
    }
    startActivity(intent)
    if (clearAllStack)
        requireActivity().finishAffinity()
}

inline fun <reified T : AppCompatActivity> Fragment.castToActivity(
    callback: (T?) -> Unit,
): T? {
    return if (requireActivity() is T) {
        callback(requireActivity() as T)
        requireActivity() as T
    } else {
        callback(null)
        null
    }
}


@Suppress("DEPRECATION")
inline val Fragment.windowWidth: Int
    @SuppressLint("ObsoleteSdkInt")
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = requireActivity().windowManager.currentWindowMetrics
            val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
            metrics.bounds.width() - insets.left - insets.right
        } else {
            val view = requireActivity().window.decorView
            val insets: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val res = WindowInsetsCompat.toWindowInsetsCompat(view.rootWindowInsets, view)
                    .getInsets(WindowInsetsCompat.Type.systemBars())
                return resources.displayMetrics.widthPixels - res.left - res.right
            } else {
                val displayMetrics = DisplayMetrics()
                requireActivity()
                    .windowManager.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.widthPixels
            }
            return insets
        }
    }



