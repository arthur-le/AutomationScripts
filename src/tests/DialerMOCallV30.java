package tests;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;



//Open current app and use following in CMD to get app name and appActivity:
//adb shell
//dumpsys window windows | grep mCurrentFocus



/*
 * This class automates and stress tests making 2 MO calls and merging into a conference call
 * for any Google device: Pixel 1, Pixel 2, Nexus, Pheonix, etc.
 * Class handles all exceptions including call/sms/mms interrupts, user interrupts, call busy, etc.
 * Programmed to infinitely run until user stops. Can also be programmed to run a
 * set number of times.
 */

public class DialerMOCallV30 {
	
	private static int counter = 0;
	
	
	public static void main(String[] args) throws InterruptedException 
	{
		
		while(true)
		{
			//Set the Desired Capabilities
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability("deviceName", "My Phone");
			
			//Phone id acquired through ADB
			caps.setCapability("udid", "LGH9310a940009"); 
			caps.setCapability("platformName", "Android");
			caps.setCapability("platformVersion", "8.0.0");
			
			//Open google dialer com.google.androidr.dialer
			caps.setCapability("appPackage", "com.lge.launcher3");
			
			//dialer main activity to open the app
			caps.setCapability("appActivity", "com.lge.launcher3.LauncherExtension");
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
			
			WebDriverWait wait = new WebDriverWait(driver, 15);
			
			try 
			{
				//must wait for phone app to be clickable after restart
				//wait for folder and phone app onscreen elements to be clickable before proceeded after the restart
				wait.until(ExpectedConditions.elementToBeClickable(By.className("android.widget.TextView")));		
				driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Phone\")")).click();
			}
			catch(Exception e)
			{
				System.out.println("Error in waiting for phone app to be clickable. Restarting main");
				driver.closeApp();
				String[] args1 = {};
				main(args1);
			}
		
			//Flags if call was ended. 0 is not ended yet
			int flag = 0;
			
			
			//Try to end all ongoing calls if error occurs,
			//catch the exception and restart the main method
			try 
			{
				//try to end all ongoing calls first. If call ended set flag to 1.
				flag = endOngoingCalls(driver);
			}
			catch(Exception e)
			{
				System.out.println("Error occured. Restarting main");
				String[] args1 = {};
				main(args1);
			}
			
			
			
			
			//Try to make two MO calls and merge. If any error occurs,
			//catch the exception and restart the main method
			try 
			{
				//if flag == 0 that means no end call, continue todialer
				//else flag == 1 so skip this and restart main loop to check for ongoing call again
				//So main method must be restarted so that dialer app can be launched from home screen
				
				//Else if flag == 0 and call didnt have to end, go ahead and make MO call straight from the home screen
				if (flag == 0)
				{
					//Make MO calls
					makeMOCalls(driver, caps);
				}
			}
			catch(Exception e)
			{
				System.out.println("Error occured. Restarting main");
				String[] args1 = {};
				main(args1);
			}
			
			
			
		}

	}
	
	private static void makeMOCalls(AppiumDriver<MobileElement> driver, DesiredCapabilities caps)
	{
		//wait time set to 45 seconds
		WebDriverWait wait = new WebDriverWait(driver, 40);
	
		//Must open dialer again if call was ended		
		
		//MAKE FIRST MO CALL TO TEST PHONE SERVERS
		//Tap on the Dial tab
		driver.findElement(MobileBy.AndroidUIAutomator(
				"new UiScrollable(new UiSelector().resourceId(\" \")).scrollIntoView("
				+ "new UiSelector().text(\"Dial\"))")).click();
		
		//Calling echo script number
		//Select each key and dial a number
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"1\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"9\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"0\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"9\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"3\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"9\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"0\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"0\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"0\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"0\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"3\")")).click();
		driver.findElement(By.id("com.android.contacts:id/btnLogsCall")).click();

		
		//ADD SECOND MO CALL TO TO CURRENT CALL
		//Wait for call to be connected 45 second wait			
		wait.until(ExpectedConditions.elementToBeClickable(By.id("com.android.incallui:id/menuIcon")));				
		
		
		//Tap on more menu button once it is clickable
		driver.findElement(By.id("com.android.incallui:id/menuIcon")).click();
		
		// tap on add call button
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Add call\")")).click();

		
		//calling automated script number
		//Dial second MO support
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"1\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"8\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"5\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"8\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"6\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"5\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"1\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"5\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"0\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"5\")")).click();
		driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().text(\"0\")")).click();
		driver.findElement(By.id("com.android.contacts:id/btnLogsCall")).click();
		
		
		
		//Wait for 2nd MO call to connect. Waiting 45 seconds for merge to appear
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/hierarchy/android.widget.FrameLayout/"
				+ "android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.RelativeLayout[1]/"
				+ "android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/"
				+ "android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.Button[@text='Merge calls']")));

		
		//Click on merge button once appeared
		driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/"
				+ "android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.RelativeLayout[1]/"
				+ "android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout[2]/"
				+ "android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.Button[@text='Merge calls']")).click();
		
		//Let conference call run for 20 seconds and then hit end call
		try 
		{
			Thread.sleep(20000);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		//Hit end call button to end script.
		driver.findElement(By.id("com.android.incallui:id/endButton")).click();
		
		counter++;
		System.out.println("Number of conference calls made is: " + counter);
	}
					

	private static int endOngoingCalls(AppiumDriver<MobileElement> driver)
	{
		int flag = 0;
		
		//wait time 40 seconds
		WebDriverWait wait = new WebDriverWait(driver, 40);
		
			
		//Check if Return to call button is present. If it is:
		if(!driver.findElements(By.id("com.android.incallui:id/endButton")).isEmpty()) 
		{
				
				
			//hit the end call button once to end call
			driver.findElement(By.id("com.android.incallui:id/endButton")).click();
			flag = 1;	
			System.out.println("Call had been ended now");
		}
		return flag;
	}

	
	public int getCounter()
	{
		return counter;
	}
	
	
	
}
