package com.dean.ojekonlinefirebase.ui.request.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dean.ojekonlinefirebase.R
import com.dean.ojekonlinefirebase.model.Booking
import com.dean.ojekonlinefirebase.ui.request.fragment.RequestBookingFragment
import kotlinx.android.synthetic.main.booking_item.view.*

class BookingAdapter(
    private val mValues: ArrayList<Booking>,
    private val mListener: RequestBookingFragment.OnlistFragmentInteractionListener?
) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    init {
        mOnClickListener = View.OnClickListener {
            v ->
            val item = v.tag as Booking
            mListener?.onlistFragmentInteraction(item)
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        var mAwal: TextView = mView.tv_item_awal
        var mTujuan: TextView = mView.tv_item_tujuan
        var mTanggal: TextView = mView.tv_item_tanggal


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.booking_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mValues.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mAwal.text = item.lokasiAwal
        holder.mTujuan.text = item.lokasiAwal
        holder.mTanggal.text = item.tanggal

        with(holder.mView){
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

}