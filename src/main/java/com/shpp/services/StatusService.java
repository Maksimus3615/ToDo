package com.shpp.services;

import com.shpp.models.Status;
import org.springframework.stereotype.Service;


@Service
public class StatusService {

    public Status getStatusByIndex(int index) {
        for (Status status : Status.values()) {
            if (status.getIndex() == index) {
                return status;
            }
        }
        return null;
    }
}
