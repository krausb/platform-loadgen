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

import scala.reflect.io.File

/**
  * Trait for implementing a payload generator
  */
trait PayloadGenerator {

  /**
    * Payload generator initialization with payload from a given file
    *
    * @param payloadFile
    */
  def init(payloadFile: File): Unit = ???

  /**
    * Generate Payload for data ingest
    *
    * @param deviceId
    * @param sessionId
    * @return
    */
  def generatePayload(deviceId: String, sessionId: String): Array[Byte] = ???

}
