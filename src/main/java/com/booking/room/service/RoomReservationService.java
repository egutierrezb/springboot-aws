package com.booking.room.service;

import com.booking.room.exception.RoomReservationBadRequest;
import com.booking.room.model.Room;
import com.booking.room.repository.RoomRepository;
import com.booking.room.utilities.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class RoomReservationService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    Room room;

    private static final Logger LOG = LoggerFactory.getLogger(RoomReservationService.class);

    public Room getStringTest() {
        room.setDescription("Testing description");
        room.setId(111);
        room.setName("Test room");
        room.setEmail("test@test.com");
        room.setReservation(true);
        LOG.info("Inside of testing");
        return room;
    }

    public void saveRoom(Room room) throws RuntimeException {
        try {
            this.room.setReservation(true);
            roomRepository.save(room);
        }catch(RuntimeException e) {
            LOG.warn("Unable on storing data into db");
            if(e.getMessage().contains("Could not commit JPA transaction"))
                throw new RoomReservationBadRequest("Error on storing data in db!");
            else
                throw new RuntimeException("Unknown error");
        }
    }


    public void updateRoom(Room room) throws RuntimeException {
        try {
            roomRepository.save(room);
        }catch(RuntimeException e) {
            LOG.warn("Unable on updating data into db");
            if(e.getMessage().contains("Could not commit JPA transaction"))
                throw new RoomReservationBadRequest("Error on storing data in db!");
            else
                throw new RuntimeException("Unknown error");
        }
    }


    public void feedDatabase() throws RuntimeException {
        List<Room> roomList = new ArrayList<>();
        for(int i = 0; i< Constants.MAX_ELEMENTS_LIST; i++) {
            roomList.add(new Room(i+1,
                    "test"+i,
                    "test_description "+i,
                    "test"+i+"@"+"test.com",
                    false));
        }
        try{
            roomRepository.saveAll(roomList);
        }catch(RuntimeException e) {
            LOG.warn("Unable on storing data into db");
            throw new RuntimeException(e);
        }
    }

    public List<Room> getAllRooms() {
        List<Room> roomReservationList = new ArrayList<>();
        //This would be the equivalent of roomReservationList::add
        Consumer<Room> action = s->roomReservationList.add(s);
        roomRepository.findAll().forEach(roomReservationList::add);
        return roomReservationList;
    }

    public List<Room> getRoomsReserved(Boolean reservation) {
        List<Room> roomReservationList = new ArrayList<>();
        roomRepository.findRoomsReserved(reservation).forEach(roomReservationList::add);
        return roomReservationList;
    }

    public List<Room> getRoomsByRange(Integer id1, Integer id2) {
        List<Room> roomList = roomRepository.findRoomsFromToId(id1, id2);
        return(roomList);
    }


}
