package com.coloso.docs

import java.io.InputStream

import com.google.inject.Inject
import com.twitter.finagle.http.{Cookie, Request}
import com.twitter.finatra.http.Controller
import com.twitter.finatra.http.routing.AdminIndexInfo
import com.twitter.finatra.utils.FuturePools
import com.twitter.inject.Logging

class DocumentsController @Inject()(docLoader: DocLoader) extends Controller with Logging {

  private val futurePool = FuturePools.unboundedPool("documents")

  get("/doc/:name") {
    request: Request =>
      futurePool {
        response.ok.body(docLoader.get(request.getParam("name")))
      }
  }

  head("/doc/:name") {
    request: Request =>
      futurePool {
        error("Not implemented")
        docLoader.head(request.getParam("name"))
        response.notImplemented.body("not implemented")
      }
  }

  get("/admin/status", admin = true, adminIndexInfo = Option(AdminIndexInfo("status", "custom"))) {
    request: Request =>
      futurePool {
        response.notImplemented.body("Soon")
      }
  }

  get("/login") { // TODO: Would be better a filter for this, open Id /oAuth
    request: Request =>
      val user = request.getParam("user")
      val c = new Cookie(name = "loggedIn", value = user)
      c.isSecure = true
      response.ok.cookie(c).plain(s"You are logged in now $user")
  }

  get("/logout") { // TODO: Would be better a filter for this, open Id /oAuth
    request: Request =>
      val user = request.cookies.getValue("loggedIn")
      response.ok.plain("You are logged out now").removeCookie("loggedIn")
  }

  get("/account") {
    request: Request =>
      request.cookies.getValue("loggedIn") match { // TODO: A Filter instead of this
        case Some(user) => response.ok.plain(s"$user is logged in")
        case _ => response.unauthorized.plain("Please login").location("/login")
      }
  }

}

trait DocLoader {
  def get(path: String): InputStream

  def head(path: String): InputStream
}