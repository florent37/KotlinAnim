package com.github.florent37.kotlinanimation

import android.animation.*
import android.os.Build
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.suspendCoroutine

fun floatAnimation(view: View,
                       startValue: Float,
                       endValue: Float,
                       duration: Long = 300,
                       startDelay: Long = 0,
                       evaluator: TypeEvaluator<Number> = FloatEvaluator(),
                       interpolator: Interpolator = LinearInterpolator(),
                       valueListener: (View, Float) -> Unit) = launch {
    suspendCoroutine<ViewAnimation> { continuation ->
        view.post {
            val viewAnimation = ViewAnimation()

            ValueAnimator.ofFloat(startValue, endValue).apply {
                addUpdateListener { valueAnimator ->
                    valueListener.invoke(view, valueAnimator.animatedValue as Float)
                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        continuation.resume(viewAnimation)
                    }
                })

                this.setEvaluator(evaluator)
                this.interpolator = interpolator
                this.startDelay = startDelay
                this.duration = duration
                start()
            }
        }
    }
}

fun intAnimation(view: View,
                   startValue: Int,
                   endValue: Int,
                   duration: Long = 300,
                   startDelay: Long = 0,
                   evaluator: TypeEvaluator<Int> = IntEvaluator(),
                   interpolator: Interpolator = LinearInterpolator(),
                   valueListener: (Int) -> Unit) = launch {
    suspendCoroutine<ViewAnimation> { continuation ->
        view.post {
            val viewAnimation = ViewAnimation()

            ValueAnimator.ofInt(startValue, endValue).apply {

                addUpdateListener { valueAnimator ->
                    valueListener.invoke(valueAnimator.animatedValue as Int)
                }
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        continuation.resume(viewAnimation)
                    }
                })

                this.setEvaluator(evaluator)
                this.interpolator = interpolator
                this.startDelay = startDelay
                this.duration = duration
                start()
            }
        }
    }
}


fun animation(view: View,
              duration: Long = 300,
              startDelay: Long = 0,
              interpolator: Interpolator = LinearInterpolator(),
              block: (ViewAnimation.() -> Unit)) = launch {

    suspendCoroutine<ViewAnimation> { continuation ->
        view.post {
            val viewAnimation = ViewAnimation();

            block.invoke(viewAnimation)

            val animations = mutableListOf<ValueAnimator>()
            viewAnimation.left?.let {
                animations.add(ObjectAnimator.ofFloat(view, View.X, 1f * view.x, 1f * it))
            }
            viewAnimation.right?.let {
                animations.add(ObjectAnimator.ofFloat(view, View.X, 1f * view.x, 1f * -view.width))
            }
            viewAnimation.centerX?.let {
                animations.add(ObjectAnimator.ofFloat(view, View.X, 1f * view.x, 1f * it - view.width / 2f))
            }
            viewAnimation.top?.let {
                animations.add(ObjectAnimator.ofFloat(view, View.Y, 1f * view.y, 1f * it))
            }
            viewAnimation.bottom?.let {
                animations.add(ObjectAnimator.ofFloat(view, View.Y, 1f * view.y, 1f * it - view.height))
            }
            viewAnimation.centerY?.let {
                animations.add(ObjectAnimator.ofFloat(view, View.Y, 1f * view.y, 1f * it - view.height / 2f))
            }
            viewAnimation.alpha?.let {
                animations.add(ObjectAnimator.ofFloat(view, View.ALPHA, 1f * view.alpha, 1f * it))
            }
            viewAnimation.rotation?.let {
                animations.add(ObjectAnimator.ofFloat(view, View.ROTATION, 1f * view.rotation, 1f * it))
            }
            viewAnimation.rotationX?.let {
                animations.add(ObjectAnimator.ofFloat(view, View.ROTATION_X, 1f * view.rotationX, 1f * it))
            }
            viewAnimation.rotationY?.let {
                animations.add(ObjectAnimator.ofFloat(view, View.ROTATION_Y, 1f * view.rotationY, 1f * it))
            }
            viewAnimation.scaleX?.let {
                animations.add(ObjectAnimator.ofFloat(view, View.SCALE_X, 1f * view.scaleX, 1f * it))
            }
            viewAnimation.scaleY?.let {
                animations.add(ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f * view.scaleY, 1f * it))
            }

            viewAnimation.translationX?.let {
                animations.add(ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 1f * view.translationX, 1f * it))
            }
            viewAnimation.translationY?.let {
                animations.add(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 1f * view.translationY, 1f * it))
            }

            viewAnimation.properties.forEach {
                val propertyName = it.first
                val values = it.second
                animations.add(ObjectAnimator.ofFloat(view, propertyName, values))
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewAnimation.translationZ?.let {
                    animations.add(ObjectAnimator.ofFloat(view, View.TRANSLATION_Z, 1f * view.translationZ, 1f * it))
                }
            }

            if (animations.isNotEmpty()) {
                animations[0].addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        continuation.resume(viewAnimation)
                    }
                })
            }

            animations.forEach {
                it.interpolator = interpolator
                it.startDelay = startDelay
                it.duration = duration
                it.start()
            }

        }
    }
}

class ViewAnimation {

    var left: Float? = null
    var x: Float? = null
        set(value) {
            left = value
            field = value
        }

    var right: Float? = null
    var top: Float? = null
    var y: Float? = null
        set(value) {
            top = value
            field = value
        }
    var bottom: Float? = null
    var alpha: Float? = null
    var scale: Float? = null
        set(value) {
            scaleX = value
            scaleY = value
            field = value
        }

    var scaleX: Float? = null
    var scaleY: Float? = null
    var centerY: Float? = null
    var centerX: Float? = null
    var rotation: Float? = null
    var rotationX: Float? = null
    var rotationY: Float? = null
    var translationX: Float? = null
    var translationY: Float? = null
    var translationZ: Float? = null

    val properties = mutableListOf<Pair<String, Float>>()

    fun property(name: String, value: Float) {
        properties.add(name to value)
    }
}

fun View.centerX(): Float {
    return this.x + this.width / 2f;
}

fun View.centerY(): Float {
    return this.y + this.height / 2f;
}