package UDEMX.icecream

import IceCreamAdapter
import UDEMX.icecream.Models.Extras
import UDEMX.icecream.Models.IceCream
import UDEMX.icecream.Models.IceCreams
import UDEMX.icecream.Models.RetrofitInstance
import UDEMX.icecream.Models.Status
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var apiInterface: ApiInterface
    private lateinit var iceCreamAdapter: IceCreamAdapter
    private lateinit var recyclerView: RecyclerView
    private var iceCreamList = mutableListOf<IceCream>()
    private var basePrice = 0.0
    private lateinit var spinnerStatus: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        iceCreamAdapter = IceCreamAdapter(emptyList(), basePrice) { iceCream ->
            addToCart(iceCream)
        }
        recyclerView.adapter = iceCreamAdapter

        //status spinner setup
        spinnerStatus = findViewById(R.id.spinnerStatus)
        val adapter = object : ArrayAdapter<Status>(
            this,
            android.R.layout.simple_spinner_item,
            Status.entries.toTypedArray()
        ) {
            //set the text sizes to 18f
            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.textSize = 18f
                return view
            }

            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                textView.textSize = 18f
                return view
            }
        }
        spinnerStatus.adapter = adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerStatus.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                if (iceCreamList.isNotEmpty()) {
                    val selectedStatus = Status.entries[position]
                    sortByStatus(selectedStatus)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        })

        getApiInterface()
        getIceCreamData()
        getExtrasData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sortByStatus(status: Status)
    {
        iceCreamList.sortWith(compareByDescending<IceCream> { it.status == status }.thenBy { it.name })
        iceCreamAdapter = IceCreamAdapter(iceCreamList, basePrice) { iceCream ->
            addToCart(iceCream)
        }
        recyclerView.adapter = iceCreamAdapter
        iceCreamAdapter.notifyDataSetChanged()
    }

    private fun getApiInterface() {
        apiInterface = RetrofitInstance.getInstance().create(ApiInterface::class.java)
    }

    private fun getIceCreamData()
    {
        val call = apiInterface.getIceCreams()
        call.enqueue(object : Callback<IceCreams>
        {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<IceCreams>, response: Response<IceCreams>)
            {
                if (response.isSuccessful && response.body() != null)
                {
                    val iceCreams = response.body()
                    println("Ice Creams: $iceCreams")
                    if (iceCreams != null)
                    {
                        basePrice = iceCreams.basePrice
                        iceCreamList.addAll(iceCreams.iceCreams)
                    }
                    iceCreamAdapter = IceCreamAdapter(iceCreamList, basePrice) { iceCream ->
                        addToCart(iceCream)
                    }
                    recyclerView.adapter = iceCreamAdapter
                    iceCreamAdapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<IceCreams>, t: Throwable)
            {
                t.printStackTrace()
            }


        })
    }

    private fun getExtrasData() {
        val call = apiInterface.getExtras()
        call.enqueue(object : Callback<List<Extras>> {
            override fun onResponse(call: Call<List<Extras>>, response: Response<List<Extras>>) {
                if (response.isSuccessful && response.body() != null) {
                    val extras = response.body()
                    println("Extras: $extras")
                }
            }

            override fun onFailure(call: Call<List<Extras>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun addToCart(iceCream: IceCream)
    {
        if (iceCream.status == Status.available)
            Toast.makeText(this, "${iceCream.name} added to cart!", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "This ice cream is not available now!", Toast.LENGTH_SHORT).show()
    }
}
