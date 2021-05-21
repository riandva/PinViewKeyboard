package com.riandivayana.pinviewkeyboard.resources

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Property
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import com.riandivayana.pinviewkeyboard.R

class CustomButton : androidx.appcompat.widget.AppCompatButton {

    private lateinit var initialText: String
    private var canvas: Canvas? = null
    private var animatedDrawable: CircularAnimatedDrawable? = null
    private var isLoading = false
    private val paddingProgress = 15
    private val strokeWidth = 10f
    private var buttonType = 0
    private var fontSize = 18f
    private var color = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setButtonType(attrs, context)
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        elevation = 0f
        stateListAnimator = null
        transformationMethod = null

        textSize = fontSize

        setButtonType(attrs, context)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas
        setLoading()
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        if (!text.isNullOrBlank()) initialText = text.toString()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        setBackgroundResource(if (enabled) R.drawable.btn_primary else R.drawable.btn_gray)
    }

    private fun setButtonType(attrs: AttributeSet, context: Context) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomButton,
            0, 0).apply {

            try {
                buttonType = getInteger(R.styleable.CustomButton_ButtonType, 0)
                fontSize = getInteger(R.styleable.CustomButton_customFontSize, 18).toFloat()
                color = getInteger(R.styleable.CustomButton_Color, 0)

            } finally {
                recycle()
            }
        }

        when (color) {
            0 -> setBlue()
            else -> setRed()
        }
        textSize = fontSize
        invalidate()
        requestLayout()
    }

    private fun drawIndeterminateProgress(canvas: Canvas) {
        if (animatedDrawable == null) {
            val offset = (width - height) / 2
            val colorIndicator = when(buttonType) {
                0 -> resources.getColor(R.color.white)
                else -> resources.getColor(R.color.primary)
            }
            animatedDrawable = CircularAnimatedDrawable(colorIndicator, strokeWidth)
            val left: Int = offset + paddingProgress
            val right: Int = width - offset - paddingProgress
            val bottom: Int = height - paddingProgress
            val top: Int = paddingProgress
            animatedDrawable?.setBounds(left, top, right, bottom)
            animatedDrawable?.callback = this
            animatedDrawable?.start()
        } else {
            animatedDrawable?.draw(canvas)
        }
    }

    private fun setLoading() {
        if (isLoading) {
            canvas?.let {
                drawIndeterminateProgress(it)
            }
            text = ""
        } else {
            if (animatedDrawable != null) {
                animatedDrawable?.stop()
                animatedDrawable = null
            }
            text = initialText
        }
    }

    fun showLoading() {
        this.isLoading = true
        isEnabled = false
        setBackgroundResource(R.drawable.btn_primary)
        setLoading()
    }

    fun hideLoading() {
        this.isLoading = false
        isEnabled = true
        setLoading()
    }

    fun setRed() {
        when(buttonType) {
            0 -> {
                setBackgroundResource(R.drawable.btn_primary_red)
                setTextColor(resources.getColor(R.color.white))
            }
            1 -> {
                setBackgroundResource(R.drawable.btn_secondary_red)
                setTextColor(resources.getColor(R.color.primary))
            }
            else -> {
                setBackgroundResource(R.drawable.btn_cancel)
                setTextColor(resources.getColor(R.color.error))
            }
        }
    }

    fun setBlue() {
        when(buttonType) {
            0 -> {
                setBackgroundResource(R.drawable.btn_primary)
                setTextColor(resources.getColor(R.color.white))
            }
            1 -> {
                setBackgroundResource(R.drawable.btn_secondary)
                setTextColor(resources.getColor(R.color.primary))
            }
            else -> {
                setBackgroundResource(R.drawable.btn_cancel)
                setTextColor(resources.getColor(R.color.primary))
            }

        }
    }
}

class CircularAnimatedDrawable(color: Int, private val borderWidth: Float) :
    Drawable(), Animatable {
    private val fBounds = RectF()
    private var objectAnimatorSweep: ObjectAnimator? = null
    private var objectAnimatorAngle: ObjectAnimator? = null
    private var modeAppearing = false
    private val paint: Paint = Paint()
    private var running = false
    private var currentGlobalAngleOffset = 0f

    override fun draw(canvas: Canvas) {
        var startAngle = currentGlobalAngle - currentGlobalAngleOffset
        var sweepAngle = currentSweepAngle
        if (!modeAppearing) {
            startAngle += sweepAngle
            sweepAngle = 360 - sweepAngle - MIN_SWEEP_ANGLE
        } else {
            sweepAngle += MIN_SWEEP_ANGLE.toFloat()
        }
        canvas.drawArc(fBounds, startAngle, sweepAngle, false, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    private fun toggleAppearingMode() {
        modeAppearing = !modeAppearing
        if (modeAppearing) {
            currentGlobalAngleOffset =
                (currentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360
        }
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        fBounds.left = bounds.left + borderWidth / 2f + .5f
        fBounds.right = bounds.right - borderWidth / 2f - .5f
        fBounds.top = bounds.top + borderWidth / 2f + .5f
        fBounds.bottom = bounds.bottom - borderWidth / 2f - .5f
    }

    private val mAngleProperty: Property<CircularAnimatedDrawable, Float> =
        object : Property<CircularAnimatedDrawable, Float>(
            Float::class.java, "angle"
        ) {
            override fun get(`object`: CircularAnimatedDrawable): Float {
                return `object`.currentGlobalAngle
            }

            override fun set(
                `object`: CircularAnimatedDrawable,
                value: Float
            ) {
                `object`.currentGlobalAngle = value
            }
        }

    private val mSweepProperty: Property<CircularAnimatedDrawable, Float> =
        object : Property<CircularAnimatedDrawable, Float>(
            Float::class.java, "arc"
        ) {
            override fun get(`object`: CircularAnimatedDrawable): Float {
                return `object`.currentSweepAngle
            }

            override fun set(
                `object`: CircularAnimatedDrawable,
                value: Float
            ) {
                `object`.currentSweepAngle = value
            }
        }

    private fun setupAnimations() {
        objectAnimatorAngle = ObjectAnimator.ofFloat(this, mAngleProperty, 360f)
        objectAnimatorAngle?.apply {
            interpolator =
                ANGLE_INTERPOLATOR
            duration = ANGLE_ANIMATOR_DURATION.toLong()
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
        }
        objectAnimatorSweep = ObjectAnimator.ofFloat(
            this,
            mSweepProperty,
            360f - MIN_SWEEP_ANGLE * 2
        )
        objectAnimatorSweep?.apply {
            interpolator =
                SWEEP_INTERPOLATOR
            duration = SWEEP_ANIMATOR_DURATION.toLong()
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {
                    toggleAppearingMode()
                }
            })
        }
    }

    override fun start() {
        if (isRunning) {
            return
        }
        running = true
        objectAnimatorAngle!!.start()
        objectAnimatorSweep!!.start()
        invalidateSelf()
    }

    override fun stop() {
        if (!isRunning) {
            return
        }
        running = false
        objectAnimatorAngle!!.cancel()
        objectAnimatorSweep!!.cancel()
        invalidateSelf()
    }

    override fun isRunning(): Boolean {
        return running
    }

    var currentGlobalAngle: Float = 0f
        set(value) {
            field = value
            invalidateSelf()
        }

    var currentSweepAngle: Float = 0f
        set(value) {
            field = value
            invalidateSelf()
        }

    companion object {
        private val ANGLE_INTERPOLATOR: Interpolator = LinearInterpolator()
        private val SWEEP_INTERPOLATOR: Interpolator = DecelerateInterpolator()
        private const val ANGLE_ANIMATOR_DURATION = 1000
        private const val SWEEP_ANIMATOR_DURATION = 1000
        const val MIN_SWEEP_ANGLE = 30
    }

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth
        paint.color = color
        setupAnimations()
    }
}
