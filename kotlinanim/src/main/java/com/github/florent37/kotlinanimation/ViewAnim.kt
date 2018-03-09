package com.github.florent37.kotlinanimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Build
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

fun anim(view: View,
         viewAnimation: ViewAnimation = ViewAnimation(),
         duration: Long = 300,
         startDelay: Long = 0,
         interpolator: Interpolator = LinearInterpolator(),
         block: (ViewAnimation.() -> Unit)): ViewAnimation {
    view.post({
        block.invoke(viewAnimation)

        val animations = mutableListOf<ValueAnimator>()
        viewAnimation.x?.let {
            animations.add(ObjectAnimator.ofFloat(view, View.X, 1f * view.x, 1f * it))
        }
        viewAnimation.y?.let {
            animations.add(ObjectAnimator.ofFloat(view, View.Y, 1f * view.y, 1f * it))
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
                    viewAnimation.then.forEach { function ->
                        function.invoke(viewAnimation)
                    }
                }

                override fun onAnimationStart(animation: Animator?) {
                    viewAnimation.and.forEach { function ->
                        function.invoke(viewAnimation)
                    }
                }
            })
        }

        animations.forEach {
            it.interpolator = interpolator
            it.startDelay = startDelay
            it.duration = duration
            it.start()
        }
    })
    return viewAnimation
}

class ViewAnimation {

    var x: Float? = null
    var y: Float? = null
    var alpha: Float? = null
    var scaleX: Float? = null
    var scaleY: Float? = null
    var rotation: Float? = null
    var rotationX: Float? = null
    var rotationY: Float? = null
    var translationX: Float? = null
    var translationY: Float? = null
    var translationZ: Float? = null

    val properties = mutableListOf<Pair<String, Float>>()

    fun property(name: String, value: Float){
        properties.add(name to value)
    }

    var then = mutableListOf<((ViewAnimation) -> ViewAnimation)>()
    var and = mutableListOf<((ViewAnimation) -> ViewAnimation)>()

    fun then(block: (ViewAnimation) -> ViewAnimation) {
        this.then.add(block)
    }

    fun and(block: (ViewAnimation) -> ViewAnimation) {
        this.and.add(block)
    }

    fun thenAnim(view: View,
                 duration: Long = 300,
                 startDelay: Long = 0,
                 interpolator: Interpolator = LinearInterpolator(),
                 block: (ViewAnimation.() -> Unit)): ViewAnimation {
        val viewAnimation = ViewAnimation()
        then {
            anim(view, viewAnimation, duration, startDelay, interpolator, block)
        }
        return viewAnimation
    }

    fun andAnim(view: View,
                duration: Long = 300,
                startDelay: Long = 0,
                interpolator: Interpolator = LinearInterpolator(),
                block: (ViewAnimation.() -> Unit)): ViewAnimation {
        val viewAnimation = ViewAnimation()
        and {
            anim(view, viewAnimation, duration, startDelay, interpolator, block)
        }
        return viewAnimation
    }
}