package admin;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import io.appium.java_client.android.Activity;
import utils.TestBase;
import utils.TestUtils;

public class Login extends TestBase {

	@Test
	public static void navigateToLoginPage()
			throws InterruptedException, IOException, ParseException {
		headerText("Navigate to Sign Up Page");

		TestUtils.waitForVisibilityOf(By.id("com.daretoinnovate.oze:id/oze_logo"));
		TestUtils.elementIsPresent(By.id("com.daretoinnovate.oze:id/oze_logo"), "OZE logo");
		TestUtils.assertSearchText("ID", "com.daretoinnovate.oze:id/get_oze_label", "Get OZÉ and run your business like a pro");
		Thread.sleep(500);
		TestUtils.click(By.id("com.daretoinnovate.oze:id/project_title"));
		Thread.sleep(500);

		TestUtils.click(By.id("com.daretoinnovate.oze:id/btn_signup"));

		TestUtils.waitForVisibilityOf(By.id("com.daretoinnovate.oze:id/call_to_login"));
		TestUtils.click(By.id("com.daretoinnovate.oze:id/call_to_login"));
	}
	
	public static void login(String phone, String pw2) throws InterruptedException {
		String loginDetails = "Login with Phone number: (" + phone + ") and password (" + pw2 + ")";
		Markup a = MarkupHelper.createLabel(loginDetails, ExtentColor.BLUE);
		testInfo.get().info(a);

		// Change country code
		TestUtils.click(By.id("com.daretoinnovate.oze:id/textView_selectedCountry"));
		TestUtils.waitForVisibilityOf(By.id("com.daretoinnovate.oze:id/rl_holder"));

		TestUtils.click(By.id("com.daretoinnovate.oze:id/editText_search"));
		TestUtils.sendKeys(By.id("com.daretoinnovate.oze:id/editText_search"), "Nigeria");
		Thread.sleep(500);
		TestUtils.click(By.id("com.daretoinnovate.oze:id/textView_countryName"));

		TestUtils.sendKeys(By.id("com.daretoinnovate.oze:id/phone_number"), phone);
		TestUtils.sendKeys(By.id("com.daretoinnovate.oze:id/edit_text_password"), pw2);
		TestUtils.click(By.id("com.daretoinnovate.oze:id/btn_login"));
	}
	
	@Test
	public static void emptyPhoneEmptyPasswordTest() throws InterruptedException {
		login("", "");
		TestUtils.assertSearchText("ID", "com.seamfix.bioregistra:id/field_info_text", "Please provide a valid email address");
	}
	
	@Test
	@Parameters({ "dataEnv" })
	public static void invalidPhonePasswordTest(String dataEnv) throws FileNotFoundException, IOException, ParseException, InterruptedException {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("resources/" + dataEnv + "/test.conf.json"));
		JSONObject envs = (JSONObject) config.get("invalid_phone_password");
		String phone = (String) envs.get("phone");
		String pw = (String) envs.get("pw");
		
		login(phone, pw);
		TestUtils.assertToast("Please make sure the mobile number you entered is valid");
		
	}
	
	@Test
	@Parameters({"dataEnv"})
	public static void validPhoneEmptyPasswordTest(String dataEnv) throws FileNotFoundException, IOException, ParseException, InterruptedException {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("resources/" + dataEnv + "/test.conf.json"));
		JSONObject envs = (JSONObject) config.get("valid_Phone_Empty_Password");
		String phone = (String) envs.get("phone");
		
		login(phone, "");
		TestUtils.waitForVisibilityOf(By.id(""));
		TestUtils.assertSearchText("ID", "", "");
	}
	
	@Test
	@Parameters({"dataEnv"})
	public static void emptyPhoneValidPasswordTest(String dataEnv) throws FileNotFoundException, IOException, ParseException, InterruptedException {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("resources/" + dataEnv + "/test.conf.json"));
		JSONObject envs = (JSONObject) config.get("empty_Phone_Valid_Password");
		String pw = (String) envs.get("pw");
		
		login("", pw);
		TestUtils.waitForVisibilityOf(By.id(""));
		TestUtils.assertSearchText("ID", "", "");
	}
	
	@Test
	@Parameters({ "dataEnv" })
	public void validPhoneWrongPasswordTest(String dataEnv)
			throws InterruptedException, FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("resources/" + dataEnv + "/test.conf.json"));
		JSONObject envs = (JSONObject) config.get("valid_Phone_Wrong_Password");
		String phone = (String) envs.get("phone");
		String pw = (String) envs.get("pw");

		login(phone, pw);
		
		TestUtils.assertToast("");
	}
	
	@Test
	@Parameters({ "dataEnv" })
	public void wrongPhoneValidPasswordTest(String dataEnv)
			throws InterruptedException, FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("resources/" + dataEnv + "/test.conf.json"));
		JSONObject envs = (JSONObject) config.get("wrong_Phone_Valid_Password");
		String phone = (String) envs.get("phone");
		String pw = (String) envs.get("pw");

		login(phone, pw);
		
		TestUtils.assertToast("");
	}

	@Test
	@Parameters({ "dataEnv" })
	public static void validLoginTest(String dataEnv, String testVal) throws FileNotFoundException, IOException, ParseException, InterruptedException {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("resources/" + dataEnv + "/test.conf.json"));
		JSONObject envs = (JSONObject) config.get(testVal);
		String phone = (String) envs.get("phone");
		String pw = (String) envs.get("pw");
		
		login(phone, pw);
	}

	public static void headerText(String msg) {
		String header = msg;
		Markup text = MarkupHelper.createLabel(header, ExtentColor.BLUE);
		testInfo.get().info(text);
	}
}
