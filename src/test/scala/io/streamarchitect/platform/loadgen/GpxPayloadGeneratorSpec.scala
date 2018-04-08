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

import io.streamarchitect.platform.domain.codec.DomainCodec
import io.streamarchitect.platform.domain.telemetry.PositionedTelemetry
import io.streamarchitect.platform.loadgen.payload.{ GpxPayloadGenerator, PayloadGenerator }
import org.apache.logging.log4j.LogManager
import org.scalatest.{ MustMatchers, WordSpecLike }

import scala.io.Source
import scala.reflect.io.{ File, Path }

/**
  * Test Spec for a Payload Generator
  */
class GpxPayloadGeneratorSpec extends WordSpecLike with MustMatchers {

  private val log = LogManager.getLogger(this.getClass)

  "A GpxPayloadGenerator should" should {

    "successful cycle through a set of track points" in {
      val gpxPayloadGenerator: PayloadGenerator = getPayloadGenerator

      for (c <- 0 to 100) {
        log.debug(gpxPayloadGenerator.generatePayload("123", "456"))
      }

    }

    "successful creates payload which can be deserialized" in {
      val gpxPayloadGenerator: PayloadGenerator = getPayloadGenerator

      val payload = gpxPayloadGenerator.generatePayload("123", "456")

      val decodecEntity = DomainCodec.decode(payload, PositionedTelemetry.SCHEMA$)
      log.debug(s"Decoded entity: ${decodecEntity}")
    }

  }

  private def getPayloadGenerator(): PayloadGenerator =
    GpxPayloadGenerator(File(getClass.getResource("/test_trace.gpx").getFile))

}
