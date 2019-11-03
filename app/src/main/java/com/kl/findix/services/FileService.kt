package com.kl.findix.services

interface FileService {
    fun getDirectoryPath(directory: String): List<String>
    fun getFilePaths(direcory: String): List<String>
}