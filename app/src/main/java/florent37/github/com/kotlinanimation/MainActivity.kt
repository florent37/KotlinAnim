package florent37.github.com.kotlinanimation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.florent37.kotlinanimation.anim
import com.github.florent37.kotlinanimation.centerY

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val username = findViewById<View>(R.id.username)
        val avatar = findViewById<View>(R.id.avatar)
        val follow = findViewById<View>(R.id.follow)

        follow.setOnClickListener {
            anim(avatar, startDelay = 1000L) {
                x = 16f
                y = 0f
                scaleX = 0.5f
                scaleY = 0.5f
            }.thenAnim(username) {
                x = avatar.x + avatar.width
                centerY = avatar.centerY()
            }.andAnim(follow) {
                x = avatar.x + avatar.width + username.width + 16
                centerY = avatar.centerY()
            }
        }
    }


}
