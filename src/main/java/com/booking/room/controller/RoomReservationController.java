package com.booking.room.controller;

import com.booking.room.exception.RoomReservationNotFound;
import com.booking.room.model.Room;
import com.booking.room.service.RoomReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class RoomReservationController {

    @Autowired
    Room room;

    @Autowired
    RoomReservationService roomReservationService;

    private static final Logger LOG = LoggerFactory.getLogger(RoomReservationController.class);

    @GetMapping("/test")
    public ModelAndView getStringTest() {
        ModelAndView modelAndView = new ModelAndView("home");
        room = roomReservationService.getStringTest();
        modelAndView.addObject("room",room);
        return modelAndView;
    }
    @Operation(summary = "Reserve a single room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A single room was reserved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Room.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Empty list",
                    content = @Content) })
    @PostMapping("/reservations")
    public ResponseEntity<String> saveRoom(@RequestBody Room room) throws RuntimeException {
        roomReservationService.saveRoom(room);
        return new ResponseEntity<>("Stored in db!",HttpStatus.OK);
    }

    @Operation(summary = "Updates a single room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A single room was reserved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Room.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Empty list",
                    content = @Content) })
    @PutMapping("/rooms")
    public ResponseEntity<String> updateRoom(@RequestBody Room room) throws RuntimeException {
        roomReservationService.updateRoom(room);
        return new ResponseEntity<>("Stored in db!",HttpStatus.OK);
    }

    @Operation(summary = "Feed no reserved rooms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Elements stored in db"),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Empty list",
                    content = @Content) })
    @PostMapping("/rooms")
    public ResponseEntity<String> feedDatabase() throws RuntimeException {
        roomReservationService.feedDatabase();
        LOG.info("DB feeded sucessfully {}", 200);
        return new ResponseEntity<>("All elements stored in db!",HttpStatus.OK);
    }

    @Operation(summary = "Get all rooms reserved or not")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get the list of rooms reserved or not",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Room.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Empty list",
                    content = @Content) })
    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getAllRooms() {
       List<Room> roomList =roomReservationService.getAllRooms();
       if(roomList.isEmpty()) {
           throw new RoomReservationNotFound("No rooms available!");
       }
       LOG.info("Get all room sucessfull {}", 200);
       return new ResponseEntity<>(roomList, HttpStatus.OK);
    }

    @Operation(summary = "Get either rooms reserved or not")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get rooms reserved or not",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Room.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Empty list",
                    content = @Content) })
    @GetMapping("/reservations")
    public ResponseEntity<List<Room>> getRoomsReserved(@RequestParam(value="reservation", required=true) Boolean reservation) {
        List<Room> roomList =roomReservationService.getRoomsReserved(reservation);
        if(roomList.isEmpty())
            throw new RoomReservationNotFound("Room Reservation List is empty");
        return new ResponseEntity<>(roomList, HttpStatus.OK);
    }

    //We can have the same method mapping but in order to differentiate we need to specify
    //that we are going to receive two params.
    @Operation(summary = "Get rooms reserved given a range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get the list of rooms reserved",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Room.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Empty list",
                    content = @Content) })
    @GetMapping(value="/reservations", params={"id1", "id2"})
    public ResponseEntity<List<Room>> getRoomsByRange(@Parameter(description = "lowest id of room to be searched") @RequestParam(value="id1", required=true) Integer id1,
                                                      @Parameter(description = "highest id of room to be searched") @RequestParam(value="id2", required=true) Integer id2) {
        List<Room> roomList = roomReservationService.getRoomsByRange(id1,id2);
        if(roomList.isEmpty())
            throw new RoomReservationNotFound("Room Reservation List is empty");
        return new ResponseEntity<>(roomList, HttpStatus.OK);
    }
}
