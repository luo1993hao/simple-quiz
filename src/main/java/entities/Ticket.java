package entities;

import com.google.common.base.Splitter;
import exception.InvalidTicketException;
import preparedstatement.crud.PreparedStatementUpdate;

import java.util.List;

public class Ticket {
    private int spotNumber;
    private String carNumber;
    private String carparkId;

    public Ticket(int spotNumber, String carNumber, String carparkId) {
        this.spotNumber = spotNumber;
        this.carNumber = carNumber;
        this.carparkId = carparkId;
    }

    public Ticket() {
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getCarparkId() {
        return carparkId;
    }

    void save() {
        String insertSql = "INSERT INTO ticket (spot_number, car_number, carpark_id) VALUES (?, ?, ?)";
        PreparedStatementUpdate.update(insertSql, this.spotNumber, this.carNumber, this.carparkId);
    }

    public void delete() {
        String deleteSql = "DELETE FROM ticket WHERE car_number = ?";
        PreparedStatementUpdate.update(deleteSql, this.carNumber);
    }

    public static Ticket parseTicket(String ticketString) {
        List<String> ticketFieldList = Splitter.on(",")
                .trimResults().splitToList(ticketString);
        int ticketFieldSize = 3;
        if (ticketFieldList.size() != ticketFieldSize) {
            throw new InvalidTicketException("invalid input ticket");
        }
        return new Ticket(Integer.parseInt(ticketFieldList.get(1)), ticketFieldList.get(2), ticketFieldList.get(0));
    }

    @Override
    public String toString() {
        String message = "已将您的车牌号为" + this.carNumber
                + "的车辆停到" + this.carparkId + "停车场" + this.spotNumber + "号车位, 停车券为："
                + this.carparkId + "," + this.spotNumber + "," + this.carNumber + ", 请您妥善保存！";
        System.out.println(message);
        return this.carparkId + "," + this.spotNumber + "," + this.carNumber;
    }
}
