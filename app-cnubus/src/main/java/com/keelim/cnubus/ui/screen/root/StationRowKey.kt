package com.keelim.cnubus.ui.screen.root

import com.keelim.core.data.model.Location

/**
 * Identity of a catalog row, not the persisted favorite name.
 *
 * Names and even coordinates repeat in the catalog. The complete immutable
 * coordinate/route tuple distinguishes those rows without depending on list order,
 * filters, favorite state, mutable display names or image URLs. Favorites remain
 * name-based groups for compatibility with station_preferences.
 */
internal val Location.stationRowKey: String
    get() = "station:${latLng.latitude.toBits()}:${latLng.longitude.toBits()}:$roota:$rootb:$rootc:$root_night"
