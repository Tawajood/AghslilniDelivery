package com.dotjoo.aghslilnidelivery.util.ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
 import com.dotjoo.aghslilnidelivery.R


fun RecyclerView.disableItemChangedAnimation(disable: Boolean = true) {
    (itemAnimator as SimpleItemAnimator?)?.supportsChangeAnimations = !disable
}

fun RecyclerView.setDivider(show: Boolean, @DrawableRes customDivider: Int? = null) {
    if (show) {
        val decoration = DividerItemDecoration(
            context,
            (layoutManager as LinearLayoutManager).orientation
        )
        with(decoration) {
            customDivider?.run { setDrawable(context.getDrawableFromRes(customDivider)!!) }
            addItemDecoration(this)
        }
    }
}

fun getColorFromString(color: String?): Int {
    if (color == null) return 0
    return try {
        Color.parseColor(color)
    } catch (e: Exception) {
        0
    }
}

fun TextView.setTextSizeFromToPx(@DimenRes res: Int) =
    setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(res))

fun View.show(show: Boolean = true) {
    if (show) visible() else gone()
}

fun View.showInvisible(show: Boolean = true) {
    if (show) visible() else invisible()
}

fun View.refreshView() {
    invalidate()
    requestLayout()
}

fun ImageView.setTint(@ColorInt color: Int?) {
    if (color == null) {
        ImageViewCompat.setImageTintList(this, null)
        return
    }
    ImageViewCompat.setImageTintMode(this, PorterDuff.Mode.SRC_IN)
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

fun ImageView.loadGif(
    data: Any?,
    disablePlaceholder: Boolean = false,
    progressBar: ProgressBar? = null,
    placeHolderImage: Int? = null
) {

    progressBar?.visible()
    val options = RequestOptions().error(R.drawable.loading_image).apply {
        if (!disablePlaceholder)
            placeholder(placeHolderImage ?: R.drawable.loading_image)
        diskCacheStrategy(DiskCacheStrategy.NONE)
    }
    Glide.with(this).asGif().load(data).apply(options)
        .addListener(object : RequestListener<GifDrawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<GifDrawable>?,
                isFirstResource: Boolean
            ): Boolean {
                progressBar?.gone()
            //    e.showLogMessage("glide")
                return false
            }

            override fun onResourceReady(
                resource: GifDrawable?,
                model: Any?,
                target: Target<GifDrawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progressBar?.gone()
                return false
            }

        }).into(this)
}

fun ImageView.loadImage(
    data: Any?,
    disableCache: Boolean = true,
    disablePlaceholder: Boolean = false,
    isCircular: Boolean = false,
    progressBar: ProgressBar? = null,
     errorImage: Int = R.drawable.ic_fail,
    placeHolderOnFsImage: Int = R.drawable.loading_image

) {

    progressBar?.visible()
    val options = RequestOptions().error(errorImage).apply {
        if (!disablePlaceholder)
            placeholder(placeHolderOnFsImage  )
        if (isCircular)
            circleCrop()
        diskCacheStrategy(if (disableCache) DiskCacheStrategy.NONE else DiskCacheStrategy.AUTOMATIC)
    }
    Glide.with(this).load(data).apply(options).addListener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            progressBar?.gone()
        //    e.showLogMessage("glide")
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            progressBar?.gone()
            return false
        }

    }).into(this)

}

fun RecyclerView.init(
    context: Context?,
    adapter: RecyclerView.Adapter<*>?,
    type:Int,
    column: Int  =2 // 1 :hor  2:lin 3:grid

) {

    var layoutManager :RecyclerView.LayoutManager? =null
    when(type){
        1-> {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL,
                false
            )
        }
        2->layoutManager = LinearLayoutManager(context)
        else ->{layoutManager = GridLayoutManager(context, column)


        }
    }

    this.layoutManager = layoutManager
    this.setHasFixedSize(false)
    this.adapter = adapter
}

@RequiresApi(Build.VERSION_CODES.M)
fun View.makeForeGroundClickable() {
    val outValue = TypedValue()
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
    foreground = ContextCompat.getDrawable(context, outValue.resourceId)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}







