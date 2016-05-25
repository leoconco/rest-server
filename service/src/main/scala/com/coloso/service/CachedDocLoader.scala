package com.coloso.service

import java.io.{File, FileInputStream, InputStream}
import java.net.URI

import com.coloso.docs.DocLoader
import com.twitter.inject.Logging

class CachedDocLoader(basePath:URI) extends DocLoader with Logging {
  def get(path:String): InputStream = {
    info(s"paths: $basePath and $path")
    new FileInputStream(new File(basePath.getPath()+"/"+path))
  }
  def head(path:String): InputStream = new FileInputStream(new File(basePath.resolve(path)))

}
