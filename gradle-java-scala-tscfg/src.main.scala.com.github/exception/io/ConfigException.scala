package edu.ie3.service.exception.io

import edu.ie3.service.exception.ApplicationException

case class ConfigException(msg: String) extends ApplicationException(msg)
