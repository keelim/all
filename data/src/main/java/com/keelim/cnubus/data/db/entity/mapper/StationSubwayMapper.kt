package com.keelim.cnubus.data.db.entity.mapper

import com.keelim.cnubus.data.model.Subway
import com.keelim.cnubus.data.model.Station
import com.keelim.cnubus.data.db.entity.StationEntity
import com.keelim.cnubus.data.db.entity.StationWithSubwaysEntity
import com.keelim.cnubus.data.db.entity.SubwayEntity

fun StationWithSubwaysEntity.toStation() =
    Station(
        name = station.stationName,
        isFavorited = station.isFavorited,
        connectedSubways = subways.toSubways()
    )

fun Station.toStationEntity() =
    StationEntity(
        stationName = name,
        isFavorited = isFavorited,
    )


fun List<StationWithSubwaysEntity>.toStations() = map { it.toStation() }

fun List<SubwayEntity>.toSubways(): List<Subway> = map { Subway.findById(it.subwayId) }
