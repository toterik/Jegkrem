package UDEMX.icecream

import UDEMX.icecream.Models.IceCreams
import UDEMX.icecream.Models.Extras
import UDEMX.icecream.Models.RetrofitInstance
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var apiInterface: ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getApiInterface()
        getExampleData()
        getExtrasData()
    }

    private fun getApiInterface() {
        apiInterface = RetrofitInstance.getInstance().create(ApiInterface::class.java)
    }

    private fun getExampleData() {
        val call = apiInterface.getIceCreams()
        call.enqueue(object : Callback<IceCreams> {
            override fun onResponse(call: Call<IceCreams>, response: Response<IceCreams>) {
                if (response.isSuccessful && response.body() != null) {
                    val iceCreams = response.body()
                    println("✅ Ice Creams: $iceCreams")
                }
            }

            override fun onFailure(call: Call<IceCreams>, t: Throwable) {
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
                    println("✅ Extras: $extras")
                }
            }

            override fun onFailure(call: Call<List<Extras>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}
