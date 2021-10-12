package edu.ie3.service.consume

import com.typesafe.config.{ConfigFactory, Config => TypesafeConfig}
import edu.ie3.service.config.ArgsParser
import edu.ie3.service.config.ArgsParser.Arguments
import edu.ie3.service.exception.io.ConfigException

trait ApplicationHelper {

  def prepareConfig(args: Array[String]): (Arguments, TypesafeConfig) = {

    val parsedArgs = ArgsParser.parse(args) match {
      case Some(pArgs) => pArgs
      case None =>
        System.exit(-1)
        throw new IllegalArgumentException(
          "Unable to parse provided Arguments."
        )
    }

    // ConfigConsistencyComparator.parseBeamTemplateConfFile(parsedArgs.configLocation.get) // todo implement

    // check if a config is provided
    val parsedArgsConfig = parsedArgs.config match {
      case None =>
        throw ConfigException(
          "Please provide a valid config file via --config <path-to-config-file>."
        )
      case Some(parsedArgsConfig) => parsedArgsConfig
    }

    // set config file location as system property // todo do we need this? (same is valid for additional parsing below)
    System.setProperty(
      "configFileLocation",
      parsedArgs.configLocation.getOrElse("")
    )

    val configFromLocationPath =
      ConfigFactory.parseString(
        s"config=${parsedArgs.configLocation.getOrElse(throw ConfigException("Cannot get config location from configuration!"))}"
      )

    // note: this overrides the default config values provided in the config file!
    // THE ORDER OF THE CALLS MATTERS -> the later the call, the more "fallback" -> first config is always the primary one!
    // hence if you add some more program arguments, you have to add them before(!) your default config!
    // see https://github.com/lightbend/config#merging-config-trees for details on merging configs
    val config = parsedArgsConfig
      .withFallback(configFromLocationPath)
      .resolve()

    (parsedArgs, config)
  }

}
