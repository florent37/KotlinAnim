# KotlinAnim

```kotlin
val username = findViewById<View>(R.id.username)
val avatar = findViewById<View>(R.id.avatar)
val follow = findViewById<View>(R.id.follow)

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
```