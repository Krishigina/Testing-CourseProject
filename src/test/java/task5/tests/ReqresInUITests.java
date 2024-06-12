package task5.tests;

import base.BaseSeleniumTest;
import base.TestListener;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task5.pages.ReqresInPage;

@ExtendWith(TestListener.class)
@Feature("ReqresIn compare API and UI Tests")
public class ReqresInUITests extends BaseSeleniumTest {

    private ReqresInPage reqresInPage;
    private static final Logger logger = LoggerFactory.getLogger(ReqresInUITests.class);

    @BeforeEach
    public void setUpEach() {
        Allure.step("Open ReqresIn page and initialize page", () -> {
            reqresInPage = new ReqresInPage();
        });
    }

    @Test
    @DisplayName("Verify API and UI responses for various endpoints")
    @Story("Complete API and UI response verification")
    @Description("This test verifies that the results from clicking buttons match the API responses")
    public void test() {
        verifyApiResponse("List users", "get");
        verifyApiResponse("Single user", "get");
        verifyApiResponse("Single user not found", "get");
        verifyApiResponse("List <resource>", "get");
        verifyApiResponse("Single <resource>", "get");
        verifyApiResponse("Single <resource> not found", "get");
        verifyApiResponse("Create", "post");
        verifyApiResponse("Update", "put");
        verifyApiResponse("Update", "patch");
        verifyApiResponse("Delete", "delete");
        verifyApiResponse("Register - successful", "post");
        verifyApiResponse("Register - unsuccessful", "post");
        verifyApiResponse("Login - successful", "post");
        verifyApiResponse("Login - unsuccessful", "post");
        verifyApiResponse("Delayed response", "get");
    }

    private void verifyApiResponse(String buttonName, String httpMethod) {
        Allure.step("Verify " + buttonName + " API response with " + httpMethod.toUpperCase() + " method", () -> {
            try {
                reqresInPage.clickOnButtonAndCheckAPI(buttonName, httpMethod);
                logger.info("Checked response of '{}' against the API", buttonName);
            } catch (AssertionError e) {
                logger.error("Test failed: Verify API responses for various endpoints - {}", buttonName, e);
                throw e;
            }
        });
    }
}
