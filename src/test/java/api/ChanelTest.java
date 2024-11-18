package api;

import api.services.ChanelService;
import com.epam.http.response.RestResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Instant;
import java.util.*;

import static com.epam.http.requests.ServiceInit.init;
import static utils.AllureAssert.assertEquals;
import static utils.AllureAssert.assertResponseStatus;
import static utils.ResponseUtil.*;

public class ChanelTest {

    public static boolean isCurrentTimestampInRange(long currentTimestamp, Map<Long, Long> timestampRanges) {
        for (Map.Entry<Long, Long> entry : timestampRanges.entrySet()) {
            long start = entry.getKey();
            long end = entry.getValue();
            if (currentTimestamp >= start && currentTimestamp <= end) {
                System.out.println("Current timestamp is in range: " + start + " - " + end);
                return true;
            }
        }
        return false;
    }

    @BeforeClass
    public void preconditions() {
        init(ChanelService.class);
    }

    @Test(dataProvider = "channels", priority = 3, groups = "medium")
    @Severity(SeverityLevel.NORMAL)
    @Description("Get channel and check program is correct test")
    public void getChanelProgramTest(Integer videoId) {
        long currentTimestamp = Instant.now().getEpochSecond();
        System.out.println("Current timestamp: " + currentTimestamp);

        RestResponse response = ChanelService.getChanelProgram
                .queryParams("video_ids=" + videoId).call();
        assertResponseStatus(response, 200);
        printResponseAsJson(response);

        Map<Long, Long> timestampRanges = extractTimestampRanges(response);
        boolean isInRange = isCurrentTimestampInRange(currentTimestamp, timestampRanges);

        if (isInRange) {
            System.out.println("Current timestamp is within one of the program time ranges: " + timestampRanges);
        } else {
            System.out.println("Current timestamp is NOT within any program time range.");
        }

        checkTimestampValidity(currentTimestamp, timestampRanges);
        verifySortedTimestamps(response);
    }

    private Map<Long, Long> extractTimestampRanges(RestResponse response) {
        Map<Long, Long> timestampRanges = new HashMap<>();
        int programQty = getListFromResponse(response, "data[0].programs.start_timestamp", Long.class).size();

        for (int i = 0; i < programQty; i++) {
            Long startTimestamp = getLongFromResponse(response, "data[0].programs.start_timestamp[" + i + "]");
            Long endTimestamp = getLongFromResponse(response, "data[0].programs.end_timestamp[" + i + "]");
            timestampRanges.put(startTimestamp, endTimestamp);
        }

        return timestampRanges;
    }

    private void checkTimestampValidity(long currentTimestamp, Map<Long, Long> timestampRanges) {
        boolean hasPastTimestamps = false;
        boolean hasFutureTimestamps = false;

        for (Map.Entry<Long, Long> entry : timestampRanges.entrySet()) {
            long end = entry.getValue();

            if (end < currentTimestamp) {
                hasPastTimestamps = true;
            }

            if (entry.getKey() > currentTimestamp + 86400) { // 86400 seconds = 24 hours
                hasFutureTimestamps = true;
            }
        }

        System.out.println(hasPastTimestamps ? "There are past program timestamps." : "No past program timestamps.");
        System.out.println(hasFutureTimestamps ? "There are future program timestamps beyond 24 hours." : "No future program timestamps beyond 24 hours.");
    }

    private void verifySortedTimestamps(RestResponse response) {
        List<Long> timeStamps = new ArrayList<>(getListFromResponse(response, "data[0].programs.start_timestamp", Long.class));
        List<Long> sortedTimeStamps = new ArrayList<>(timeStamps);
        Collections.sort(sortedTimeStamps);

        System.out.println("Original timestamps: " + timeStamps);
        System.out.println("Sorted timestamps: " + sortedTimeStamps);

        assertEquals("Verify timestamps lists", timeStamps, sortedTimeStamps);
    }

    @DataProvider
    public Object[] channels() {
        return new Object[]{
                1639111,
                1585681,
                1639231

        };
    }
}
