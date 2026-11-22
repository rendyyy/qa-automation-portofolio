package io.github.rendyyy.qa.selenium.tests.hybrid;

import io.github.rendyyy.qa.common.api.ApiConfig;
import io.github.rendyyy.qa.common.api.PlatformApiClient;
import io.github.rendyyy.qa.common.api.model.ContactMessage;
import io.github.rendyyy.qa.selenium.pages.platform.AdminMessagesPage;
import io.github.rendyyy.qa.selenium.pages.platform.PlatformHomePage;
import io.github.rendyyy.qa.selenium.tests.base.BaseTest;
import io.github.rendyyy.qa.selenium.tests.data.ContactMessageFactory;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.HYBRID;
import static io.github.rendyyy.qa.selenium.tests.base.TestGroups.REGRESSION;
import static org.assertj.core.api.Assertions.assertThat;

public class AdminMessageHybridTests extends BaseTest {

    private static final Logger LOG = LogManager.getLogger(AdminMessageHybridTests.class);

    // One list per thread: tests run in parallel on the same instance.
    private final ThreadLocal<List<String>> createdSubjects = ThreadLocal.withInitial(ArrayList::new);

    @Test(groups = {REGRESSION, HYBRID},
            description = "A contact message created through the API is listed in the admin panel")
    public void shouldShowApiCreatedMessageInAdminPanel() {
        ContactMessage message = ContactMessageFactory.random();

        // Arrange: create the data through the API
        Response response = new PlatformApiClient().createMessage(message);
        createdSubjects.get().add(message.subject());
        assertThat(response.statusCode()).as("create message through the API").isBetween(200, 299);

        // Act: sign in through the UI and open the message list
        AdminMessagesPage messages = new PlatformHomePage(driver())
                .load()
                .openAdminLogin()
                .loginAs(ApiConfig.platformAdminUsername(), ApiConfig.platformAdminPassword())
                .openMessages();

        // Assert: the subject is unique per run, so it can only be listed if the API stored it
        assertThat(messages.isMessageListed(message.subject()))
                .as("message with subject '%s' is listed in the admin panel", message.subject())
                .isTrue();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUpMessages() {
        List<String> subjects = createdSubjects.get();
        try {
            if (!subjects.isEmpty()) {
                PlatformApiClient api = new PlatformApiClient();
                String token = api.fetchAdminToken();
                for (String subject : subjects) {
                    List<Integer> ids = api.findMessageIdsBySubject(subject, token);
                    LOG.info("Cleanup: found {} message(s) with subject '{}'", ids.size(), subject);
                    ids.forEach(id -> LOG.info("Cleanup: DELETE message {} -> status {}",
                            id, api.deleteMessage(id, token).statusCode()));
                }
            }
        } catch (RuntimeException e) {
            // The public demo server is shared and resets itself, so a failed cleanup must not fail the test.
            LOG.warn("Cleanup of messages {} failed: {}", subjects, e.getMessage());
        } finally {
            createdSubjects.remove();
        }
    }
}