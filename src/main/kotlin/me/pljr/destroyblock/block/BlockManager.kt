package me.pljr.destroyblock.block

import me.pljr.destroyblock.config.ConfigManager

const val CONF_PATH = "blocks"

class BlockManager(config: ConfigManager) {
    val blocks: HashMap<String, Block> = HashMap()

    init {
        val section = config.getConfigurationSection(CONF_PATH)
        section?.getKeys(false)?.forEach { block ->
            blocks[block] = Block(
                config.getString("$CONF_PATH.$block.name"),
                config.getString("$CONF_PATH.$block.perm"),
                config.getLocation("$CONF_PATH.$block.location"),
                config.getPLJRTitle("$CONF_PATH.$block.title"),
                config.getStringList("$CONF_PATH.$block.rewards"),
                config.getStringList("$CONF_PATH.$block.randomRewards"),
                config.getInt("$CONF_PATH.$block.health"),
                config.getLong("$CONF_PATH.$block.respawn")
            )
        }
    }
}