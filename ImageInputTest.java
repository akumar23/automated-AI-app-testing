package test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.PushesFiles;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.nativekey.PressesKey;
import io.appium.java_client.remote.MobileCapabilityType;

/**
 * For image input testing, this program assumes that the tester has
 * downloaded the Replika app to the emulator, has made a Replika 
 * account and has logged into that account before testing starts
 *
 * RUN Get_Images.java FIRST TO TRANSFER IMAGES FROM THE COMPUTER TO
 * THE SDCARD OF THE EMULATOR
 */
public class ImageInputTest {
	

	public static void main(String[] args)throws IOException{
		
		//sets up the driver for the android emulator
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
		dc.setCapability("platformname", "android");
		dc.setCapability("appActivity", "com.chromium.browser.LauncherShortcutActivity");
        dc.setCapability(MobileCapabilityType.PLATFORM_NAME, "ANDROID");
        dc.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11.0");
        dc.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel 4 XL API 30");
        dc.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
		AndroidDriver<AndroidElement> driver = new AndroidDriver<AndroidElement>(new URL("http://0.0.0.0:4723/wd/hub"), dc);
		driver.manage().timeouts().implicitlyWait(600, TimeUnit.SECONDS);
		
		try {
			
			//defines a file imgFolder with the path for the images folder where the images for testing are stored
			File imgFolder = new File(System.getProperty("user.home") + "/Downloads/images/");
			
			//ArrayList img to hold the name of all the images in the images folder
			ArrayList<String> img = new ArrayList<String>(Arrays.asList(imgFolder.list()));
			
			//opens Replika app
			MobileElement el1 = (MobileElement) driver.findElementByAccessibilityId("Replika");
			el1.click();
			
			//opens chat room
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			MobileElement el2 = (MobileElement) driver.findElementById("ai.replika.app:id/chatButton");
			el2.click();
			
			//loops through the number of images in the images folder
			for(int i=0; i<img.size(); i++) {
				
				//clicks on send picture icon to send image to Replika
				MobileElement el3 = (MobileElement) driver.findElementByAccessibilityId("Send a picture");
				driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
				el3.click();
				
				//clicks on from Files when the select a source window pops up
				MobileElement el4 = (MobileElement) driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.ScrollView/android.widget.TabHost/android.widget.LinearLayout/android.widget.FrameLayout/com.android.internal.widget.ViewPager/android.widget.RelativeLayout/com.android.internal.widget.RecyclerView/android.widget.LinearLayout/android.widget.LinearLayout[2]/android.widget.ImageView");
				el4.click();
				driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
				
				try {
					
					//selects the image at index i when the Recent images window pops up
					driver.findElements(MobileBy.id("com.google.android.documentsui:id/thumbnail")).get(i).click();
					driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	                
					//clicks on the crop button when the crop window pops up
					MobileElement el5 = (MobileElement) driver.findElementById("ai.replika.app:id/crop_image_menu_crop");
					el5.click();
					
					//in case the crop window is still present, it clicks on the crop button again
					if(driver.findElements(MobileBy.id("ai.replika.app:id/crop_image_menu_crop")).size() > 0) {
						driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
						el5.click();
					}
					
					//waits for the image to properly send
					driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
					Thread.sleep(1000);
					driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
					
					//lets the tester know that the image passed
					System.out.println("Image Test Case: " + img.get(i) + " passed");
				} catch (Exception e) {
					
					//lets the tester know that image failed
					System.out.println("Image Test Case: " + img.get(i) + " failed");
				}
				
			}
			
			driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
			
			//moves back twice and hits the home button to reset the emulator state for future testing
			driver.navigate().back();
			driver.navigate().back();
			driver.pressKey(new KeyEvent(AndroidKey.HOME));
			
			//lets the tester know that the testing is done
			System.out.println("DONE");
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
}
	
