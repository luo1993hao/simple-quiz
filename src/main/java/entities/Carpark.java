package entities;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import preparedstatement.crud.PreparedStatementQuery;
import preparedstatement.crud.PreparedStatementUpdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Carpark {
    private String id;
    private int space;
    private String spotNumber;


    public Carpark(String id, int space) {
        this.id = id;
        this.space = space;
        this.spotNumber = parseSpaceToSpotNumber();
    }


    public Carpark() {
    }

    public String getId() {
        return id;
    }

    public int getSpace() {
        return space;
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public void update() {
        String sql = "UPDATE carpark SET spotNumber = ? WHERE id = ?";
        PreparedStatementUpdate.update(sql, this.spotNumber, this.id);
    }

    public static Carpark findById(String id) {
        String querySql = "SELECT space,  spotNumber FROM carpark WHERE id = ?";
        return PreparedStatementQuery.queryInfo(Carpark.class, querySql, id);
    }

    public static List<Carpark> findAll() {
        String sql = "SELECT * FROM carpark";
        return PreparedStatementQuery.queryInfoList(Carpark.class, sql);

    }


    public void updateSpotNumber(Integer spotNumber) {
        List<String> spotNumberList = new ArrayList<>(Splitter.on(",")
                .trimResults().splitToList(this.spotNumber));
        spotNumberList.add(String.valueOf(spotNumber));
        this.spotNumber = Joiner.on(",").join(spotNumberList);
    }

    Ticket park(String carNumber, String spotNumber) {
        //find minspot
        int minSpot = this.findMinSpotNumber(spotNumber);
        //remove minSpot
        String nowSpotNumber = this.removeMinSpotNumber(spotNumber, minSpot);
        //update carParkSpotNumber
        this.updateCarPark(nowSpotNumber);
        //new ticket and return
        Ticket ticket = new Ticket(minSpot, carNumber, this.id);
        ticket.save();
        return ticket;
    }

    Boolean isAvailable() {
        return this.spotNumber.length() > 0;
    }

    private int findMinSpotNumber(String spotNumber) {
        List<Integer> spotNumberList;
        if (spotNumber.length() == 1) {
            spotNumberList = Lists.newArrayList(Integer.parseInt(spotNumber));
        } else {
            spotNumberList = Splitter.on(",")
                    .trimResults().splitToList(spotNumber)
                    .stream().map(Integer::parseInt).collect(Collectors.toList());
        }

        return Collections.min(spotNumberList);
    }

    private String removeMinSpotNumber(String spotNumber, Integer minSpot) {
        List<Integer> spotNumberList = Splitter.on(",")
                .trimResults().splitToList(spotNumber)
                .stream().map(Integer::parseInt).collect(Collectors.toList());
        spotNumberList.remove(minSpot);
        return Joiner.on(",").join(spotNumberList);
    }


    private void updateCarPark(String spotNumber) {
        String updateCarParkSql = "UPDATE CARPARK SET spotNumber = ? where id = ?";
        PreparedStatementUpdate.update(updateCarParkSql, spotNumber, this.id);

    }

    private String parseSpaceToSpotNumber() {
        List<Integer> sportNumberList = new ArrayList<>();
        for (int i = 1; i < space + 1; i++) {
            sportNumberList.add(i);
        }
        return Joiner.on(",").join(sportNumberList);
    }


}
