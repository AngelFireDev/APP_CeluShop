package com.app_celushop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app_celushop.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapsFragment: Fragment() {

    private lateinit var map1: GoogleMap
    private lateinit var map2: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Usa el XML de tu activity_perfil (asumiendo que lo renombraste a fragment_perfil.xml)
        return inflater.inflate(R.layout.fragment_google_maps, container, false) // ⬅️ Usa el nombre de tu XML de perfil
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createFragment()
    }

    private fun createFragment() {
        val mapFragment1 = childFragmentManager.findFragmentById(R.id.map1) as SupportMapFragment
        val mapFragment2 = childFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment

        mapFragment1.getMapAsync { googleMapPrimerUbicacion ->
            map1 = googleMapPrimerUbicacion
            createMarker1()
        }

        mapFragment2.getMapAsync { googleMapSegundaUbicacion ->
            map2 = googleMapSegundaUbicacion
            createMarker2()
        }
    }

    private fun createMarker1() {
        val cordenadas = LatLng(4.675897799109642, -74.08785970264074)
        val marker = MarkerOptions().position(cordenadas).title("Nuestra tienda principal")
        map1.addMarker(marker)
        map1.animateCamera(CameraUpdateFactory.newLatLngZoom(cordenadas, 18f),
            2000,
            null)
    }

    private fun createMarker2() {
        val cordenadas = LatLng(4.621218933505406, -74.07306297337038)
        val marker = MarkerOptions().position(cordenadas).title("Tienda recien inaugurada")
        map2.addMarker(marker)
        map2.animateCamera(CameraUpdateFactory.newLatLngZoom(cordenadas, 18f),
            2000,
            null)
    }
}