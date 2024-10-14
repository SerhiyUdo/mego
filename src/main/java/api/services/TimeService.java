package api.services;

import com.epam.http.annotations.*;
import com.epam.http.requests.RestMethod;

@ServiceDomain("${serverUrl}/time")
public class TimeService {
    @GET("/")
    public static RestMethod getTime;

}
