package admin;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import utils.TestBase;
import utils.TestUtils;

public class SignUp extends TestBase {

	@Test
	public static void navigateToSIgnUpPage()
			throws InterruptedException, IOException, ParseException {
		headerText("Navigate to Sign Up Page");

		TestUtils.waitForVisibilityOf(By.id("com.daretoinnovate.oze:id/oze_logo"));
		TestUtils.elementIsPresent(By.id("com.daretoinnovate.oze:id/oze_logo"), "OZE logo");
		TestUtils.assertSearchText("ID", "com.daretoinnovate.oze:id/get_oze_label", "Get OZÉ and run your business like a pro");
		Thread.sleep(500);
		TestUtils.click(By.id("com.daretoinnovate.oze:id/project_title"));
		Thread.sleep(500);

		TestUtils.click(By.id("com.daretoinnovate.oze:id/btn_signup"));
	}

	@Test
	@Parameters({ "dataEnv" })
	public void emptySignUpForm() throws InterruptedException {

		headerText("Attempt to Submit an empty form");

		// Ensure all input fields are empty
		getDriver().findElement(By.id("com.daretoinnovate.oze:id/phone_number")).clear();
		Thread.sleep(500);
		getDriver().findElement(By.id("com.daretoinnovate.oze:id/new_user_email")).clear();
		Thread.sleep(500);
		getDriver().findElement(By.id("com.daretoinnovate.oze:id/new_user_password")).clear();
		Thread.sleep(500);
		getDriver().findElement(By.id("com.daretoinnovate.oze:id/new_user_password2")).clear();
		Thread.sleep(500);
		//Sign Up
		TestUtils.click(By.id("com.daretoinnovate.oze:id/btn_signup"));
		Thread.sleep(1000);
		TestUtils.assertToast("Please fill all the fields");
	}

	@Test
	@Parameters({ "dataEnv" })
	public void incompleteSignUpForm(String dataEnv) throws InterruptedException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("resources/" + dataEnv + "/test.conf.json"));
		JSONObject envs = (JSONObject) config.get("signUp");
		String countryCode = (String) envs.get("countryCode");
		String phone = (String) envs.get("phone");
		String email = (String) envs.get("email");
		String password = (String) envs.get("password");

		headerText("Attempt to Submit an incomplete form(Phone Number missing)");

		signUpForm(countryCode,"", email, password, password);
		Thread.sleep(1000);
		TestUtils.assertToast("Please fill all the fields");

		headerText("Attempt to Submit an incomplete form(Email missing)");

		signUpForm(countryCode,phone, "", password, password);
		Thread.sleep(1000);
		TestUtils.assertToast("Please fill all the fields");

		headerText("Attempt to Submit an incomplete form(Password missing)");

		signUpForm(countryCode,phone, email, "", password);
		Thread.sleep(1000);
		TestUtils.assertToast("Please fill all the fields");

		headerText("Attempt to Submit an incomplete form(New Password missing)");

		signUpForm(countryCode,phone, email, password, "");
		Thread.sleep(1000);
		TestUtils.assertToast("Please fill all the fields");
	}

	@Test
	@Parameters({ "dataEnv" })
	public void invalidSignUpForm(String dataEnv) throws InterruptedException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("resources/" + dataEnv + "/test.conf.json"));
		JSONObject envs = (JSONObject) config.get("signUp");
		String countryCode = (String) envs.get("countryCode");
		String phone = (String) envs.get("phone");
		String email = (String) envs.get("email");
		String password = (String) envs.get("password");

		headerText("Attempt to Submit an invalid form(Phone Number too small)");
		signUpForm(countryCode,"123456", email, password, password);
		Thread.sleep(1000);
		TestUtils.assertToast("Please make sure the mobile number you entered is valid");

		headerText("Attempt to Submit an invalid form(Phone Number too large)");
		signUpForm(countryCode,"1234566787665", email, password, password);
		Thread.sleep(1000);
		TestUtils.assertToast("Please make sure the mobile number you entered is valid");

		headerText("Attempt to Submit an invalid form(Email format missing '@' )");
		signUpForm(countryCode,phone, "victor", password, password);
		Thread.sleep(1000);
		TestUtils.assertToast("Please enter a valid email address");

		headerText("Attempt to Submit an invalid form(Email format missing '.com' )");
		signUpForm(countryCode,phone, "victor@yopmail", password, password);
		Thread.sleep(1000);
		TestUtils.assertToast("Please enter a valid email address");

		headerText("Attempt to Submit an invalid form(Password has no capital character)");
		signUpForm(countryCode,phone, email, "password", password);
		Thread.sleep(1000);
		// TODO: Note error the error message is wrong
		TestUtils.assertToast("Please make sure your password has at least 8 characters");

		headerText("Attempt to Submit an invalid form(Password has less than 8 character)");
		signUpForm(countryCode,phone, email, "passwor", password);
		Thread.sleep(1000);
		TestUtils.assertToast("Please make sure your password has at least 8 characters");

		headerText("Attempt to Submit an invalid form(New Password does not match main password)");
		signUpForm(countryCode,phone, email, password, "password1");
		Thread.sleep(1000);
		TestUtils.assertToast("Retype password is not the same as password entered");
	}

	@Test
	@Parameters({ "dataEnv" })
	public void fillSignUpForm(String dataEnv) throws IOException, ParseException, InterruptedException {
		JSONParser parser = new JSONParser();
		JSONObject config = (JSONObject) parser.parse(new FileReader("resources/" + dataEnv + "/test.conf.json"));
		JSONObject envs = (JSONObject) config.get("signUp");
		String countryCode = (String) envs.get("countryCode");
		String phone = (String) envs.get("phone");
		String email = (String) envs.get("email");
		String password = (String) envs.get("password");

		headerText("Submit the SignUp Form with valid details");

		signUpForm(countryCode,phone, email, password, password);

	}

	public static void headerText(String msg) {
		String header = msg;
		Markup text = MarkupHelper.createLabel(header, ExtentColor.BLUE);
		testInfo.get().info(text);
	}

	public static void signUpForm(String countryCode, String phone, String email, String password, String password1) throws InterruptedException {
		TestUtils.waitForVisibilityOf(By.id("com.daretoinnovate.oze:id/phone_number"));
		TestUtils.elementIsPresent(By.id("com.daretoinnovate.oze:id/phone_number"), "Phone Number field is present");

		// Change country code
		TestUtils.click(By.id("com.daretoinnovate.oze:id/textView_selectedCountry"));
		TestUtils.waitForVisibilityOf(By.id("com.daretoinnovate.oze:id/rl_holder"));

		TestUtils.click(By.id("com.daretoinnovate.oze:id/editText_search"));
		TestUtils.sendKeys(By.id("com.daretoinnovate.oze:id/editText_search"), countryCode);
		Thread.sleep(500);
		TestUtils.click(By.id("com.daretoinnovate.oze:id/textView_countryName"));

		// Input phone number
		TestUtils.sendKeys(By.id("com.daretoinnovate.oze:id/phone_number"), phone);

		// Input email address
		TestUtils.sendKeys(By.id("com.daretoinnovate.oze:id/new_user_email"), email);

		// Input password
		TestUtils.sendKeys(By.id("com.daretoinnovate.oze:id/new_user_password"), password);

		// Input new password
		TestUtils.sendKeys(By.id("com.daretoinnovate.oze:id/new_user_password2"), password1);

		//Sign Up
		TestUtils.click(By.id("com.daretoinnovate.oze:id/btn_signup"));
	}

}
