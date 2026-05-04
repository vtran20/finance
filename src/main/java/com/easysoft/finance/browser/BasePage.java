package com.easysoft.finance.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;


import javax.annotation.PostConstruct;

public class BasePage {

    @Autowired
    public WebDriver driver;

    @PostConstruct
    public void initDriver() {
        PageFactory.initElements(driver, this);
    }

    public void Navigate(String url) {
        this.driver.navigate().to(url);
    }
}
