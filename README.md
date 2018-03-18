# KotlinAnim

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