package me.pljr.destroyblock.block

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import me.pljr.destroyblock.DestroyBlock
import me.pljr.destroyblock.title.PLJRTitle
import me.pljr.destroyblock.title.TitleBuilder
import me.pljr.destroyblock.util.createProgressBar
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Block(
    val name: String,
    val perm: String,
    val location: Location,
    val title: PLJRTitle,
    val rewards: List<String>,
    val randomRewards: List<String>,
    val maxHealth: Int,
    val respawn: Long) {
    var health: Int = maxHealth
    var hologram = HologramsAPI.createHologram(DestroyBlock.instance, location.world.getBlockAt(location).location.add(0.5, 2.5, 0.5))

    init {
        updateHologram()
    }

    fun hit(player: Player) {
        health--
        updateHologram()
        if (health <= 0) destroy(player)
    }

    private fun updateHologram() {
        hologram.clearLines()
        hologram.appendItemLine(ItemStack(Material.DIAMOND_PICKAXE))
        hologram.appendTextLine(name)
        hologram.appendTextLine(createProgressBar(health.toFloat(), maxHealth.toFloat(),
            "■", "&8", "&b"))
    }

    private fun destroy(player: Player) {
        // Effects
        location.world.createExplosion(location, 4F, false, false)
        location.world.strikeLightningEffect(location)
        TitleBuilder(title)
            .replaceTitle("{player}", player.name)
            .replaceSubtitle("{player}", player.name)
            .create().send(player)

        // Removing the block and resetting health
        val block = location.world.getBlockAt(location)
        val blockType = block.type
        block.type = Material.AIR
        health = maxHealth

        // Guaranteed rewards
        rewards.forEach { reward ->
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward.replace("{player}", player.name))
        }

        // Random reward
        val randomReward = randomRewards.random()
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), randomReward.replace("{player}", player.name))

        Bukkit.getScheduler().runTaskLater(DestroyBlock.instance, Runnable {
            block.type = blockType
        }, respawn)
    }
}