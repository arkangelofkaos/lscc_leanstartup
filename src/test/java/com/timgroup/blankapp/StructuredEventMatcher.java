package com.timgroup.blankapp;

import com.timgroup.structuredevents.Event;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class StructuredEventMatcher extends TypeSafeDiagnosingMatcher<Event> {
    private final Matcher<String> eventType;

    private StructuredEventMatcher(Matcher<String> eventType) {
        this.eventType = eventType;
    }

    public static StructuredEventMatcher ofType(String type) {
        return new StructuredEventMatcher(Matchers.equalTo(type));
    }

    public static StructuredEventMatcher ofType(Matcher<String> typeMatcher) {
        return new StructuredEventMatcher(typeMatcher);
    }

    @Override
    protected boolean matchesSafely(Event item, Description mismatchDescription) {
        mismatchDescription.appendText("event type was ").appendValue(item.getType());
        return eventType.matches(item.getType());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("event of type ").appendDescriptionOf(eventType);
    }
}
