package utils

import java.io.File
import kotlin.reflect.KClass

object TestFileUtils {
    fun getResourceDir(cls: KClass<*>, filename: String): File {
        val resource = cls.java.getResource(filename) ?: error("Resource file $filename not found to ${cls.simpleName}")
        return File(resource.toURI())
    }
}