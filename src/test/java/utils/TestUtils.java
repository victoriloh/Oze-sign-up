package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.google.common.collect.ImmutableMap;
import com.testinium.deviceinformation.helper.ProcessHelper;

import enums.TargetTypeEnum;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;

public class TestUtils extends TestBase {

	// @SuppressWarnings("resource")
	public static String addScreenshot() {

		TakesScreenshot ts = getDriver();
		File scrFile = ts.getScreenshotAs(OutputType.FILE);

		String encodedBase64 = null;
		FileInputStream fileInputStreamReader;
		try {
			fileInputStreamReader = new FileInputStream(scrFile);
			byte[] bytes = new byte[(int) scrFile.length()];
			fileInputStreamReader.read(bytes);
			encodedBase64 = new String(Base64.encodeBase64(bytes));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "data:image/png;base64," + encodedBase64;
	}


	public static String randomText(Integer length) {
		Random rand = new Random();
		StringBuilder result = new StringBuilder();
		String alpha = "abcdefghijklmnopqrstuvwxyz";
		char[] chars = alpha.toCharArray();

		for (int i = 0; i < length; i++) {
			Integer num = rand.nextInt(26);
			result.append(chars[num]);
		}

		return result.toString();
	}

	 public static String generatePhoneNumber() {

	        long y = (long) (Math.random() * 100000 + 0333330000L);
	        String Surfix = "080";
	        String num = Long.toString(y);
	        String number = Surfix + num;
	        return number;

	    }

	public static void sendKeys(By locator, String text) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(getDriver(), 30);

		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		getDriver().findElement(locator).clear();
		getDriver().findElement(locator).sendKeys(text);
	}

	public static void waitForVisibilityOf(By locator) {
		WebDriverWait wait = new WebDriverWait(getDriver(), 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public static void click(By locator) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(getDriver(), 30);

		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		MobileElement clickElemnet = (MobileElement) getDriver().findElement(locator);
		clickElemnet.click();
		Thread.sleep(500);
	}

	public static void assertToast(String expected) {
		WebElement toastView = getDriver().findElement(By.xpath("//android.widget.Toast[1]"));
		String text = toastView.getAttribute("name");
		testInfo.get().info(text);
		
		Assert.assertEquals(text, expected);

		// TODO: //div[@class='toast-message'], //android.widget.Toast,
		// //android.widget.Toast[@text='toast text']
	}

	public static void elementIsPresent(By by, String element) {
		try {
			getDriver().findElement(by);
			testInfo.get().log(Status.INFO, element + " is present.");
		} catch (NoSuchElementException e) {
			testInfo.get().log(Status.FAIL, element + " is not present.");
		}
	}

	public static void elementIsNotPresent(By by, String element) {
		try {
			getDriver().findElement(by);
			testInfo.get().log(Status.FAIL, element + " is present.");
		} catch (NoSuchElementException e) {
			testInfo.get().log(Status.INFO, element + " is not present.");
		}
	}

	/**
	 * @param type
	 * @param element
	 * @param value
	 * @description to check if the expected text is present in the page.
	 */
	public static void assertSearchText(String type, String element, String value) {

		StringBuffer verificationErrors = new StringBuffer();
		TargetTypeEnum targetTypeEnum = TargetTypeEnum.valueOf(type);
		String ttype = null;

		switch (targetTypeEnum) {
		case ID:
			ttype = getDriver().findElement(By.id(element)).getText();
			break;
		case NAME:
			ttype = getDriver().findElement(By.name(element)).getText();
			break;
		case CSS:
			ttype = getDriver().findElement(By.cssSelector(element)).getText();
			break;

		case XPATH:
			ttype = getDriver().findElement(By.xpath(element)).getText();
			break;
		case CLASS:
			ttype = getDriver().findElement(By.className(element)).getText();
			break;

		default:
			ttype = getDriver().findElement(By.id(element)).getText();
			break;
		}

		try {
			Assert.assertEquals(ttype, value);
			testInfo.get().log(Status.INFO, value + " found");
		} catch (Error e) {
			verificationErrors.append(e.toString());
			String verificationErrorString = verificationErrors.toString();
			testInfo.get().error(value + " not found");
			testInfo.get().error(verificationErrorString);
		}
	}

	public static void hideKeyboard() throws InterruptedException {
		if (getDriver().isKeyboardShown()) {
			getDriver().hideKeyboard();
			Thread.sleep(500);
		}
	}



	// Possible solution to interacting with Android Internal or External memory
	public static String executeAdbCommand(String command) throws IOException {
		Process process = null;
		StringBuilder builder = new StringBuilder();
		String commandString;
		commandString = String.format("%s", command);
		// System.out.print("Command is " + commandString + "\n");
		try {
			process = ProcessHelper.runTimeExec(commandString);
		} catch (IOException e) {
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.print(line + "\n");
			builder.append(line);
		}
		return builder.toString();
	}

}
