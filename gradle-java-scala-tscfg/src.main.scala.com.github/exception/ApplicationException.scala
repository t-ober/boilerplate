package edu.ie3.service.exception

/**
 * Simple interface trait to group application related exceptions together
 */
class ApplicationException(
   private val msg: String,
   private val cause: Throwable = None.orNull
 ) extends Exception(msg, cause)

