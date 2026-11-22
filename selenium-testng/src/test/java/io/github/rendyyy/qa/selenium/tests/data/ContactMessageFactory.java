package io.github.rendyyy.qa.selenium.tests.data;

import io.github.rendyyy.qa.common.api.model.ContactMessage;

import java.util.UUID;

/** Builds a message that satisfies the contact form limits and is unique per call. */
public final class ContactMessageFactory {

    private ContactMessageFactory() {
    }

    public static ContactMessage random() {
        String id = UUID.randomUUID().toString().substring(0, 8);
        return new ContactMessage(
                "Hybrid Tester",
                "qa." + id + "@example.com",
                "081234567890",
                "Hybrid check " + id,
                "Message created by an automated hybrid test " + id);
    }
}