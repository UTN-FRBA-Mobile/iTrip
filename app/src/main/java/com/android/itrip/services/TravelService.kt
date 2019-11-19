package com.android.itrip.services

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.itrip.apiModels.CiudadAVisitarApiModel
import com.android.itrip.apiModels.Continente
import com.android.itrip.apiModels.ViajeApiModel
import com.android.itrip.models.*
import com.android.itrip.util.ApiError
import com.android.itrip.util.ViajeData
import com.android.itrip.util.VolleyClient
import com.android.itrip.util.calendarToString
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import javax.inject.Inject


class TravelService @Inject constructor(queue: VolleyClient) : ApiService(queue) {

    fun getDestinations(
        responseHandler: (LiveData<List<Ciudad>>) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        getArray("destinos/", {
            val listType = object : TypeToken<List<Continente>>() {}.type
            val continentes: List<Continente> = gson.fromJson(it.toString(), listType)
            val ciudades = continentes.flatMap { continente ->
                continente.paises.flatMap { pais ->
                    pais.ciudades
                }
            }
            responseHandler(MutableLiveData(ciudades))
        }, errorHandler)
    }

    fun getTravels(
        responseHandler: (List<Viaje>) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        getArray("viajes/", {
            val viajesApiModel: List<ViajeApiModel> =
                gson.fromJson(it.toString(), object : TypeToken<List<ViajeApiModel>>() {}.type)
            responseHandler(viajesApiModel.map { it.viaje() })
        }, errorHandler)
    }

    fun getTrip(
        id: Long,
        responseHandler: (Viaje) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("getTrip.")
        get("viajes/$id", {
            val viajeApiModel: ViajeApiModel = gson.fromJson(it.toString(), ViajeApiModel::class.java)
            val viaje: Viaje = viajeApiModel.viaje()
            responseHandler(viaje)
        }, errorHandler)
    }

    fun createTrip(
        body: ViajeData,
        responseHandler: (Viaje) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        val json = JSONObject(gson.toJson(body))
        post("viajes/", json, {
            val viajeApiModel: ViajeApiModel = gson.fromJson(it.toString(), ViajeApiModel::class.java)
            responseHandler(viajeApiModel.viaje())
        }, errorHandler)
    }

    fun deleteTrip(
        viajeParam: Viaje,
        responseHandler: () -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("deleteTrip.")
        delete("""viajes/${viajeParam.id}/""", {
            responseHandler()
        }, errorHandler)
    }

    fun getActivities(
        ciudad: Ciudad,
        responseHandler: (List<Actividad>) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("getActivities.")
        getArray("""destinos/${ciudad.id}/actividades/""", {
            val listType = object : TypeToken<List<Actividad>>() {}.type
            val actividades: List<Actividad> = gson.fromJson(it.toString(), listType)
            responseHandler(actividades)
        }, errorHandler)
    }

    fun getActivities2(
        ciudad: Ciudad,
        responseHandler: (LiveData<List<Actividad>>) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("getActivities.")
        getArray("""destinos/${ciudad.id}/actividades/""", {
            val listType = object : TypeToken<List<Actividad>>() {}.type
            val actividades: List<Actividad> = gson.fromJson(it.toString(), listType)
            responseHandler(MutableLiveData(actividades))
        }, errorHandler)
    }

    fun postDestination(
        viaje: Viaje,
        destination: CiudadAVisitar,
        responseHandler: (CiudadAVisitar) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        val json = JSONObject().apply {
            put("ciudad", destination.detalle_ciudad?.id)
            put("inicio", calendarToString(destination.inicio, "yyyy-MM-dd"))
            put("fin", calendarToString(destination.fin, "yyyy-MM-dd"))
        }
        post("viajes/${viaje.id}/add_destination/", json, {
            val data = gson.fromJson(it.toString(), CiudadAVisitarApiModel::class.java)
            responseHandler(data.ciudadAVisitar())
        }, errorHandler, initialTimeoutMs = 25000, retries = 0)
    }

    fun get_CityToVisit(
        ciudad_a_visitarParam: CiudadAVisitar,
        responseHandler: (CiudadAVisitar) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("get_CityToVisit.")
        get("""ciudad-a-visitar/${ciudad_a_visitarParam.id}/""", {
            val ciudadAVisitarApiModel: CiudadAVisitarApiModel =
                gson.fromJson(it.toString(), CiudadAVisitarApiModel::class.java)
            val ciudad_a_visitar: CiudadAVisitar = ciudadAVisitarApiModel.ciudadAVisitar()
            responseHandler(ciudad_a_visitar)
        }, errorHandler)
    }

    fun deleteDestination(
        ciudad_a_visitarParam: CiudadAVisitar,
        responseHandler: () -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("deleteDestination.")
        delete("""ciudad-a-visitar/${ciudad_a_visitarParam.id}/""", {
            responseHandler()
        }, errorHandler)
    }

    fun deleteToDoActivity(
        actividadARealizar: ActividadARealizar,
        responseHandler: () -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("deleteDestination.")
        delete("""actividad-a-realizar/${actividadARealizar.id}/""", {
            responseHandler()
        }, errorHandler)
    }

    fun getActivitiesForBucket(
        bucket: Bucket,
        responseHandler: (List<Actividad>) -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("getActivitiesForBucket.")
        val url =
            """ciudad-a-visitar/${bucket.ciudadAVisitar.id}/posibles-actividades/?dia=${bucket.dia}&bucket_inicio=${bucket.bucket_inicio}"""
        getArray(
            url,
            {
                val listType = object : TypeToken<List<Actividad>>() {}.type
                val actividades: List<Actividad> = gson.fromJson(it.toString(), listType)
                responseHandler(actividades)
            },
            errorHandler
        )
    }

    fun addActivityToBucket(
        bucket: Bucket,
        responseHandler: () -> Unit,
        errorHandler: (ApiError) -> Unit
    ) {
        logger.info("addActivityToBucket.")
        val url =
            """ciudad-a-visitar/${bucket.ciudadAVisitar.id}/agregar-actividad/"""
        val json = JSONObject().apply {
            put("actividad", bucket.actividad.id)
            put("dia", bucket.dia)
            put("bucket_inicio", bucket.bucket_inicio)
        }
        post(url, json, {
            responseHandler()
        }, errorHandler)
    }

}