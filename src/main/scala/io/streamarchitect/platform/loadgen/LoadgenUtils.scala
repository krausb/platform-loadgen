/*
 * Copyright (C) 2018  Bastian Kraus
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.streamarchitect.platform.loadgen

import io.gatling.core.Predef._
import com.github.jeanadrien.gatling.mqtt.Predef._
import com.github.jeanadrien.gatling.mqtt.protocol.MqttProtocolBuilder
import com.typesafe.config.{ Config, ConfigFactory }

/**
  * Configuration singleton
  */
object LoadgenConfig {
  val config: Config = ConfigFactory.load()
}

/**
  * Factory to generate the MQTT  Broker protocol
  */
object MqttBrokerConnectionFactory {

  val mqttHost: String = LoadgenConfig.config.getString("mqtt.host")
  val mqttPort: String = LoadgenConfig.config.getString("mqtt.port")

  def createMqttProtocol(): MqttProtocolBuilder = {
    val host               = mqtt.host(s"${mqttHost}:${mqttPort}")
    val usernameConfigPath = "mqtt.username"
    val passwordConfigPath = "mqtt.password"
    if (LoadgenConfig.config.hasPath(usernameConfigPath) && LoadgenConfig.config.hasPath(
          passwordConfigPath
        )) {
      val username = LoadgenConfig.config.getString(usernameConfigPath)
      val password = LoadgenConfig.config.getString(passwordConfigPath)
      host.userName(username).password(password)
    } else {
      host
    }
  }
}
