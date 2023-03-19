package com.shpp.services;

import com.shpp.models.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusServiceTest {
    StatusService service = new StatusService();

    @Test
    void testGetStatusByIndex() {
        for (Status s : Status.values()) {
            assertEquals(
                    service.getStatusByIndex(s.getIndex()).toString(),
                    s.toString()
            );
        }
    }

    @Test
    void testGetStatusByIndexReturnNull() {
        int index = 999;
        assertNull(service.getStatusByIndex(index));
    }
}