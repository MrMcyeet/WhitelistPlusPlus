package me.mcyeet.whitelistplusplus.listeners

import me.mcyeet.whitelistplusplus.WhitelistPlusPlus.Companion.Config
import org.bukkit.BanList
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener: Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (Bukkit.getWhitelistedPlayers().contains(event.player)) return

        val reason = Config.get<String>("Ban_Reason")
        Bukkit.getBanList(BanList.Type.NAME).addBan(event.player.name, reason, null, null)
        event.player.kickPlayer(reason)
    }

}