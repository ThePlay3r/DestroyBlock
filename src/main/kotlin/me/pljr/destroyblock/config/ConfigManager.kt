package me.pljr.destroyblock.config

import me.pljr.destroyblock.title.PLJRTitle
import me.pljr.destroyblock.title.TitleBuilder
import me.pljr.destroyblock.util.colorString
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.*
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

class ConfigManager(private val plugin: JavaPlugin, private val fileName: String) {
    val file: File
    var config: FileConfiguration

    init {
        val dataFolder = plugin.dataFolder
        if (!dataFolder.exists()) dataFolder.mkdir()
        this.file = File(dataFolder, fileName)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        this.config = YamlConfiguration.loadConfiguration(file)
    }

    fun save() {
        config.save(file)
    }

    fun reload() {
        save()
        config = YamlConfiguration.loadConfiguration(file)
    }

    private fun pathNotFound(path: String) {
        plugin.logger.warning("Path $path was not found in $fileName.")
    }

    private fun isNot(type: String, name: String, path: String) {
        plugin.logger.warning("$name is not a $type in file $fileName on $path.")
    }

    /**
     * Tries to get a colored String from [FileConfiguration].
     *
     * @param path Path to the String
     * @return Colored String if one was found, default value if otherwise.
     */
    fun getString(path: String, def: String = "${ChatColor.RED}$path"): String {
        return if (config.isSet(path)) {
            colorString(config.getString(path, def)!!)
        } else {
            pathNotFound(path)
            def
        }
    }

    /**
     * Tries to get an Integer from [FileConfiguration].
     *
     * @param path Path to the Integer
     * @return Custom Integer if one was found, default value otherwise.
     */
    fun getInt(path: String, def: Int = -1): Int {
        if (config.isSet(path)) {
            if (config.isInt(path)) {
                return config.getInt(path, def)
            }
            isNot("Integer", getString(path), path)
            return def
        }
        pathNotFound(path)
        return def
    }

    /**
     * Tries to get an Double from [FileConfiguration].
     *
     * @param path Path to the Double
     * @return Custom Double if one was found, default value otherwise.
     */
    fun getDouble(path: String, def: Double = -1.0): Double {
        if (config.isSet(path)) {
            if (config.isDouble(path)) {
                return config.getDouble(path, def)
            }
            isNot("Double", getString(path), path)
            return def
        }
        pathNotFound(path)
        return def
    }

    /**
     * Tries to get an Long from [FileConfiguration].
     *
     * @param path Path to the Double
     * @return Custom Long if one was found, default value otherwise.
     */
    fun getLong(path: String, def: Int = -1) = getInt(path, def).toLong()

    /**
     * Gets an Float from [FileConfiguration].
     *
     * @param path Path to the Float.
     * @return Custom Float.
     */
    fun getFloat(path: String, def: Double = -1.0) = getDouble(path, def).toFloat()

    /**
     * Tries to get an Boolean from [FileConfiguration].
     *
     * @param path Path to the boolean
     * @return Custom Boolean if one was found, default value otherwise
     */
    fun getBoolean(path: String, def: Boolean = false): Boolean {
        if (config.isSet(path)) {
            if (config.isBoolean(path)) {
                return config.getBoolean(path, def)
            }
            isNot("Boolean", getString(path), path)
            return def
        }
        pathNotFound(path)
        return def
    }

    /**
     * Tries to get a Colored String ArrayList from [FileConfiguration].
     *
     * @param path Path to the StringList
     * @return Colored StringList if one was found, empty ArrayList otherwise
     */
    fun getStringList(path: String): List<String> {
        if (config.isSet(path)) {
            if (config.isList(path)) {
                val stringList = config.getStringList(path)
                val coloredStringList: MutableList<String> = ArrayList()
                for (string in stringList) {
                    coloredStringList.add(colorString(string))
                }
                return coloredStringList
            }
            isNot("StringList", "Value", path)
            return ArrayList()
        }
        pathNotFound(path)
        return ArrayList()
    }

    /**
     * Tries to get an [EntityType] from [FileConfiguration].
     *
     * @param path Path to the [EntityType]
     * @return Custom [EntityType] if one was found, PIG otherwise
     */
    fun getEntityType(path: String): EntityType {
        if (config.isSet(path)) {
            val entityName = getString(path)
            for (entity in EntityType.values()) {
                if (entity.toString().equals(entityName, ignoreCase = true)) {
                    return entity
                }
            }
            isNot("EntityType", entityName, path)
            return EntityType.PIG
        }
        pathNotFound(path)
        return EntityType.PIG
    }

    /**
     * Saves an array of [Enchantment] to [FileConfiguration].
     *
     * @param path Path to where the array will be saved.
     * @param enchants Enchantments that should be saved.
     */
    fun setEnchantments(path: String, enchants: Map<Enchantment, Int>) {
        val enchs: MutableList<String> = ArrayList()
        for ((key, value) in enchants) {
            enchs.add("${key.key}:$value")
        }
        config[path] = enchs
    }

    /**
     * Tries to get an [ItemStack] from [FileConfiguration].
     *
     * @param path Path to the [ItemStack]
     * @return Custom [ItemStack] if one was found, ItemStack(Material.STONE) otherwise
     */
    fun getItemStack(path: String): ItemStack {
        if (config.isSet(path)) {
            val itemStack = config.getItemStack(path)
            if (itemStack == null) {
                isNot("ItemStack", "?", path)
                return ItemStack(Material.STONE)
            }
            return itemStack
        }
        pathNotFound(path)
        return ItemStack(Material.STONE)
    }

    /**
     * Tries to get an DamageCause from [FileConfiguration].
     *
     * @param path Path to the DamageCause
     * @return Custom DamageCause if one was found, VOID otherwise
     */
    fun getDamageCause(path: String): EntityDamageEvent.DamageCause {
        if (config.isSet(path)) {
            val causeName = getString(path)
            for (cause in EntityDamageEvent.DamageCause.values()) {
                if (cause.toString().equals(causeName, true)) {
                    return cause
                }
            }
            isNot("DamageCause", causeName, path)
            return EntityDamageEvent.DamageCause.VOID
        }
        pathNotFound(path)
        return EntityDamageEvent.DamageCause.VOID
    }

    /**
     * Tries to get an [PLJRTitle] from [FileConfiguration].
     *
     * @param path Path to the [PLJRTitle]
     * @return Custom [PLJRTitle] if one was found.
     */
    fun getPLJRTitle(path: String): PLJRTitle {
        if (config.isSet(path)) {
            val title = Component.text(getString("$path.title"))
            val subtitle = Component.text(getString("$path.subtitle"))
            val inTime = getLong("$path.in")
            val stayTime = getLong("$path.stay")
            val outTime = getLong("$path.out")
            return PLJRTitle(title, subtitle, inTime, stayTime, outTime)
        }
        pathNotFound(path)
        return TitleBuilder(Component.text("Title"), Component.text("Was not found!")).create()
    }

    /**
     * Sets [PLJRTitle] to [PLJRActionBar].
     *
     * @param path Path to where the [PLJRTitle] should be set.
     * @param title [PLJRTitle] that should be set.
     */
    fun setPLJRTitle(path: String, title: PLJRTitle) {
        config["$path.title"] = MiniMessage.get().serialize(title.title)
        config["$path.subtitle"] = MiniMessage.get().serialize(title.subtitle)
        config["$path.in"] = title.inTime
        config["$path.stay"] = title.stayTime
        config["$path.out"] = title.outTime
    }

    /**
     * Tries to get an [ConfigurationSection] from [FileConfiguration].
     *
     * @param path Path to the [ConfigurationSection]
     * @return Custom [ConfigurationSection] if one was found, null otherwise
     *
     * @see .pathNotFound
     */
    fun getConfigurationSection(path: String): ConfigurationSection? {
        if (config.isSet(path)) {
            return config.getConfigurationSection(path)
        }
        pathNotFound(path)
        return null
    }

    /**
     * Tries to get an [World] from [FileConfiguration].
     *
     * @param path Path to the [World]
     * @return [World] if one was found, random from loaded worlds otherwise.
     *
     * @see .getString
     * @see .isNotWorld
     */
    fun getWorld(path: String): World {
        val worldName = getString(path)
        if (Bukkit.getWorld(worldName) == null) {
            isNot("World", worldName, path)
            return Bukkit.getWorlds()[0]
        }
        return Bukkit.getWorld(worldName)!!
    }

    /**
     * Gets an [Location] from [FileConfiguration].
     *
     * @param path Path to the [Location]
     * @return Custom [Location] from the settings.
     *
     * @see .getWorld
     * @see .getInt
     * @see .getFloat
     */
    fun getLocation(path: String): Location {
        val world: World = getWorld("$path.world")
        val x = getDouble("$path.x")
        val y = getDouble("$path.y")
        val z = getDouble("$path.z")
        val yaw = getFloat("$path.yaw")
        val pitch = getFloat("$path.pitch")
        return Location(world, x, y, z, yaw, pitch)
    }
}