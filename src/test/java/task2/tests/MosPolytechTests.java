package task2.tests;

import base.BaseSeleniumTest;
import base.TestListener;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task2.pages.MosPolytechPage;
import task2.pages.MosPolytechPageSchedules;

@ExtendWith(TestListener.class)
@Feature("LambdaTest Application Tests")
public class MosPolytechTests extends BaseSeleniumTest {
    private static final Logger logger = LoggerFactory.getLogger(MosPolytechTests.class);
    private MosPolytechPage mosPolytechPage;
    private MosPolytechPageSchedules mosPolytechPageSchedules;

    @BeforeEach
    public void setUpEach() {
        Allure.step("Open Mospolytech page and initialize page objects", () -> {
            mosPolytechPage = new MosPolytechPage();
        });
    }

    @Test
    @DisplayName("Open Schedule Page and Initialize")
    @Story("Open Schedule Page")
    @Description("This test opens the schedule page from the Mospolytech website.")
    public void openSchedulePageAndInitialize() {
        logger.info("Starting test: openSchedulePageAndInitialize");

        Allure.step("Click on the 'Расписание' button", mosPolytechPage::openSchedule);
        Allure.step("Scroll and click on the 'Смотрите на сайте' link", mosPolytechPage::clickViewOnSiteLink);
        Allure.step("Switch to new tab and initialize schedule page", () -> {
            mosPolytechPageSchedules = new MosPolytechPageSchedules();
            mosPolytechPageSchedules.switchToNewWindow();
        });
    }

    @Test
    @DisplayName("Search for Group and Verify Result")
    @Story("Search Group")
    @Description("This test searches for a specific group and verifies the result.")
    public void searchForGroupAndVerifyResult() {
        logger.info("Starting test: searchForGroupAndVerifyResult");

        openSchedulePageAndInitialize();

        Allure.step("Enter group number in search field", () -> {
            mosPolytechPageSchedules.enterGroupNumber("221-361");
        });

        Allure.step("Verify group is in search results", () -> {
            boolean isGroupFound = mosPolytechPageSchedules.isGroupInSearchResults("221-361");
            Assertions.assertTrue(isGroupFound, "Group not found in search results");
        });

        Allure.step("Click on the group in search results", mosPolytechPageSchedules::clickOnSearchResult);
    }

    @Test
    @DisplayName("Verify Current Day Highlighted")
    @Story("Verify Current Day")
    @Description("This test verifies that the current day is highlighted in the schedule.")
    public void verifyCurrentDayHighlighted() {
        logger.info("Starting test: verifyCurrentDayHighlighted");

        searchForGroupAndVerifyResult();

        Allure.step("Verify current day is highlighted", () -> {
            boolean isCurrentDayHighlighted = mosPolytechPageSchedules.isCurrentDayHighlighted();
            Assertions.assertTrue(isCurrentDayHighlighted, "Current day is not highlighted in the schedule");
        });
    }
}
