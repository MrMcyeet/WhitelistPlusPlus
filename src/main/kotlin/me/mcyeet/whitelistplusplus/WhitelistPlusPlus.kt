package me.mcyeet.whitelistplusplus

import me.mcyeet.whitelistplusplus.listeners.PlayerJoinListener
import me.mcyeet.whitelistplusplus.utils.YamlDocument
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileNotFoundException

class WhitelistPlusPlus : JavaPlugin() {
    companion object {
        lateinit var Config: YamlDocument
        lateinit var Plugin: JavaPlugin
    }

    override fun onLoad() {
        val defaultConfig = getResource("config.yml") ?: throw FileNotFoundException("Default config is either missing or unreadable")
        Config = YamlDocument(File(dataFolder, "config.yml"), defaultConfig)

        Plugin = this
    }

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(PlayerJoinListener(), Plugin)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}