package net.cimadai.chatwork

import com.ning.http.client.Response
import dispatch.Defaults._
import dispatch._
import org.json4s._
import org.json4s.jackson.JsonMethods

import scala.concurrent.duration._
import scala.concurrent.{Await, TimeoutException}

trait HttpClient extends JsonMethods {

  object HTTP_METHOD extends Enumeration {
    val GET, POST, PUT, DELETE = Value
  }

  private val absoluteTimeout = 30.seconds
  protected val StatusCodeUndefined = -1
  implicit val formats = DefaultFormats

  private val http = new Http

  /**
   * A method for appending application specified request headers if it required.
   */
  protected def decorateRequest(req: Req): Req = req

  def get(uri: String) = executeRequestAndAwait(uri, HTTP_METHOD.GET, Map.empty)
  def post(uri: String, params: Params) = executeRequestAndAwait(uri, HTTP_METHOD.POST, params)
  def put(uri: String, params: Params) = executeRequestAndAwait(uri, HTTP_METHOD.PUT, params)
  def delete(uri: String, params: Params) = executeRequestAndAwait(uri, HTTP_METHOD.DELETE, params)

  protected def parseAs[ERR, T](response: Response)
      (implicit mft: scala.reflect.Manifest[T], mfe: scala.reflect.Manifest[ERR]): Either[Option[ERR], T] = {
    try {
      val camelized = parse(response.getResponseBody).camelizeKeys
      camelized.extractOpt[T] match {
        case Some(resp) => Right(resp)
        case _ => Left(camelized.extractOpt[ERR])
      }
    } catch {
      case e: Throwable => Left(None)
    }
  }
  protected def getAs[ERR, T](url: String, paramsOrNone: Option[Params] = None)
      (implicit mft: scala.reflect.Manifest[T], mfe: scala.reflect.Manifest[ERR]): Either[Option[ERR], T] = {
    get(url + paramsOrNone.map(_.toQueryString).getOrElse("")) match {
      case Some(response) => parseAs[ERR, T](response)
      case _ => Left(None)
    }
  }
  protected def postAs[ERR, T](url: String, params: Params = Map.empty)
      (implicit mft: scala.reflect.Manifest[T], mfe: scala.reflect.Manifest[ERR]): Either[Option[ERR], T] = {
    post(url, params) match {
      case Some(response) => parseAs[ERR, T](response)
      case _ => Left(None)
    }
  }
  protected def putAs[ERR, T](url: String, params: Params = Map.empty)
      (implicit mft: scala.reflect.Manifest[T], mfe: scala.reflect.Manifest[ERR]): Either[Option[ERR], T] = {
    put(url, params) match {
      case Some(response) => parseAs[ERR, T](response)
      case _ => Left(None)
    }
  }

  type Params = Map[String, Seq[String]]
  implicit class ParamsExtension(params: Params) {
    def toQueryString: String = {
      "?" + params.flatMap(param => {
        s"${param._1}=${param._2.head}"
      }).mkString("&")
    }
  }

  /**
   * Execute a request and await for the response.
   * @param uri A request URL
   * @param method HTTP_METHOD (GET/POST/PUT/DELETE)
   * @param params Request parameters
   * @return Response or None
   */
  private def executeRequestAndAwait(uri: String, method: HTTP_METHOD.Value, params: Params): Option[Response] = {
    try {
      val req = url(uri).setMethod(method.toString).setParameters(params)

      if (method != HTTP_METHOD.GET) {
        req.addHeader("Content-Type", "application/x-www-form-urlencoded")
      }
      val res = Await.result(http(decorateRequest(req)), absoluteTimeout)
      Option(res)
    } catch {
      case e: TimeoutException =>
        None
    }
  }
}
