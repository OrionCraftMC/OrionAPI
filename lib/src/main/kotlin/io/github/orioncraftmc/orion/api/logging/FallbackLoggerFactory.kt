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

package io.github.orioncraftmc.orion.api.logging

object FallbackLoggerFactory: LoggerFactory {
	override fun create(name: String): Logger {
		return FallbackLogger
	}
}

object FallbackLogger: Logger {
	override fun name(): String {
		return "fallback"
	}

	override fun debug(str: String, vararg args: Any) {
		println("[debug] " + str.format(args))
	}

	override fun info(str: String, vararg args: Any) {
		println("[info] " + str.format(args))
	}

	override fun warn(str: String, vararg args: Any) {
		println("[warn] " + str.format(args))
	}

	override fun error(str: String, vararg args: Any) {
		println("[error] " + str.format(args))
	}

	override fun fatal(str: String, vararg args: Any) {
		println("[fatal] " + str.format(args))
	}
}
