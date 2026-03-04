package org.example.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Driver {
    private static final Logger log = LoggerFactory.getLogger(Driver.class);

    public static WebDriver getAutoLocalDriver() {

        ChromeOptions options = new ChromeOptions();

        if (System.getenv("CI") != null) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        }

        // Ensure chromedriver binary is present
        WebDriverManager.chromedriver().setup();

        return new ChromeDriver(options);
    }
    static public WebDriver getLocalDriver() {
        // Use WebDriverManager instead of manual System property
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        return new ChromeDriver(options);
    }

    public static WebDriver getDriverFromEnv() {
        Map<String, String> env = System.getenv();
        if ("true".equalsIgnoreCase(env.getOrDefault("USE_REMOTE_DRIVER", "false"))) {
            log.info("USE_REMOTE_DRIVER=true, using remote driver");
            return getRemoteDriver();
        }
        log.info("USE_REMOTE_DRIVER is false or not set, using local driver");
        return getAutoLocalDriver();
    }

    public static WebDriver getRemoteDriver() {
        String selenoidUrl = System.getenv().getOrDefault("SELENOID_URL", "http://localhost:4444/wd/hub");
        log.info("Initializing remote ChromeDriver for Selenoid URL: {}", selenoidUrl);
        ChromeOptions options = new ChromeOptions();
        options.setCapability("browserVersion", "128.0");
        options.setCapability("selenoid:options", new HashMap<String, Object>() {{
            /* How to add test badge */
            put("name", "Test badge...");

            /* How to set session timeout */
            put("sessionTimeout", "15m");

            /* How to set timezone */
            put("env", new ArrayList<String>() {{
                add("TZ=UTC");
            }});

            /* How to add "trash" button */
            put("labels", new HashMap<String, Object>() {{
                put("manual", "true");
            }});

            /* How to enable video recording */
            put("enableVideo", true);
            put("enableVNC", true);
            put("enableLog", true);
            put("noSandbox", true);
            put("headless", true);
        }});

        // Quick connectivity check to provide a clearer error if Selenoid is down
        try {
            URL checkUrl = new URL(selenoidUrl);
            HttpURLConnection conn = (HttpURLConnection) checkUrl.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestMethod("GET");
            conn.connect();
            int code = conn.getResponseCode();
            log.info("Connectivity check to {} returned HTTP {}", selenoidUrl, code);
            // allow any 2xx/3xx/4xx response to proceed — we'll still attempt to create session
        } catch (MalformedURLException e) {
            log.error("Invalid SELENOID_URL value: {}", selenoidUrl, e);
            throw new RuntimeException("Invalid SELENOID_URL value", e);
        } catch (IOException e) {
            log.error("Could not connect to SELENOID at {}: {}. Falling back to local driver.", selenoidUrl, e.getMessage());
            log.debug("Connectivity exception", e);
            return getAutoLocalDriver();
        }

        try {
            RemoteWebDriver remoteDriver = new RemoteWebDriver(new URL(selenoidUrl), options);
            remoteDriver.setFileDetector(new LocalFileDetector());
            log.info("Remote driver session started: {}", remoteDriver.getSessionId());
            return remoteDriver;
        } catch (Exception e) {
            log.error("Failed to start remote driver at {}. Falling back to local driver.", selenoidUrl, e);
            // As a safe fallback, return a local headless-capable driver
            return getAutoLocalDriver();
        }
    }
}