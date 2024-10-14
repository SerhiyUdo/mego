package api.services;

import com.epam.http.annotations.GET;
import com.epam.http.annotations.ServiceDomain;
import com.epam.http.requests.RestMethod;


@ServiceDomain("${serverUrl}/channel")
public class ChanelService {

    @GET("/")
    public static RestMethod getChanelProgram;

}
