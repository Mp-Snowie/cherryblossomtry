/*
 * This file is part of LiquidBounce (https://github.com/CCBlueX/LiquidBounce)
 *
 * Copyright (c) 2015 - 2023 CCBlueX
 *
 * LiquidBounce is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LiquidBounce is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LiquidBounce. If not, see <https://www.gnu.org/licenses/>.
 */

package net.ccbluex.liquidbounce.render

import net.ccbluex.liquidbounce.render.engine.Color4b
import net.ccbluex.liquidbounce.render.engine.font.FontRendererBuffers
import org.joml.Matrix4f

abstract class AbstractFontRenderer {
    abstract val size: Float
    abstract val height: Float

    /**
     * Must be called before rendering
     */
    abstract fun begin()

    /**
     * Draws a string with minecraft font markup to this object.
     *
     * @param defaultColor The color of the font when no minecraft-markup applies
     * @param shadow Add a shadow to the font?
     * @return The width of the font, without considering the scaling
     */
    @Suppress("LongParameterList")
    abstract fun draw(
        text: String,
        x0: Float,
        y0: Float,
        defaultColor: Color4b = Color4b.WHITE,
        shadow: Boolean = false,
        z: Float = 0.0f,
        scale: Float = 1.0f
    ): Float

    /**
     */
    abstract fun commit(
        env: RenderEnvironment,
        buffers: FontRendererBuffers
    )

    /**
     * Approximates the width of a text. Accurate except for obfuscated (`§k`) formatting
     */
    abstract fun getStringWidth(
        text: String,
        shadow: Boolean = false
    ): Float
}
