package com.shpp.application.level_1

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.shpp.application.R
import com.shpp.application.databinding.ButtonGoogleBinding


typealias OnButtonGoogleActionListener = () -> Unit
class ButtonGoogleView( //@JvmOverloads
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {       //todo use View or Button (view is better)

    private val binding : ButtonGoogleBinding

    private var listener: OnButtonGoogleActionListener? = null

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this (context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context): this(context, null)

    init {
        val inflator = LayoutInflater.from(context)
        inflator.inflate(R.layout.button_google, this, true)
        binding = ButtonGoogleBinding.bind(this)
        initializedAttributes(attrs, defStyleAttr, defStyleRes)
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    private fun initializedAttributes(attrs: AttributeSet?, defStyleAttr: Int, desStyleRes: Int) {
        if (attrs == null) return
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonGoogleView, defStyleAttr, desStyleRes)  //todo R.styleable.ButtonGoogleView -> to constructor

        binding.labelGoogle.text = typeArray.getText(R.styleable.ButtonGoogleView_text)
        binding.labelGoogle.textSize = typeArray.getDimension(R.styleable.ButtonGoogleView_textSize,
            R.dimen.size_bottom_title.toFloat()
        )
        binding.labelGoogle.setTextColor(typeArray.getColor(R.styleable.ButtonGoogleView_textColor, R.color.textEditButton))

        // Sets font-family for label.
        val customFontResourceId = typeArray.getResourceId(R.styleable.ButtonGoogleView_fontFamily, 0)
        binding.labelGoogle.typeface = ResourcesCompat.getFont(context, customFontResourceId)

        val colorBackground = typeArray.getColor(R.styleable.ButtonGoogleView_backgroundColor, resources.getColor(R.color.light))   //todo deprecated
        binding.root.setBackgroundColor(colorBackground)

        binding.letterG.setImageResource(R.drawable.ic_big_g)
        typeArray.recycle()
    }

    fun initListener() {        //todo not needed, just extend View
        binding.root.setOnClickListener{
            this.listener?.invoke()
        }
    }

    fun setListener(listener: OnButtonGoogleActionListener?) {   //todo not needed, just extend View
        this.listener = listener
    }
}