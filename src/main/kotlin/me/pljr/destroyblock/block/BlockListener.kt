package me.pljr.destroyblock.block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BlockListener(val manager: BlockManager) : Listener {

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val location = event.block.location
        manager.blocks.forEach { (_, block) ->
            if (location.distance(block.location) <= 0.7) {
                event.isCancelled = true
                if (block.perm != "" && !event.player.hasPermission(block.perm)){
                    event.player.velocity = event.player.location.clone().toVector().subtract(block.location.toVector()).multiply(0.5)
                    return
                }
                block.hit(event.player)
            }
        }
    }
}