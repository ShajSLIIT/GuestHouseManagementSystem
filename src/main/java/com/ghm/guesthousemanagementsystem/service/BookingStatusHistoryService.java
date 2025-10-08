package com.ghm.guesthousemanagementsystem.service;

import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import jakarta.annotation.Nullable;

public interface BookingStatusHistoryService {

    void statusChange(Booking booking, BookingStatus toStatus, @Nullable String reason);

}
