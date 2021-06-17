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


public class Get_Images {
	

	public static void main(String[] args)throws IOException{
		
		//sets up the driver for the android emulator
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
		dc.setCapability("platformname", "android");
		dc.setCapability("appActivity", "com.chromium.browser.LauncherShortcutActivity");
        dc.setCapability(MobileCapabilityType.PLATFORM_NAME, "ANDROID");
        dc.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11.0");
        dc.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel 4 XL API 30");
        //dc.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
		AndroidDriver<AndroidElement> driver = new AndroidDriver<AndroidElement>(new URL("http://0.0.0.0:4723/wd/hub"), dc);
		driver.manage().timeouts().implicitlyWait(600, TimeUnit.SECONDS);
		
		try {
			
			//defines a file imgFolder with the path for the images folder where the images for testing are stored
			File imgFolder = new File(System.getProperty("user.home") + "/Downloads/images/");
			
			//ArrayList fileList to hold all the paths of the images in the images folder
			ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(imgFolder.listFiles()));
			
			//ArrayList img to hold the name of all the images in the images folder
			ArrayList<String> img = new ArrayList<String>(Arrays.asList(imgFolder.list()));
			
			//for loop to loop through all the elements int the images folder from fileList
			for(int i=fileList.size()-1; i>=0; i--) {
				
				//process builder to run adb root
				ProcessBuilder rootProc = new ProcessBuilder("adb", "root");
				Process root = rootProc.start();
				root.waitFor();
				
				//pushes the image at index i from the images folder
				driver.pushFile("/mnt/sdcard/"+img.get(i), new File(fileList.get(i).toString()));
				
				//lets the tester know that the image is mounted on the sdcard of the emulator
				System.out.println("File: " + img.get(i) + " mounted");
			}
			
			//the system must be rebooted for the images to show up in the sdcard
			//lets the tester know that the system is rebooting
			System.out.println("rebooting...");
			
			//process builder to run adb reboot
			ProcessBuilder rebootProc = new ProcessBuilder("adb", "reboot");
			Process reboot = rebootProc.start();
			reboot.waitFor();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}