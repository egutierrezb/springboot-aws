package com.booking.room.repository;

import com.booking.room.model.Room;
//import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, Integer> {

    @Query(value = "select * from rooms where reservation=true and id between ?1 and ?2", nativeQuery = true)
    List<Room> findRoomsFromToId(Integer id1, Integer id2);

    @Query(value = "select * from rooms where reservation=?1", nativeQuery = true)
    List<Room> findRoomsReserved(Boolean reservation);
}
