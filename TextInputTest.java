package test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.DesiredCapabilities;



import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.MobileCapabilityType;

/**
 * For text input testing, this program assumes that the tester has
 * downloaded the Replika app to the emulator, has made a Replika 
 * account and has logged into that account before testing starts
 */
public class TextInputTest {
	
	public static void main(String[] args)throws IOException{
		
		//sets up the driver for the android emulator
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
		dc.setCapability("platformname", "android");
		dc.setCapability("appActivity", "com.chromium.browser.LauncherShortcutActivity");
		
		AndroidDriver<AndroidElement> driver = new AndroidDriver<AndroidElement>(new URL("http://0.0.0.0:4723/wd/hub"), dc);
		
		//buffered reader to read in the txt file
		BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.home")+"/Downloads/textTest.txt"));
		
		//array list to hold all the lines in the txt file
		ArrayList<String> txt = new ArrayList<String>();
		
		//while loop to add each time into the txt ArrayList
		String currentLine = br.readLine();
		while(currentLine != null && currentLine != "\n") {
			txt.add(currentLine);
			currentLine = br.readLine();
		}
		br.close();
		
		try {
			//opens Replika app
			MobileElement el1 = (MobileElement) driver.findElementByAccessibilityId("Replika");
			el1.click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			
			//opens chat room
			MobileElement el2 = (MobileElement) driver.findElementById("ai.replika.app:id/chatButton");
			el2.click();
			driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
			
			//for loop to loop through each line in the txt ArrayList
			for(int i=0; i<txt.size(); i++) {
				try {
					
					//clicks the user message section of the chat room
					MobileElement el3 = (MobileElement) driver.findElementById("ai.replika.app:id/fragment_chat_user_message");
					el3.click();
					
					//sends keys to the user message section from the ArrayList
					el3.sendKeys(txt.get(i));
					driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
					
					//sends the sent keys as a message
					MobileElement el4 = (MobileElement) driver.findElementByAccessibilityId("Send message: " + txt.get(i));
					el4.click();
					driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
					
					//lets the tester know if the input passed
					System.out.println("Input: " + txt.get(i) + " passed");
				} catch (Exception e) {
					
					//lets the tester know if the input failed
					System.out.println("Input: " + txt.get(i) + " failed");
				}
			}
			
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			
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
