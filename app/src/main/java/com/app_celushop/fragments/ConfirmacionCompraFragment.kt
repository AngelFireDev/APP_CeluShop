package com.app_celushop.fragments

import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app_celushop.R
import com.app_celushop.database.CarritoDAO
import com.app_celushop.database.UsuariosDAO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.NumberFormat
import java.util.Locale
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class ConfirmacionCompraFragment: Fragment() {

    private lateinit var carritoDAO: CarritoDAO
    private lateinit var usuariosDAO: UsuariosDAO
    private lateinit var precio_subtotal: TextView
    private lateinit var precio_envio: TextView
    private lateinit var precio_total: TextView
    private lateinit var nombre: TextView
    private lateinit var btn_pagar: Button
    private lateinit var btn_cancelar: Button

    private lateinit var btn_PSE: RadioButton

    private val COSTO_ENVIO = 30000.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_confirmacion_compra, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        carritoDAO = CarritoDAO(requireContext())
        usuariosDAO = UsuariosDAO(requireContext())

        precio_subtotal = view.findViewById(R.id.price_product)
        precio_envio = view.findViewById(R.id.delivery)
        precio_total = view.findViewById(R.id.total)
        nombre = view.findViewById(R.id.name)
        btn_pagar = view.findViewById(R.id.btn_pay)
        btn_cancelar = view.findViewById(R.id.btn_cancelar)
        btn_PSE = view.findViewById(R.id.radioButtonPSE)

        mostrarResumenDePago()

        val prefs = requireActivity().getSharedPreferences("sesion_usuario", MODE_PRIVATE)

        val tipoLogin = prefs.getString("tipo_login", null)
        val correoUsuario = prefs.getString("correo_usuario", null)

        if(tipoLogin == "google") {
            // Usuario Firebase
            val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

            if(currentUser != null) {
                //Usuario Google
                nombre.text = currentUser.displayName ?: "Usuario"

            }
        } else if (tipoLogin == "bd") {
            if (correoUsuario != null) {
                val usuario = usuariosDAO.obtenerUsuario(correoUsuario)
                if (usuario != null) {
                    nombre.text = usuario.nombre
                }
            }
        }

        btn_pagar.setOnClickListener {
            iniciarProcesoDePago()
        }

        btn_cancelar.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun mostrarResumenDePago() {
        val subtotalProductos = carritoDAO.calcularTotalProductos()
        val totalFinal = subtotalProductos + COSTO_ENVIO

        // Actualiza las vistas
        precio_subtotal.text = formatCurrency(subtotalProductos)
        // Si tiene un TextView para Envío, asegúrese de que muestre el valor
        precio_total.text = formatCurrency(totalFinal)
    }

    private fun formatCurrency(amount: Double): String {
        val formatoMoneda = NumberFormat.getCurrencyInstance(Locale("es", "CO")) // O su Locale
        formatoMoneda.maximumFractionDigits = 0
        return formatoMoneda.format(amount)
    }

    private fun iniciarProcesoDePago() {
        if (btn_PSE.isChecked) {
            mostrarModalYRedireccionar()
        } else {
            Toast.makeText(
                requireContext(),
                "Por favor, seleccione un método de pago para continuar.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun mostrarModalYRedireccionar() {
        val dialogo = AlertDialog.Builder(requireContext())
            .setTitle("Procesando Pago")
            .setMessage("Estamos redirigiendo a la plataforma de pago externa (PSE). Espere un momento...")
            .setCancelable(false) // Hace que el modal no se cierre al tocar fuera
            .create()

        dialogo.show()

        // Usar Corrutinas para el retraso
        viewLifecycleOwner.lifecycleScope.launch {
            delay(5000) // 5000 milisegundos = 5 segundos

            // 1. Ocultar el diálogo
            if (dialogo.isShowing) {
                dialogo.dismiss()
            }

            // 2. Redirigir
            val urlExterna = "https://www.pse.com.co/persona"
            openExternalPage(urlExterna)
        }
    }

    private fun openExternalPage(url: String) {
        try {
            // Crea un Intent para abrir un navegador con la URL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error en el pago", Toast.LENGTH_SHORT).show()
        }
    }
}