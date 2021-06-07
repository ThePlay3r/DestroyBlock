package me.pljr.destroyblock.block

import me.pljr.destroyblock.config.ConfigManager

const val CONF_PATH = "blocks"

class BlockManager(config: ConfigManager) {
    val blocks: HashMap<String, Block> = HashMap()

    init {
        val section = config.getConfigurationSection(CONF_PATH)
        section?.getKeys(false)?.forEach { block ->
            val pth = "$CONF_PATH.$block"
            blocks[block] = Block(
                config.getMaterial("$pth.type"),
                config.getString("$pth.name"),
                config.getString("$pth.perm"),
                config.getLocation("$pth.location"),
                config.getPLJRTitle("$pth.title"),
                config.getStringList("$pth.rewards"),
                config.getStringList("$pth.randomRewards"),
                config.getInt("$pth.health"),
                config.getString("$pth.healthBar.health"),
                config.getString("$pth.healthBar.progressBarSymbol"),
                config.getString("$pth.healthBar.progressBarLocked"),
                config.getString("$pth.healthBar.progressBarUnlocked"),
                config.getString("$pth.info"),
                config.getLong("$pth.respawn")
            )
        }
    }
}