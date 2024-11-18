package api;

import api.services.TimeService;
import com.epam.http.response.RestResponse;
import io.qameta.allure.Description;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Instant;

import static com.epam.http.requests.ServiceInit.init;
import static utils.AllureAssert.*;
import static utils.ResponseUtil.getLongFromResponse;

public class TimeTest {

    @BeforeClass
    public void preconditions() {
        init(TimeService.class);
    }

    @Test
    @Description("Get time on server and check it is correct test")
    public void getTimeTest() {
        RestResponse response = TimeService.getTime.call();
        assertResponseStatus(response, 200);
        long serverTimestamp = getLongFromResponse(response, "data.timestamp_gmt");
        long currentTimestampSec = Instant.now().getEpochSecond();
        assertEquals("Check server time " + serverTimestamp + " is correct",
                serverTimestamp,
                currentTimestampSec);

    }

}
