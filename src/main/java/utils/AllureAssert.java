package utils;

import com.epam.http.requests.RestRequest;
import com.epam.http.response.RestResponse;
import com.epam.jdi.light.logger.AllureLogData;
import com.epam.jdi.light.logger.AllureLogger;
import com.jayway.jsonpath.internal.JsonFormatter;
//import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.epam.http.JdiHttpSettings.logger;

public class AllureAssert extends AllureLogger {

    public static void assertTrue(String stepDescription, boolean condition, String errorMessage) {
        assertEquals(stepDescription, condition, Boolean.TRUE, errorMessage);
    }

    public static void assertTrue(String stepDescription, boolean condition) {
        assertTrue(stepDescription, condition, null);
    }


    public static void assertTrue(boolean condition) {
        assertTrue("Assert true", condition, null);
    }

    public static void assertEquals(String stepDescription, Object actual, Object expected, String errorMessage) {
        logAllureStep(stepDescription + " (expected: " + expected + ", actual: " + actual + ")", actual.equals(expected));
        logger.step(stepDescription + " (expected: " + expected + ", actual: " + actual + ")");
        Assert.assertEquals(actual, expected, errorMessage);
    }

    public static void assertEquals(String stepDescription, Object actual, Object expected) {
        assertEquals(stepDescription, actual, expected, null);
    }

    private static void logAllureStep(String stepDescription, boolean condition) {
        String stepId = startStep(stepDescription);
        if (condition) {
            passStep(stepId);
        } else {
            String screenPath = null;
            try {
                screenPath = makeScreenshot("After test");
            } catch (Exception ex) {
                attachText("Screenshot failed", "text/plain", ex.getMessage());
            }
            failStep(stepId, new AllureLogData(screenPath, null, null));
        }
    }

    public static void assertResponseStatus(RestResponse response, int expectedStatus) {
        assertEquals("Assert response status", response.getStatus().getCode(), expectedStatus, null);
    }

    public static void assertDataListFromResponse(RestResponse response, List<String> data) {
        data.forEach(d -> assertTrue("Verify \"" + d + "\" exists in response",
                response.getBody().contains(d)));
    }

    public static void logAllureStep(String stepDescription) {
        logAllureStep(stepDescription, true);
    }
}
