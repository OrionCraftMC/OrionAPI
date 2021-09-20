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

package io.github.orioncraftmc.orion.api.gui.components.impl

import com.github.ajalt.colormath.Color
import com.github.ajalt.colormath.model.RGBInt
import io.github.orioncraftmc.orion.api.bridge.MinecraftBridge
import io.github.orioncraftmc.orion.api.bridge.drawString
import io.github.orioncraftmc.orion.api.gui.components.AbstractComponent
import io.github.orioncraftmc.orion.api.gui.model.Size

class LabelComponent(var text: String = "", var color: Color = RGBInt(255, 255, 255)) : AbstractComponent() {

	var hasDropShadow: Boolean = true

	override var size: Size
		get() = super.size.apply {
			width = MinecraftBridge.fontRenderer.getStringWidth(text).toDouble()
			height = MinecraftBridge.fontRenderer.fontHeight.toDouble()
		}
		set(value) {}

	override fun renderComponent(mouseX: Int, mouseY: Int) {
		MinecraftBridge.fontRenderer.drawString(text, 0, 0, color, hasDropShadow)
	}
}