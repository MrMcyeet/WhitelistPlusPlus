package me.mcyeet.whitelistplusplus.utils

import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream

class YamlDocument(private val yaml: Map<String, Any>) {
    companion object {
        private fun readYaml(yamlFile: File): Map<String, Any> {
            val yaml = Yaml()
            val inputStream = FileInputStream(yamlFile)
            return yaml.load(inputStream) as Map<String, Any>
        }

        private fun readYaml(yamlInputStream: InputStream): Map<String, Any> {
            return Yaml().load(yamlInputStream) as Map<String, Any>
        }
    }

    constructor(yamlFile: File, defaultYaml: File) : this(
        if (!yamlFile.exists()) {
            yamlFile.parentFile.mkdirs()
            yamlFile.outputStream().use { outputStream ->
                defaultYaml.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            readYaml(yamlFile)
        } else {
            readYaml(yamlFile)
        }
    )

    constructor(yamlFile: File, defaultYaml: InputStream) : this(
        if (!yamlFile.exists()) {
            yamlFile.parentFile.mkdirs()
            yamlFile.outputStream().use {
                defaultYaml.copyTo(it)
            }
            readYaml(yamlFile)
        } else {
            readYaml(yamlFile)
        }
    )

    constructor(yamlFile: InputStream) : this(
        readYaml(yamlFile)
    )

    fun <T> get(path: String): T {
        val keys = path.split(".")
        var value: Any? = yaml
        for (key in keys) {
            value = (value as Map<String, Any>)[key] ?: throw NoSuchElementException("Key not found: $key")
        }
        return value as T
    }

    fun has(path: String): Boolean {
        return yaml.contains(path)
    }

    inline fun <reified T> isType(path: String): Boolean {
        val value = get<Any>(path)
        return value is T
    }
}