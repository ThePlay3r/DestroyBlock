package me.pljr.destroyblock.block

import com.Zrips.CMI.CMI
import com.Zrips.CMI.Modules.Holograms.CMIHologram
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
    val type: Material,
    val name: String,
    val perm: String,
    val location: Location,
    val title: PLJRTitle,
    val rewards: List<String>,
    val randomRewards: List<String>,
    val maxHealth: Int,
    val healthRepresentation: String,
    val progressBarSymbol: String,
    val progressBarLocked: String,
    val progressBarUnlocked: String,
    val info: String,
    val respawn: Long) {
    var health: Int = maxHealth
    var hologram = CMIHologram(name, location.world.getBlockAt(location).location.add(0.5, 3.0, 0.5))

    init {
        CMI.getInstance().hologramManager.addHologram(hologram)
        updateHologram()
    }

    fun hit(player: Player) {
        health--
        updateHologram()
        if (health <= 0) destroy(player)
    }

    private fun updateHologram() {
        hologram.lines = listOf(
            "ICON:$type",
            name,
            healthRepresentation
                .replace("{health}", "$health")
                .replace("{maxHealth}", "$maxHealth"),
            createProgressBar(health.toFloat(), maxHealth.toFloat(),
                progressBarSymbol, progressBarLocked, progressBarUnlocked),
            "",
            info)
        hologram.update()
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
            updateHologram()
        }, respawn)
    }
}