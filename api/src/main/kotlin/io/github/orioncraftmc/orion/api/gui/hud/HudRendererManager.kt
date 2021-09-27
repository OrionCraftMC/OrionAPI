/*
 * MIT License
 *
 * Copyright (c) 2021 OrionCraftMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.orioncraftmc.orion.api.gui.hud

import com.google.common.collect.HashBasedTable
import io.github.orioncraftmc.orion.api.OrionCraft
import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.bridge.matrix
import io.github.orioncraftmc.orion.api.event.impl.HudModComponentRefreshEvent
import io.github.orioncraftmc.orion.api.event.impl.HudRenderEvent
import io.github.orioncraftmc.orion.api.gui.components.Component
import io.github.orioncraftmc.orion.api.gui.components.impl.RectangleComponent
import io.github.orioncraftmc.orion.api.gui.hud.mod.HudModSettingsModel
import io.github.orioncraftmc.orion.api.gui.hud.mod.HudOrionMod
import io.github.orioncraftmc.orion.api.gui.hud.mod.simple.SingleHudOrionMod
import io.github.orioncraftmc.orion.api.onEvent
import io.github.orioncraftmc.orion.api.utils.gui.ComponentUtils

class HudRendererManager {

	private val modElementComponents = HashBasedTable.create<String, Enum<*>, Component>()

	init {
		onEvent<HudRenderEvent> {
			doHudRender()
		}

		onEvent<HudModComponentRefreshEvent<*>> {
			// Delete current component as the next frame the component will be refreshed.
			removeHudModComponent(it.mod, it.element)
		}
	}

	private val parentComponent = RectangleComponent()
	private var lastGameWidth = 0
	private fun doHudRender() {
		if (MinecraftBridge.gameWidth != lastGameWidth) {
			parentComponent.size.apply {
				val sr = MinecraftBridge.scaledResolution
				width = sr.scaledWidthFloat.toDouble()
				height = sr.scaledHeightFloat.toDouble()
			}
		}

		lastGameWidth = MinecraftBridge.gameWidth

		OrionCraft.modManager.mods.values.filterIsInstance<HudOrionMod<*>>().forEach { hudMod ->
			hudMod.allHudElements.forEach hudElements@{ hudElement ->
				if (hasElementVisible(hudMod, hudElement) && !hasElementEnabled(hudMod, hudElement)) {
					removeHudModComponent(hudMod, hudElement)
					return@forEach
				}

				if (hasElementEnabled(hudMod, hudElement)) {
					prepareHudModComponent(hudMod, hudElement)
				}
			}
		}

		modElementComponents.values().forEach {
			matrix {
				ComponentUtils.renderComponent(it, 0, 0)
			}
		}
	}

	private fun hasElementEnabled(
		hudMod: HudOrionMod<*>,
		hudElement: Enum<*>
	): Boolean {
		// If the mod only has one hud element, only check if the mod is enabled.
		// Otherwise, check if the specific element is enabled as well.
		return hudMod.isEnabled && (hudMod is SingleHudOrionMod || getHudElementSettings(hudMod, hudElement).enabled)
	}

	private fun prepareHudModComponent(hudMod: HudOrionMod<*>, hudElement: Enum<*>) {
		if (hasElementVisible(hudMod, hudElement)) return
		val hudSettings = getHudElementSettings(hudMod, hudElement)
		val component = hudMod.getHudComponent(hudSettings.anchor, @Suppress("TYPE_MISMATCH") hudElement)

		if (component != null) {
			// Apply component settings
			component.apply {
				parent = parentComponent
				anchor = hudSettings.anchor
				position = hudSettings.position
				scale = hudSettings.scale
			}
			modElementComponents.put(hudMod.id, hudElement, component)
		}
	}

	private fun getHudElementSettings(
		hudMod: HudOrionMod<*>,
		hudElement: Enum<*>
	) = hudMod.hudSettings[hudElement] ?: HudModSettingsModel()

	private fun removeHudModComponent(mod: HudOrionMod<*>, hudElement: Enum<*>) {
		modElementComponents.remove(mod.id, hudElement)
	}

	private fun hasElementVisible(hudMod: HudOrionMod<*>, hudElement: Enum<*>) =
		modElementComponents.contains(hudMod.id, hudElement)

}
