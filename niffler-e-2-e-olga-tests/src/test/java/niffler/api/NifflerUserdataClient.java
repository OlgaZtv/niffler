package niffler.api;

import niffler.model.UserJson;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class NifflerUserdataClient {
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NifflerUserdataApi.nifflerUserdataUri)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private NifflerUserdataApi nifflerUserdataApi = retrofit.create(NifflerUserdataApi.class);

    public UserJson updateUserInfo(UserJson user) throws Exception {
        return nifflerUserdataApi.updateUserInfo(user).execute().body();
    }

    public UserJson getCurrentUser(String username) throws Exception {
        return nifflerUserdataApi.currentUser(username)
                .execute()
                .body();
    }

}
