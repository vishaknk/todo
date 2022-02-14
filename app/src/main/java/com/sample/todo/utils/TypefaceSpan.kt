package com.sample.todo.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import android.util.LruCache
import androidx.annotation.NonNull

/**
 * Style a [Spannable] with a custom [Typeface].
 *
 * @author Tristan Waddington
 */
class TypefaceSpan(
    @NonNull context: Context,
    @NonNull typefaceName: String
) : MetricAffectingSpan() {
    private var mTypeface: Typeface?
    override fun updateMeasureState(@NonNull p: TextPaint) {
        p.typeface = mTypeface

        // Note: This flag is required for proper typeface rendering
        p.flags = p.flags or Paint.SUBPIXEL_TEXT_FLAG
    }

    override fun updateDrawState(@NonNull tp: TextPaint) {
        tp.typeface = mTypeface

        // Note: This flag is required for proper typeface rendering
        tp.flags = tp.flags or Paint.SUBPIXEL_TEXT_FLAG
    }

    companion object {
        /** An `LruCache` for previously loaded typefaces.  */
        @NonNull
        private val sTypefaceCache =
            LruCache<String, Typeface?>(12)
    }

    /**
     * Load the [Typeface] and apply to a [Spannable].
     */
    init {
        mTypeface =
            sTypefaceCache[typefaceName]
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(
                context.applicationContext
                    .assets, String.format(typefaceName)
            )

            // Cache the loaded Typeface
            sTypefaceCache.put(
                typefaceName,
                mTypeface
            )
        }
    }
}