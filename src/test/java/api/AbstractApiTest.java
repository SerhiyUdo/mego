package api;

import com.epam.http.JdiHttpSettings;
import env.Environment;
import org.testng.annotations.BeforeSuite;

public class AbstractApiTest {
    @BeforeSuite
    public void setEnvironment() {
        Environment env = Environment.get();
        JdiHttpSettings.setDomain("serverUrl=" + env.serverUrl);
    }

}
