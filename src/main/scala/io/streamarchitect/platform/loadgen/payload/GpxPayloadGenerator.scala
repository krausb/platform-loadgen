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

import io.streamarchitect.codec.gpx.GpxCodec
import io.streamarchitect.platform.domain.codec.DomainCodec
import io.streamarchitect.platform.domain.telemetry.{ Meta, Position, PositionedTelemetry }
import io.streamarchitect.platform.model.WptType

import scala.reflect.io.File

/**
  * Gpx Payload Generator
  */
class GpxPayloadGenerator extends PayloadGenerator {

  var gpxTrkItems: Seq[WptType] = Seq.empty

  var counter = 0

  override def init(payloadFile: File): Unit = {
    val gpx = GpxCodec.decode(payloadFile.lines().mkString)
    gpxTrkItems = gpx.trk(0).trkseg(0).trkpt
  }

  /**
    * Generate payload based on an internal [[Iterator]] which emits
    *
    * @param deviceId
    * @param sessionId
    * @return
    */
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

    DomainCodec.encode(payload)
  }

  private def getCurrentCounter(): Integer = {
    counter = (counter + 1) % gpxTrkItems.length
    counter
  }

}

object GpxPayloadGenerator {

  def apply(file: File): GpxPayloadGenerator = {
    val pg = new GpxPayloadGenerator
    pg.init(file)
    pg
  }

}
