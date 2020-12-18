package com.dean.ojekonlinefirebase.ui.request.detail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dean.ojekonlinefirebase.MainActivity
import com.dean.ojekonlinefirebase.R
import com.dean.ojekonlinefirebase.model.Booking
import com.dean.ojekonlinefirebase.utils.Constan
import com.dean.ojekonlinefirebase.utils.Constan.key
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_detail_request.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class DetailRequestActivity : AppCompatActivity(), OnMapReadyCallback  {

    var status: Int? = null
    var booking: Booking? = null

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_request)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.fm_maps_detail) as SupportMapFragment
        mapFragment.getMapAsync(this)

        var auth = FirebaseAuth.getInstance()

        //menangkap objej yang di kirim dari masing2 objek
        booking = Booking()
        booking = intent.getSerializableExtra(Constan.booking) as Booking
        status = intent.getIntExtra(Constan.status,0)

        //memindahkan objek ke view
        tv_detail_awal.text = booking?.lokasiAwal
        tv_detail_tujuan.text = booking?.lokasiTujuan
        tv_detail_tanggal.text = booking?.tanggal
        tv_detail_price.text = booking?.harga

        detailBooking()

        btn_detail.onClick {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference(Constan.tb_booking)

            if (status == 1){
                myRef.child(key).child("status").setValue(2)
                myRef.child(key).child("driver").setValue(auth.currentUser?.uid)

                startActivity<MainActivity>()
            }
            else if (status == 2){
                myRef.child(key).child("status").setValue(4)
                startActivity<MainActivity>()
            }
        }
    }

    private fun detailBooking() {
        if (status == 2){
            btn_detail.text = getString(R.string.complete)
        }
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(Constan.tb_booking)
        var key = ""
        val query = myRef.orderByChild("tanggal").equalTo(booking?.tanggal)

        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                //untuk mengambil key masing2 item
                for (issue in snapshot.children){
                    key = issue.key.toString()
                }
            }

        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latlng = booking?.latAwal?.let {
            booking?.lonAwal?.let {
                it1 -> LatLng(it, it1) } }

        val latlng1 = booking?.latTujuan?.let {
            booking?.lonTujuan?.let {
                it1 -> LatLng(it, it1) } }

        val res = this.resources
        val marker1 = BitmapFactory.decodeResource(res, R.mipmap.ic_pin)
        val smallmarker = Bitmap.createScaledBitmap(marker1,80,120, false)

        mMap.addMarker(latlng?.let {
            MarkerOptions().position(it).title("awal").icon(BitmapDescriptorFactory.fromBitmap(smallmarker))
        })

        mMap.addMarker(latlng1?.let {
            MarkerOptions().position(it).title("tujuan").icon(BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_RED))
        })

        val builder = LatLngBounds.builder()
        builder.include(latlng)
        builder.include(latlng1)

        mMap.setOnCameraIdleListener {
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),32)

            )
        }
    }
}