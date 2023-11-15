package com.shpp.application.level_4.presentation.utils.custom_views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.shpp.application.R
import kotlin.math.min

class ButtonGoogleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.style,
    defStyleRes: Int = R.style.ButtonGoogleViewStyle
) : View(
    context,
    attrs,
    defStyleAttr,
    defStyleRes
) {

    private val paintButton: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var width: Float = 0f
    private var height: Float = 0f
    private var logoGoogle: Drawable?
    private var text: String = ""
    private var colorBackground: Int = 0
    private var textSize: Float = 0f
    private var textColor: Int = 0
    private var textFontFamily: Typeface? = null
    private val PADDING_BETWEEN_ICON_TEXT: Int = 30

    init {
        logoGoogle = ContextCompat.getDrawable(context, R.drawable.ic_big_g)
        initializedAttributes(attrs, defStyleAttr, defStyleRes)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val desiredWidth = receiveDesiredWidth(widthMode, widthSize)
        val desiredHeight = receiveDesiredHeight(heightMode, heightSize)

        setMeasuredDimension(desiredWidth.toInt(), desiredHeight.toInt())
    }

    private fun receiveDesiredWidth(widthMode: Int, widthSize: Int): Number {
        return when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(
                width,
                widthSize.toFloat()
            )

            else -> width
        }
    }

    private fun receiveDesiredHeight(heightMode: Int, heightSize: Int): Number {
        return when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(
                height,
                heightSize.toFloat()
            )

            else -> height
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(colorBackground)
        drawText(canvas)
        putBoundsLogoGoogle()

        logoGoogle?.draw(canvas)
    }

    private fun putBoundsLogoGoogle() {
        logoGoogle?.let {
            val widthLogoAndText =
                (it.intrinsicWidth + paintButton.measureText(text) + PADDING_BETWEEN_ICON_TEXT)
            val iconLeft = ((width - widthLogoAndText) / 2).toInt()
            val iconTop = (height - it.intrinsicHeight) / 2
            it.setBounds(
                iconLeft, iconTop.toInt(), iconLeft + it.intrinsicWidth,
                (iconTop + it.intrinsicHeight).toInt()
            )
        }
    }

    private fun drawText(canvas: Canvas?) {
        paintButton.color = textColor
        paintButton.textSize = textSize
        paintButton.typeface = textFontFamily
        val widthLogoAndText =
            paintButton.measureText(text) + PADDING_BETWEEN_ICON_TEXT + (logoGoogle?.intrinsicWidth
                ?: 0)
        val textX = ((width - widthLogoAndText) / 2 ) + PADDING_BETWEEN_ICON_TEXT + (logoGoogle?.intrinsicWidth
                ?: 0)
        val textY = ((logoGoogle?.intrinsicHeight ?: textSize).toFloat())

        canvas?.drawText(text.uppercase(), textX, textY, paintButton)
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    private fun initializedAttributes(attrs: AttributeSet?, defStyleAttr: Int, desStyleRes: Int) {
        if (attrs == null) return
        val typeArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.ButtonGoogleView,
            defStyleAttr,
            desStyleRes
        )

        width = typeArray.getDimension(
            R.styleable.ButtonGoogleView_width,
            R.dimen.width_google.toFloat()
        )
        height = typeArray.getDimension(
            R.styleable.ButtonGoogleView_height,
            R.dimen.height_google.toFloat()
        )

        text = typeArray.getText(R.styleable.ButtonGoogleView_text).toString()
        textSize = typeArray.getDimension(
            R.styleable.ButtonGoogleView_textSize,
            R.dimen.size_bottom_title.toFloat()
        )
        textColor =
            typeArray.getColor(R.styleable.ButtonGoogleView_textColor, R.color.textEditButton)

        // Sets font-family for label.
        val customFontResourceId =
            typeArray.getResourceId(R.styleable.ButtonGoogleView_fontFamily, 0)
        textFontFamily = ResourcesCompat.getFont(context, customFontResourceId)

        colorBackground = typeArray.getColor(
            R.styleable.ButtonGoogleView_backgroundColor,
            ContextCompat.getColor(context, R.color.light)
        )

        typeArray.recycle()
    }
}