package me.pljr.destroyblock

import me.pljr.destroyblock.block.BlockListener
import me.pljr.destroyblock.block.BlockManager
import me.pljr.destroyblock.config.ConfigManager
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin

class DestroyBlock : JavaPlugin() {
    companion object {
        lateinit var instance: DestroyBlock
            private set
        lateinit var bukkitAudiences: BukkitAudiences
            private set
    }

    private lateinit var configManager: ConfigManager
    private lateinit var blockManager: BlockManager

    override fun onEnable() {
        instance = this
        setupAdventure(this)
        setupConfig()
        setupManagers()
        setupListeners()
    }

    private fun setupAdventure(plugin: JavaPlugin) {
        bukkitAudiences = BukkitAudiences.create(plugin)
    }

    private fun setupConfig() {
        saveDefaultConfig()
        configManager = ConfigManager(this, "config.yml")
    }

    private fun setupManagers() {
        blockManager = BlockManager(configManager)
    }

    private fun setupListeners() {
        val pluginManager = server.pluginManager
        pluginManager.registerEvents(BlockListener(blockManager), this)
    }
}