package ru.niffer_android.adapter.currency

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.niffer_android.R
import ru.niffer_android.model.CurrencyDropdownItem

class CurrencyDropdownAdapter(context: Context, private val items: List<CurrencyDropdownItem>) : ArrayAdapter<CurrencyDropdownItem>(context, R.layout.item_currency_dropdown, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_currency_dropdown, parent, false)
        val item = getItem(position)

        val icon = view.findViewById<TextView>(R.id.tvCurrencyIcon)
        val text = view.findViewById<TextView>(R.id.currencyText)

        item?.let {
            icon.text = it.icon
            text.text = it.text
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}