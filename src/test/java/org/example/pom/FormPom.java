package org.example.pom;

import io.qameta.allure.Step;
import org.example.utils.StepScreenshots;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;

public class FormPom {
    private static final Logger log = LoggerFactory.getLogger(FormPom.class);

    private final WebDriver driver;
    private final JavascriptExecutor js;
    private final WebDriverWait wait;

    private final By formsCard = By.xpath("//*[text()='Forms']");
    private final By practiceFormMenu = By.xpath("//*[text()='Practice Form']");

    private final By firstName = By.id("firstName");
    private final By lastName = By.id("lastName");
    private final By userEmail = By.id("userEmail");
    private final By userNumber = By.id("userNumber");
    private final By dateOfBirthInput = By.id("dateOfBirthInput");
    private final By subjectsInput = By.id("subjectsInput");
    private final By uploadPicture = By.id("uploadPicture");
    private final By currentAddress = By.id("currentAddress");
    private final By stateContainer = By.id("state");
    private final By cityContainer = By.id("city");
    private final By submitButton = By.id("submit");
    private final By modalTitle = By.id("example-modal-sizes-title-lg");

    public FormPom(WebDriver driverParam) {
        this.driver = driverParam;
        this.js = (JavascriptExecutor) driverParam;
        this.wait = new WebDriverWait(driverParam, Duration.ofSeconds(15));
    }

    @Step("Open Practice Form page")
    public void openPracticeForm() {
        StepScreenshots.before(driver, "Open Practice Form");
        log.info("Opening Practice Form");

        closeAdvert();

        WebElement forms = wait.until(ExpectedConditions.presenceOfElementLocated(formsCard));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", forms);
        js.executeScript("arguments[0].click();", forms);

        WebElement practice = wait.until(ExpectedConditions.presenceOfElementLocated(practiceFormMenu));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", practice);
        js.executeScript("arguments[0].click();", practice);

        StepScreenshots.after(driver, "Open Practice Form");
    }

    @Step("Set first name: {value}")
    public void setFirstName(String value) {
        StepScreenshots.before(driver, "Set first name");
        type(firstName, value);
        StepScreenshots.after(driver, "Set first name");
    }

    @Step("Set last name: {value}")
    public void setLastName(String value) {
        StepScreenshots.before(driver, "Set last name");
        type(lastName, value);
        StepScreenshots.after(driver, "Set last name");
    }

    @Step("Set email: {value}")
    public void setEmail(String value) {
        StepScreenshots.before(driver, "Set email");
        type(userEmail, value);
        StepScreenshots.after(driver, "Set email");
    }

    @Step("Select gender: {gender}")
    public void setGender(String gender) {
        StepScreenshots.before(driver, "Select gender");
        log.info("Setting gender: {}", gender);
        By genderLocator = By.xpath("//*[@id='genterWrapper']//label[text()='" + gender + "']");
        wait.until(ExpectedConditions.elementToBeClickable(genderLocator)).click();
        StepScreenshots.after(driver, "Select gender");
    }

    @Step("Set mobile: {value}")
    public void setMobile(String value) {
        StepScreenshots.before(driver, "Set mobile");
        type(userNumber, value);
        StepScreenshots.after(driver, "Set mobile");
    }

    @Step("Set date of birth: {day} {month} {year}")
    public void setDateOfBirth(int day, String month, String year) {
        StepScreenshots.before(driver, "Set date of birth");
        log.info("Setting date of birth: {} {} {}", day, month, year);
        wait.until(ExpectedConditions.elementToBeClickable(dateOfBirthInput)).click();
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("react-datepicker__month-select"))))
                .selectByVisibleText(month);
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("react-datepicker__year-select"))))
                .selectByVisibleText(year);

        By dayLocator = By.xpath("//div[contains(@class,'react-datepicker__day') and not(contains(@class,'react-datepicker__day--outside-month')) and text()='" + day + "']");
        wait.until(ExpectedConditions.elementToBeClickable(dayLocator)).click();
        StepScreenshots.after(driver, "Set date of birth");
    }

    @Step("Set subject: {subject}")
    public void setSubject(String subject) {
        StepScreenshots.before(driver, "Set subject");
        log.info("Adding subject: {}", subject);
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(subjectsInput));
        input.sendKeys(subject);
        input.sendKeys(Keys.ENTER);
        StepScreenshots.after(driver, "Set subject");
    }

    @Step("Set hobby: {0}")
    public void setHobby(String hobby) {
        log.info("Setting hobby: {}", hobby);

        By hobbyLocator = By.xpath(
                "//*[@id='hobbiesWrapper']//label[text()='" + hobby + "']"
        );

        WebElement hobbyElement = wait.until(
                ExpectedConditions.presenceOfElementLocated(hobbyLocator)
        );

        js.executeScript("arguments[0].scrollIntoView({block:'center'});", hobbyElement);
        js.executeScript("arguments[0].click();", hobbyElement);
    }

    @Step("Upload picture: {fileName}")
    public void uploadPicture(String fileName) {
        StepScreenshots.before(driver, "Upload picture");

        File file = new File("src/test/resources/test-data/" + fileName);

        if (!file.exists()) {
            throw new RuntimeException("File not found: " + file.getAbsolutePath());
        }

        wait.until(ExpectedConditions.presenceOfElementLocated(uploadPicture))
                .sendKeys(file.getAbsolutePath());

        StepScreenshots.after(driver, "Upload picture");
    }

    @Step("Set current address: {value}")
    public void setCurrentAddress(String value) {
        StepScreenshots.before(driver, "Set current address");
        type(currentAddress, value);
        StepScreenshots.after(driver, "Set current address");
    }

    @Step("Set state and city: {state} / {city}")
    public void setStateAndCity(String state, String city) {
        StepScreenshots.before(driver, "Set state and city");
        log.info("Setting state and city: {} / {}", state, city);
        wait.until(ExpectedConditions.elementToBeClickable(stateContainer)).click();
        WebElement stateInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("react-select-3-input")));
        stateInput.sendKeys(state);
        stateInput.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.elementToBeClickable(cityContainer)).click();
        WebElement cityInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("react-select-4-input")));
        cityInput.sendKeys(city);
        cityInput.sendKeys(Keys.ENTER);
        StepScreenshots.after(driver, "Set state and city");
    }

    @Step("Submit form")
    public void submit() {
        StepScreenshots.before(driver, "Submit form");
        log.info("Submitting form");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
        button.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
        log.info("Submission modal is visible");
        StepScreenshots.after(driver, "Submit form");
    }

    @Step("Read submitted value for field: {label}")
    public String getSubmittedValue(String label) {
        StepScreenshots.before(driver, "Read submitted value: " + label);
        By valueLocator = By.xpath("//td[text()='" + label + "']/following-sibling::td");
        String value = wait.until(ExpectedConditions.visibilityOfElementLocated(valueLocator)).getText().trim();
        log.info("Submitted value [{}] = {}", label, value);
        StepScreenshots.after(driver, "Read submitted value: " + label);
        return value;
    }

    public void closeAdvert() {
        try {
            js.executeScript(
                    "document.querySelectorAll('iframe, footer, #fixedban')" +
                            ".forEach(el => el.remove());"
            );
        } catch (Exception ignored) {}
    }

    private void type(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.clear();
        element.sendKeys(value);
    }

}