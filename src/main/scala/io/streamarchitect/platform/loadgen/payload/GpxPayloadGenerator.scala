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

import com.typesafe.scalalogging.Logger
import io.streamarchitect.codec.gpx.GpxCodec
import io.streamarchitect.platform.domain.codec.DomainCodec
import io.streamarchitect.platform.domain.telemetry.{Meta, Position, PositionedTelemetry}
import io.streamarchitect.platform.model.WptType

/**
  * Gpx Payload Generator
  */
class GpxPayloadGenerator extends PayloadGenerator {

  private val log = Logger(getClass)

  var gpxTrkItems: Seq[WptType] = Seq.empty

  var counter = 0

  override def init(payload: String): PayloadGenerator =
    try {
      log.debug(s"Initializing GpxPayloadGenerator with file: ${payload}")
      val gpx = GpxCodec.decode(payload)
      gpxTrkItems = gpx.trk(0).trkseg(0).trkpt
      this.asInstanceOf[PayloadGenerator]
    } catch {
      case t: Throwable =>
        throw t
    }

  override def generatePayload(deviceId: String, sessionId: String): Array[Byte] = {
    val trkItem = gpxTrkItems(getCurrentCounter())
    val payload = PositionedTelemetry(
      Meta(deviceId, sessionId, System.currentTimeMillis(), 0L),
      "gpx_trk_item",
      Position(trkItem.lat.toDouble,
               trkItem.lon.toDouble,
               trkItem.ele.getOrElse(BigDecimal(0)).toDouble,
               0),
      Some(0.0),
      None
    )

    log.debug(s"generatePayload(${payload} ...")

    DomainCodec.encode(payload)
  }

  override def getDemoPayloadFilePath(): String = "/demo.gpx"

  private def getCurrentCounter(): Integer = {
    counter = (counter + 1) % gpxTrkItems.length
    counter
  }

}
