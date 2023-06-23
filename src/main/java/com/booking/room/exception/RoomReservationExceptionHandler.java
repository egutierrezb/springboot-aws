package com.booking.room.exception;

import com.google.common.base.Throwables;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

@ControllerAdvice
public class RoomReservationExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RoomReservationExceptionHandler.class);

    @ExceptionHandler(RoomReservationBadRequest.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> apiExceptionBadRequest(HttpServletRequest req, RuntimeException exception){
        LOG.error("Bad request to the API " + exception.getMessage());
        return createResponseBody(req, exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoomReservationNotFound.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> apiExceptionNotFound(HttpServletRequest req, RuntimeException exception){
        //LOG.error("Not found " + exception.getMessage());
        ResponseEntity responseEntity = createResponseBody(req, exception, HttpStatus.NOT_FOUND);
        LOG.error(responseEntity.getBody().toString());
        return responseEntity;
    }

    private ResponseEntity<Map<String,Object>> createResponseBody(HttpServletRequest req, RuntimeException exception, HttpStatus httpStatus) {
        Map<String, Object> map = new HashMap<>();
        map.put("url", req.getRequestURL().toString());
        map.put("timestamp", LocalDateTime.now().toString());
        map.put("status", httpStatus);
        String message = null;
        if(Throwables.getRootCause(exception).getMessage()!=null) {
            message = Throwables.getRootCause(exception).getMessage();
        }
        else if(exception.getMessage()!=null) {
            message = exception.getMessage();
        }
        else if(exception.getClass().getName()!=null) {
            message = exception.getClass().getName();
        }
        map.put("exception", message);
        return new ResponseEntity<>(map,null,httpStatus);

    }

}
