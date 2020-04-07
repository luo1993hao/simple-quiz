package entities;

import preparedstatement.crud.PreparedStatementUpdate;

public class Ticket {
    private int spotId;
    private String carNumber;
    private String carparkId;

    public Ticket(int spotId, String carNumber, String carparkId) {
        this.spotId = spotId;
        this.carNumber = carNumber;
        this.carparkId = carparkId;
    }

    public Ticket() {
    }

    public int getSpotId() {
        return spotId;
    }

    public String getCarparkId() {
        return carparkId;
    }

    public void saveToDb() {
        String insertSql = "INSERT INTO ticket (spot_number, car_number, carpark_id) VALUES (?, ?, ?)";
        PreparedStatementUpdate.update(insertSql, this.spotId, this.carNumber, this.carparkId);
    }

    @Override
    public String toString() {
        String message = "已将您的车牌号为" + this.carNumber
                + "的车辆停到" + this.carparkId + "停车场" + this.spotId + "号车位, 停车券为："
                + this.carparkId + "," + this.spotId + "," + this.carNumber + ", 请您妥善保存！";
        System.out.println(message);
        return this.carparkId + "," + this.spotId + "," + this.carNumber;
    }
}
