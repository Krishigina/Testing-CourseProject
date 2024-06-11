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
@Feature("MosPolytech Application Tests")
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
    @DisplayName("Test for MosPolytech website")
    @Story("Complete workflow for MosPolytech website")
    @Description("This test performs a series of checks and actions on the MosPolytech website.")
    public void fullTest() {
        logger.info("Starting test for MosPolytech website");

        Allure.step("Click on the 'Расписание' button", mosPolytechPage::openSchedule);
        Allure.step("Scroll and click on the 'Смотрите на сайте' link", mosPolytechPage::clickViewOnSiteLink);
        Allure.step("Switch to new tab and initialize schedule page", () -> {
            mosPolytechPageSchedules = new MosPolytechPageSchedules();
            mosPolytechPageSchedules.switchToNewWindow();
        });

        Allure.step("Enter group number in search field", () -> {
            mosPolytechPageSchedules.enterGroupNumber("221-361");
        });

        Allure.step("Verify group is in search results", () -> {
            boolean isGroupFound = mosPolytechPageSchedules.isGroupInSearchResults("221-361");
            Assertions.assertTrue(isGroupFound, "Group not found in search results");
        });

        Allure.step("Click on the group in search results", mosPolytechPageSchedules::clickOnSearchResult);

        Allure.step("Verify current day is highlighted", () -> {
            boolean isCurrentDayHighlighted = mosPolytechPageSchedules.isCurrentDayHighlighted();
            Assertions.assertTrue(isCurrentDayHighlighted, "Current day is not highlighted in the schedule");
        });
    }
}
