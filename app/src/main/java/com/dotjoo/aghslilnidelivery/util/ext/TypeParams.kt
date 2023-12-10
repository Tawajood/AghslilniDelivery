package com.dotjoo.aghslilnidelivery.util.ext

import android.content.res.TypedArray
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


fun <T> List<T>?.getElementsInString(): String =
    if (this == null) "" else toString().removeSurrounding("[", "]")


fun <T> ArrayList<T>?.getIfNull(): ArrayList<T> = this ?: arrayListOf()

@Suppress("DEPRECATION")
fun String.parseAsHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

fun <T> DiffUtil.ItemCallback<T>.asConfig(): AsyncDifferConfig<T> {
    return AsyncDifferConfig.Builder(this)
        .setBackgroundThreadExecutor(Dispatchers.IO.asExecutor())
        .build()
}

fun Int?.isIntTrue(): Boolean = this?.toString()?.equals("1") ?: false

fun <T : Any> T.toRequestBodyParam(): RequestBody =
    this.toString().toRequestBody("text/plain".toMediaTypeOrNull())

inline fun <reified T : Enum<T>> TypedArray.getEnum(index: Int, default: T) =
    getInt(index, -1).let {
        if (it >= 0) enumValues<T>()[it] else default
    }
/*

fun Any?.showLogMessage(tag: String? = "WinWin_Tags") =
    Timber.let {
        if (!tag.isNullOrBlank())
            it.tag(tag)
        it.e(this.toString())
    }
*/

fun Any?.isNull(): Boolean = this == null

//get Generic Class Type
@Suppress("UNCHECKED_CAST")
fun <T : Any> Any.getTClass(): Class<T> {
    val type: Type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
    return type as Class<T>
}

fun Double?.roundTo(n: Int): Double {
    return "%.${n}f".format(Locale.ENGLISH, this).toDouble()
}
/*
fun String.isValidUrl(): Boolean {
    return try {
        URLUtil.isValidUrl(this) && Patterns.WEB_URL.matcher(this).matches()
    } catch (e: Exception) {
        Timber.e(e)
        false
    }
}*/

fun CharSequence?.removeSpaces() = this?.filterNot { it.isWhitespace() }


fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String?.isNumeric(): Boolean {
    val regex = """^(-)?[0-9]*((\.)[0-9]+)?$""".toRegex()
    return if (this.isNullOrBlank()) false
    else regex.matches(this.removeSpaces()!!)
}
/*
fun String?.toMyDouble(): Double {
    Timber.e("check: $this")
    return when {
        this.isNumeric() -> {
            this!!.toDouble()
        }
        else -> {
            Timber.e("else")
            0.0
        }
    }
}*/

@RequiresApi(Build.VERSION_CODES.O)
fun String.toDateTime(pattern: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return LocalDateTime.parse(this, formatter)
}

 fun String.toDate( ): LocalDate {
    var pattern ="YYYY-MM-dd"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return LocalDate.parse(this, formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toLocalDate(): LocalDate {
    return toLocalDateTime().toLocalDate()
}

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneId.systemDefault())


