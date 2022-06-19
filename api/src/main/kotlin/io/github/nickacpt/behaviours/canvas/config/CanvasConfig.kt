package io.github.nickacpt.behaviours.canvas.config

data class CanvasConfig<ColorType>(
	val colours: CanvasColourConfig<ColorType> = CanvasColourConfig(),
	var borderWidth: Float = 1f,
)
