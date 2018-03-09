# KotlinAnim

```kotlin
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
    y = button.left
}
```