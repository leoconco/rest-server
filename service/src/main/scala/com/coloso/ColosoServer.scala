package com.coloso

import com.coloso.docs.DocumentsController
import com.coloso.service.DocumentsModule
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.routing.HttpRouter

object ColosoServerMain extends ColosoServer

class ColosoServer extends  HttpServer{

  addFrameworkModule(DocumentsModule)

  override def configureHttp(router: HttpRouter): Unit = {
    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[DocumentsController]
  }

}
