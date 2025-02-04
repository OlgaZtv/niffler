package niffler.api;

import niffler.model.UserJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NifflerUserdataApi {

    String nifflerUserdataUri = "http://127.0.0.1:8089";

    @POST("/updateUserInfo")
    Call<UserJson> updateUserInfo(@Body UserJson user);

    @GET("/currentUser")
    Call<UserJson> currentUser(@Query("username") String username);
}
