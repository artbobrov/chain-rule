package utils

import java.io.File
import kotlin.reflect.KClass

abstract class BaseResourcedTestCase(private val resourceDir: File) {
    constructor(cls: KClass<*>, filename: String = DEFAULT_DIR_NAME) : this(TestFileUtils.getResourceDir(cls, filename))

    protected val successDirectory: File
        get() = resourceDir.resolve(DEFAULT_SUCCESS_DIR_NAME)

    protected val failureDirectory: File
        get() = resourceDir.resolve(DEFAULT_FAILURE_DIR_NAME)

    companion object {
        private const val DEFAULT_DIR_NAME = "data"
        private const val DEFAULT_SUCCESS_DIR_NAME = "success"
        private const val DEFAULT_FAILURE_DIR_NAME = "failure"
    }
}
