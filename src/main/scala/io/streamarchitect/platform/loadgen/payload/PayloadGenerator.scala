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

package io.streamarchitect.platform.loadgen.payload

import java.io.FileInputStream

import io.streamarchitect.platform.loadgen.LoadgenConfig

import scala.io.Source

/**
  * Trait for implementing a payload generator
  */
trait PayloadGenerator {

  /**
    * Payload generator initialization with payload from a given file
    *
    * @param payload
    */
  def init(payload: String): PayloadGenerator = ???

  /**
    * Generate Payload for data ingest
    *
    * @param deviceId
    * @param sessionId
    * @return
    */
  def generatePayload(deviceId: String, sessionId: String): Array[Byte] = ???

  /**
    * Get the filepath to a demo payload file
    *
    * @return
    */
  def getDemoPayloadFilePath(): String = ???

}

/**
  * Factory for creating a payload generator
  */
object PayloadGenerator {

  /**
    * Create a [[PayloadGenerator]]
    *
    * @param pkg
    * @param clazz
    * @return
    */
  def apply(pkg: String, clazz: String): PayloadGenerator = {
    val payloadGen = Class.forName(s"${pkg}.${clazz}").newInstance().asInstanceOf[PayloadGenerator]

    val payload = Source.fromInputStream(
      LoadgenConfig.config.getBoolean("payloadGen.useDemoPayload") match {
        case true =>
          getClass.getResourceAsStream(payloadGen.getDemoPayloadFilePath())
        case _ =>
          Source.fromInputStream(new FileInputStream(getPayloadFile())).asInstanceOf[FileInputStream]
      }
    ).getLines().mkString
    payloadGen.init(payload)
  }

  private def getPayloadFile(): String =
    LoadgenConfig.config.getString("payloadGen.payload")

}
