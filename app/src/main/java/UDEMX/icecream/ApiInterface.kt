package UDEMX.icecream

import UDEMX.icecream.Models.Extras
import UDEMX.icecream.Models.IceCreams
import retrofit2.http.GET
import retrofit2.Call

interface ApiInterface
{
    @GET("icecreams.json")
    fun getIceCreams(): Call<IceCreams>
    @GET("extras.json")
    fun getExtras(): Call <List<Extras>>
}