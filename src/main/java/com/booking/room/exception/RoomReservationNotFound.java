package com.booking.room.exception;

public class RoomReservationNotFound extends RuntimeException{
    public RoomReservationNotFound(String str) {
        super(str);
    }
}
