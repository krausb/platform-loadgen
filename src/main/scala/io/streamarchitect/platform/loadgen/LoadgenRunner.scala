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
import io.gatling.core.scenario.Simulation
import io.gatling.core.session._
import com.github.jeanadrien.gatling.mqtt.Predef._

import scala.concurrent.duration._
import scala.util.Random
import java.util.UUID.randomUUID

/**
  * MQTT Load Generator
  */
class LoadgenRunner extends Simulation {

  import LoadgenRunner.{ createDevices, createMqttMessage, createSessions }

  private val sessions = createSessions()
  private val devices  = createDevices()
  private val randGen  = new Random(1L)

  private val mqttMessageGenerator = forever {
    pace(500.milliseconds, 2000.milliseconds)
      .exec(session => {
        val deviceId    = devices(randGen.nextInt(devices.size))
        val sessionId   = sessions(randGen.nextInt(sessions.size))
        val mqttMessage = createMqttMessage(deviceId, sessionId)

        session.setAll(("mqttMessage", mqttMessage))
      })
      .exec(
        publish(
          "dev/test",
          payload = session => session("mqttMessage").validate[Array[Byte]].get
        )
      )
  }

  private val loadgenScenario = scenario("MQTT Load Generator Scenario")
    .exec(connect)
    .exec(mqttMessageGenerator)

  setUp(
    loadgenScenario.inject(
      rampUsers(10) over 10.minutes,
      rampUsers(10) over 10.minutes
    )
  ).protocols(MqttBrokerConnectionFactory.createMqttProtocol())
    .maxDuration(10.minutes)

}

object LoadgenRunner {

  val numDevices  = 5
  val numSessions = 10

  /**
    * Create a list of UUIDs
    *
    * @param size
    * @return
    */
  def randomUuids(size: Int): List[String] =
    List.fill(size) { randomUUID().toString }

  /**
    * Create a list of devices
    *
    * @return
    */
  def createDevices(): List[String] = randomUuids(numDevices)

  /**
    * Create a list of sessions
    * @return
    */
  def createSessions(): List[String] = randomUuids(numSessions)

  def createMqttMessage(deviceId: String, sessionId: String): Array[Byte] =
    s"{'deviceId':'${deviceId}','sessionId':'${sessionId}'}".getBytes

}
