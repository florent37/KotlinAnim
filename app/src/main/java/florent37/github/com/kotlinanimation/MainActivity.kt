package florent37.github.com.kotlinanimation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.florent37.kotlinanimation.animation
import com.github.florent37.kotlinanimation.centerY
import com.github.florent37.kotlinanimation.floatAnimation
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async

class MainActivity : AppCompatActivity() {

    lateinit var avatar: View
    lateinit var kotlin: View
    lateinit var coroutine: View
    lateinit var follow: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        kotlin = findViewById<View>(R.id.kotlin)
        coroutine = findViewById<View>(R.id.coroutine)
        avatar = findViewById<View>(R.id.avatar)
        follow = findViewById<View>(R.id.follow)

        follow.setOnClickListener { performAnimation() }
    }

    fun performAnimation() = async {
        animation(avatar, startDelay = 1000L) { top = 0f }.join()

        floatAnimation(avatar, 1f, 0.5f){ view, value ->
            view.alpha = value
        }.join()

        val animations = mutableListOf<Job>(
                animation(follow) {
                    top = avatar.y + avatar.height + 16f
                },
                animation(kotlin) {
                    left = avatar.x - kotlin.width - 16f
                    centerY = avatar.centerY()
                },
                animation(coroutine) {
                    left = avatar.x + avatar.width + 16f
                    centerY = avatar.centerY()
                }
        )
        animations.forEach { it.join() } // wait for all animations to complete
    }

}