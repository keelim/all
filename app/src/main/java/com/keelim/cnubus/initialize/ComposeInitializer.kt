package com.keelim.cnubus.initialize

class ComposeInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        ComposeView(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(
            ProcessLifecycleInitializer::class.java,
            TimberInitializer::class.java,
        )
    }
}
