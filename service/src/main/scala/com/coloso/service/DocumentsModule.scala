package com.coloso.service

import java.net.URI

import com.coloso.docs.DocLoader
import com.google.inject.Provides
import com.twitter.finatra.annotations.Flag
import com.twitter.inject.TwitterModule

object DocumentsModule extends TwitterModule {

  @Provides
  def providesDocLoader( @Flag("doc.root") docFolder: String): DocLoader = {
    new CachedDocLoader(new URI(docFolder))
  }

}
