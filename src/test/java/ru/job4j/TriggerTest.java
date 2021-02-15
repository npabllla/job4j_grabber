package ru.job4j;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TriggerTest {

    @Test
    public void trigger() {
        assertThat(new Trigger().logic(), is(0));
    }

}