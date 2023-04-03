package solutus.coronawatch.data.reposetory.implementation

import solutus.coronawatch.data.entity.InternationalZone
import solutus.coronawatch.data.entity.NationalZone
import solutus.coronawatch.data.network.implementation.MapApi
import solutus.coronawatch.data.reposetory.abstraction.SafeApiRequest

class MapRepository (private val mapApi: MapApi
): SafeApiRequest() {

    suspend fun getNationalZones () : ArrayList<NationalZone>? {
        return mapApi.getNationalZones().body()?.results
    }
    suspend fun getInterNationalZones () : ArrayList<InternationalZone>? {
        return mapApi.getInterNationalZones().body()?.results
    }
}