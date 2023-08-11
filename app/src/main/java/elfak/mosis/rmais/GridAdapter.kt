package elfak.mosis.rmais

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class GridAdapter(
    applicationContext: Context?,
    private val items: List<Array<String>>
) : BaseAdapter() {

    var inflater: LayoutInflater = LayoutInflater.from(applicationContext)

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(i: Int): Any {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View? {
        val myView = inflater.inflate(R.layout.grid_item, null)

        val leftText = myView.findViewById<TextView>(R.id.left_text)
        leftText.text = this.items[i][0]

        val rightText = myView.findViewById<TextView>(R.id.right_text)
        rightText.text = this.items[i][1]

        return myView
    }
}