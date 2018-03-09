package florent37.github.com.kotlinanimation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.github.florent37.kotlinanimation.anim


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<TextView>(R.id.text)
        val button = findViewById<View>(R.id.button)

        anim(view, startDelay = 1000, duration = 1000) {
            y = button.y
        }.thenAnim(view, duration = 1000) {
            translationX = -30f
            translationY = -100f
            rotation = 90f
        }.andAnim(button, duration = 1000) {
            alpha = 0.5f
        }
    }


}
