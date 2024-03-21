package com.starostin.car_brands_new

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class CustomSpinnerAdapter(private val context: Context, private val itemList: MutableList<Vehicle>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if (view == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.spinner_item_with_delete_icon, parent, false)
        }

        val currentItem = itemList[position]
        val itemName = view!!.findViewById<TextView>(R.id.tv_item_name)
        itemName.text = currentItem.name

        val deleteIcon = view.findViewById<ImageView>(R.id.iv_delete)
        deleteIcon.setOnClickListener {
            showDeleteDialog(position)
        }

        return view
    }

    private fun showDeleteDialog(position: Int) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Удалить элемент?")
            .setMessage("Вы уверены, что хотите удалить этот элемент?")
            .setPositiveButton("Удалить") { _, _ ->
                itemList.removeAt(position)
                notifyDataSetChanged()
                Toast.makeText(context, "Элемент удален", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss()
            }
            .create()
            .show()
    }
}

