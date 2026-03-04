package org.example.testng;

import org.example.pom.FormPom;
import org.example.utils.Driver;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

public class FormTest {

    static public WebDriver driver;
    static public String URL = "https://demoqa.com/";
    static public String FIRST_NAME = "Eduard";
    static public String LAST_NAME = "Iateniuc";
    static public String EMAIL = "eduardaceman@gmail.com";
    static public String GENDER = "Male";
    static public String NUMBER = "0683934022";
    static public String DATE = "17 Jul 2006";
    static public String SUBJECT = "Maths";
    static public String STATE = "Haryana";
    static public String CITY = "Karnal";
    static public String HOBBY = "Music";

    @BeforeMethod
    public void beforeMethod() throws MalformedURLException {
//        driver = Driver.getAutoLocalDriver();
        driver = Driver.getRemoteDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void formTest() {
        System.out.println("Start test");
        driver.get(URL);
        FormPom formPom = new FormPom(driver);
        formPom.clickForms();
//        formPom.pause(1000);
        formPom.clickPracticeForm();
        formPom.closeAdvert();
        formPom.setFirstName(FIRST_NAME);
        formPom.setLastName(LAST_NAME);
        formPom.setEmail(EMAIL);
        formPom.setGender(GENDER);
        formPom.setNumber(NUMBER);
        formPom.setDate(DATE);
        formPom.setHobby(HOBBY);
        formPom.setSubject(SUBJECT);
        formPom.setState(STATE);
        formPom.setCity(CITY);
        formPom.clickSubmit();
        formPom.pause(5000);

        String actualName = formPom.getTableDataByLabel("Student Name");
        Assert.assertEquals(actualName, FIRST_NAME + " " + LAST_NAME);
        System.out.println("Finish test");

    }

    @AfterMethod
    public void afterMethod() {
        driver.quit();
    }
}
