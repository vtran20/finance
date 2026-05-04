package com.easysoft.finance.browser;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.springframework.stereotype.Component;

@Component
public class LoginPage extends BasePage {

    @FindBy(how = How.NAME, using = "su_username")
    public WebElement txtUserName;

    @FindBy(how = How.NAME, using = "su_password")
    public WebElement txtPassword;

    @FindBy(how = How.CSS, using = "button.pw-showhide")
    public WebElement btnLogin;

    public void Login(String userName, String password)
    {
        txtUserName.sendKeys(userName);
        txtPassword.sendKeys(password);
    }

    public void clickLogin()
    {
        btnLogin.submit();
        System.out.println("click login");
    }



}

