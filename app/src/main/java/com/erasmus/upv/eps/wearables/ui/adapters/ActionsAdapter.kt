package com.erasmus.upv.eps.wearables.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.erasmus.upv.eps.wearables.R
import com.erasmus.upv.eps.wearables.model.actions.Actions
import com.erasmus.upv.eps.wearables.model.actions.BasketballActions
import com.erasmus.upv.eps.wearables.model.actions.FootballActions
import com.erasmus.upv.eps.wearables.model.actions.HandballActions
import timber.log.Timber
import java.util.*

class ActionsAdapter(private val mContext: Context, private val resource: Int, private val actionTexts: Array<String>,
                     private val objects: Array<Actions>
    ) :
    ArrayAdapter<String>(mContext, resource, actionTexts) {



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if(view == null) {
             view = LayoutInflater.from(mContext).inflate(resource, parent, false)

        }
        val itemText = view?.findViewById<TextView>(R.id.dropdown_item_tv)

        val item = getItem(position)
        Timber.d("item actions $item")
        itemText?.text = item

        itemText?.setBackgroundColor(Color.RED)

        return view!!
    }



    fun getItemAsAction(position: Int): Actions {
        return objects[position]
    }

}