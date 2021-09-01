package ui;

import android.media.MediaDrm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import tech.gusavila92.apache.http.HttpStatus;

public interface RestAPI {

    @GET("/testingConnection")
    public Call<HttpStatus> testingConnection();

    @Streaming
    @GET("api/get/Ip")
    public Call<List<String>> getAllIp();




}
