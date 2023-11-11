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
 *
 */

package net.ccbluex.liquidbounce.features.module.modules.combat

import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.handler
import net.ccbluex.liquidbounce.features.module.Category
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.utils.client.isOldCombat
import net.minecraft.item.ShieldItem
import net.minecraft.item.SwordItem
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
import net.minecraft.util.Hand

/**
 * This module allows the user to block with swords. This makes sense to be used on servers with ViaVersion.
 */
object ModuleSwordBlock : Module("SwordBlock", Category.COMBAT) {

    val onPacket = handler<PacketEvent> {
        // If we are already on the old combat protocol, we don't need to do anything
        if (isOldCombat) {
            return@handler
        }

        val packet = it.packet

        if (packet is PlayerInteractItemC2SPacket) {
            val hand = packet.hand
            val itemInHand = player.getStackInHand(hand) // or activeItem

            if (hand == Hand.MAIN_HAND && itemInHand.item is SwordItem) {
                it.cancelEvent()

                val offHandItem = player.getStackInHand(Hand.OFF_HAND)
                if (offHandItem?.item !is ShieldItem) {
                    PlayerInteractItemC2SPacket(Hand.MAIN_HAND, packet.sequence) // We use the old sequence
                    // Until "now" we should get a shield from the server
                    interaction.sendSequencedPacket(world) { sequence ->
                        PlayerInteractItemC2SPacket(Hand.OFF_HAND, sequence) // This time we use a new sequence
                    }
                } else {
                    PlayerInteractItemC2SPacket(Hand.OFF_HAND, packet.sequence) // We use the old sequence
                }
            }
        }
    }
}
