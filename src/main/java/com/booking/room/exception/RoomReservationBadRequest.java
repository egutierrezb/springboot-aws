package com.booking.room.exception;

public class RoomReservationBadRequest extends RuntimeException {
    public RoomReservationBadRequest(String str){
        super(str);
    }
}
