package selenium.webdriver.basic;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.firefox.FirefoxDriver;
//comment the above line and uncomment below line to use Chrome
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class auto_deposit {
	static ArrayList<String> account_list=new ArrayList<String>();

	public static ArrayList<String> ReadToList(ArrayList<String> list){		 
		try{
			FileReader FR = new FileReader(".\\Accounts.txt");
			BufferedReader BR = new BufferedReader(FR);
			String Content = "";

			//Loop to read all lines one by one from file and print It.
			while((Content = BR.readLine())!= null){
				System.out.println(Content);
				list.add(Content);
			}
			BR.close();
		}catch (Exception e) {
			System.out.println("File Not Found");
		}
		return list;
	}


	public static void main(String[] args) throws InterruptedException {	
		account_list = ReadToList(account_list);
		System.out.println("AL: "+ account_list);

		//System.setProperty("webdriver.firefox.marionette",".\\driver\\geckodriver.exe");
		//WebDriver driver = new FirefoxDriver();
		//comment the above 2 lines and uncomment below 2 lines to use Chrome
		System.setProperty("webdriver.chrome.driver",".\\driver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		String baseUrl = "https://starsweb.qa-sc.pyr/";

		//Launch Fire fox and direct it to the Base URL
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.get(baseUrl);

		Thread.sleep(1000);

		WebDriverWait wait = new WebDriverWait(driver, 25);

		for (int i = 0; i<account_list.size();++i){
			driver.manage().deleteAllCookies();
			
			//Login button
			By locator = By.xpath("//span[contains(text(), 'Login')]");
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			element.click();

			//Enter name and password
			driver.findElement(By.id("userId")).sendKeys(account_list.get(i));
			driver.findElement(By.id("password")).sendKeys("MyTest123");

			//Login button
			By locator2 = By.xpath("//button[@class='_UFf03IQ _2Fo_xGA _1esiHHz _HQR9OXG']");
			WebElement element2 = wait.until(ExpectedConditions.elementToBeClickable(locator2));
			element2.click();

			//'News' header
			try {
				By header_locator = By.xpath("//header[@class='_2D7132u _2Ws_Q24']");
				WebElement header = wait.until(ExpectedConditions.presenceOfElementLocated(header_locator));
				if(header.isDisplayed()){
					System.out.println("Login Succeed "+account_list.get(i));
				}
			}
			catch (Exception e){
				System.out.println("Login Failed "+account_list.get(i));
				driver.close();
			}
			//Close the News pop-up
			driver.findElement(By.xpath("//button[@class='_jFGMpqT']")).click();

			//User menu button
			By user_menu_loc = By.xpath("//span[@class='_1EEF_71']");
			wait.until(ExpectedConditions.elementToBeClickable(user_menu_loc)).click();

			Thread.sleep(1000);

			//Scroll down to Logout
			WebElement logout = driver.findElement(By.xpath("//a[@href='/account/logout']"));
			logout.sendKeys(Keys.PAGE_DOWN);  
			logout.click();

			Thread.sleep(500);
			//Logout button
			try{
				driver.findElement(By.xpath("/html/body/div[3]/div/div/div/section/main/section/button/span")).click();
				System.out.println("Logout Succeed "+account_list.get(i));
			}catch (Exception e){
				System.out.println("Logout Failed "+account_list.get(i));        	
			}
		}
		driver.close();
	}
}