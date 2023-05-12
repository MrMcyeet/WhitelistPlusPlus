package me.mcyeet.whitelistplusplus.utils

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream

class YamlDocument(private val configFile: File, private val defaultConfigStream: InputStream) {
    init {
        if (!configFile.exists()) {
            configFile.parentFile.mkdirs()
            configFile.outputStream().use {
                defaultConfigStream.copyTo(it)
            }
        }
    }

    private val yaml = Yaml()
    private val configData: MutableMap<String, Any?> = loadConfig()

    private fun loadConfig(): MutableMap<String, Any?> {
        FileInputStream(configFile).use { fileInputStream ->
            return yaml.load(fileInputStream) as MutableMap<String, Any?>
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(path: String): T {
        if (!this.has(path)) throw NoSuchElementException("Could not find key \"$path\" in ${configFile.path}")
        return path.split(".").fold(configData) { config: Any?, key ->
            (config as Map<String, *>)[key]
        } as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrNull(path: String): T? {
        if (!this.has(path)) return null
        return path.split(".").fold(configData) { config: Any?, key ->
            (config as Map<String, *>)[key]
        } as T
    }

    fun <T> getOrDefault(path: String, default: T): T {
        if (!this.has(path)) return default
        return this.get(path)
    }

    private fun has(path: String): Boolean {
        var value: Any? = configData
        path.split('.').forEach{
            if ((value !is Map<*, *>) || (!(value as Map<*, *>).containsKey(it)))
                return false
            value = (value as Map<*, *>)[it]
        }
        return true
    }

    inline fun <reified T> isType(path: String): Boolean {
        val value = get<Any>(path)
        return value is T
    }
}