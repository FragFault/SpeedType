import android.content.Intent
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.simpletodo.PlayActivity
import com.example.simpletodo.R
import com.example.simpletodo.fragments.SelectionFragment
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

//Similar to a recycler view a carousel adapter is created

class CarouselRVAdapter(private val carouselDataList: Map<String, Drawable>, private val context: Context) : //The carousel data list must be a map with a key, value pair of String Drawable
    RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.item_carousel, parent, false)
        return CarouselItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        val textView = holder.itemView.findViewById<TextView>(R.id.textview)
        textView?.background = carouselDataList.values.elementAt(position) //The textView that is being bound contains a background xml element
        textView?.text = carouselDataList.keys.elementAt(position)


    }

    override fun getItemCount(): Int {
        return carouselDataList.size
    }

    inner class CarouselItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener { //Made the item View holder as an inner class to listen for clicks on each recyclerview item
        private val LYRICS_URL = "http://52.87.203.225/api/getLyricsPrompt"
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val category = carouselDataList.keys.elementAt(adapterPosition)
            Log.i(TAG, "$category")

            val promptData = dataRetrieve(LYRICS_URL, category)


        }
    }

    fun dataRetrieve(CATEGORY_URL: String, selection: String) {
        val client = OkHttpClient.Builder().connectTimeout(100,TimeUnit.SECONDS).writeTimeout(100,TimeUnit.SECONDS).readTimeout(300,TimeUnit.SECONDS).build()
        var INDEX = 0

        if(selection == "Poetry"){
            INDEX = 0
        }else if(selection == "Movies"){
            INDEX = 1
        }else{
            INDEX = 2
        }
        Log.e("dataRetrieve",CATEGORY_URL)
        val request = okhttp3.Request.Builder().url(CATEGORY_URL).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("dataRetrieve:","FAIL")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if(body!=null){
                    val jObject = JSONObject(body)
                    val intent = Intent(context,PlayActivity::class.java)
                    intent.putExtra("prompt",jObject.get("prompt").toString())
                }
            }

        })

    }

    companion object {
        private const val TAG = "ADAPTER"
    }




}
