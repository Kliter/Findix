package com.kl.findix.services

import java.io.File

class FileServiceImpl: FileService {
    override fun getDirectoryPath(directory: String): List<String> {
        val file = File(directory)
        val listFiles: Array<File> = file.listFiles()
        return listFiles.filter {
            it.isDirectory
        }.map {
            it.absolutePath
        }
    }

    override fun getFilePaths(direcory: String): List<String> {
        val file = File(direcory)
        val listFiles: Array<File> = file.listFiles()
        return listFiles.filter {
            it.isFile
        }.map {
            it.absolutePath
        }
    }
}