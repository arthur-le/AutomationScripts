package tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import android.annotation.SuppressLint;

//telephone imports
//import android.content.Context;
//import android.provider.Settings.Secure;
//import android.telephony.TelephonyManager;
//import android.telephony.CellInfo;


//Open current app and use following in CMD to get app name and appActivity:
//adb shell
//dumpsys window windows | grep mCurrentFocus

public class SendAndReceiveSMSPheonix {
	
	//private static Context context;
	private static String phoneNumber = "";
	
	public static void main(String[] args)
	{
		
		//Set the Desired Capabilities
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", "My Phone");
		
		//Phone id acquired through ADB
		caps.setCapability("udid", "LMQ910b56eeeb3"); 
		caps.setCapability("platformName", "Android");
		caps.setCapability("platformVersion", "8.1.0");
		

		
		//Open google dialer com.google.androidr messaging
		//caps.setCapability("appPackage", "com.google.android.apps.messaging");
		caps.setCapability("appPackage", "com.android.settings");
		
		//messaging main activity to open the app messaging app
		//caps.setCapability("appActivity", "com.google.android.apps.messaging.ui.ConversationListActivity");
		caps.setCapability("appActivity", "com.android.settings.Settings");
		caps.setCapability("noReset", "true");

		
		AppiumDriver<MobileElement> driver = null;
		//Instantiate Appium Driver
		try 
		{
			driver = new AndroidDriver<MobileElement>(new URL("http://0.0.0.0:4723/wd/hub"), caps);
		} 
		catch (MalformedURLException e) 
		{
			System.out.println(e.getMessage());
		}
	

		//get device phone number
		phoneNumber = getDeviceNumber(driver);
		
		
		
		
		//TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		//System.out.println("Phone number is: " );
		//System.out.println("Phone number is: " + mPhoneNumber);
		//sendMessage(driver);

		
	}
	
	
	public static String getDeviceNumber(AppiumDriver<MobileElement> driver)
	{
		String phoneNumber = "";
		
		//CODE FOR SCROLLING
		//Scrolls to "System". "Note that the string cannot contain commans such as \"System, device"\ or error occurs
		driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true))"
				+ ".scrollIntoView(new UiSelector().textContains(\"System\"))"));
		
		
		//Taps on "System"
		driver.findElement(MobileBy.AndroidUIAutomator(
				"new UiScrollable(new UiSelector().resourceId(\" \")).scrollIntoView("
				+ "new UiSelector().text(\"System\"))")).click();
		
		driver.findElement(MobileBy.AndroidUIAutomator(
				"new UiScrollable(new UiSelector().resourceId(\" \")).scrollIntoView("
				+ "new UiSelector().text(\"About phone\"))")).click();

		driver.findElement(MobileBy.AndroidUIAutomator(
				"new UiScrollable(new UiSelector().resourceId(\" \")).scrollIntoView("
				+ "new UiSelector().text(\"Status\"))")).click();
		
		driver.findElement(MobileBy.AndroidUIAutomator(
				"new UiScrollable(new UiSelector().resourceId(\" \")).scrollIntoView("
				+ "new UiSelector().text(\"SIM status\"))")).click();
		
		
		//Identify List of elements using Resource ID
		List<MobileElement> allTitleElements = ((AndroidDriver<MobileElement>)driver).findElementsByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/summary\")");		
		//System.out.println("Element Count - " + allTitleElements.size());
		
		//Obtain phone number from list of elements with Resrouce ID "adnroid:id/summary"
		//Grab 7th element which is phone number and store into text
				//at element [6]
		int counter = 0;
		for(MobileElement element : allTitleElements) 
		{
			if(counter == 6)
			{
				phoneNumber = element.getText().toString();
				break;
			}
			else
			{
				counter++;
			}
		}
		
		
	
		System.out.println("Number is: " + phoneNumber );
		
		return phoneNumber;
	}
	
	public static void sendMessage(AppiumDriver<MobileElement> driver)
	{
		
		
		
	}
	
	
	

}
