package entities;

import exception.InvalidInitInfoException;
import exception.ParkingLotFullException;
import preparedstatement.crud.PreparedStatementUpdate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Manager {
    private List<Carpark> carparkList;

    public Manager(List<Carpark> carparkList) {
        this.carparkList = carparkList;
    }

    public List<Carpark> parseInitialInput(String initInfo) {
        List<String> initialInfo = Arrays.asList(initInfo.split(","));
        for (String info : initialInfo) {
            String id = info.substring(0, 1);
            int space = Integer.parseInt(info.substring(2));
            checkInitInfo(id, space);
            Carpark carpark = new Carpark(id, space);
            this.carparkList.add(carpark);
        }
        return this.carparkList;
    }

    public static void checkInitInfo(String id, int space) {
        if (id.equals("A") & space > 8) {
            throw new InvalidInitInfoException("停车场初始数据有误，请重新输入");
        } else if (id.equals("B") & space > 10) {
            throw new InvalidInitInfoException("停车场初始数据有误，请重新输入");
        }
    }

    public static void storeDatabase(List<Carpark> carparks) {
        String sql = "INSERT INTO carpark VALUES (?, ?,?)";
        for (Carpark carpark : carparks) {
            PreparedStatementUpdate.update(sql, carpark.getId(), carpark.getSpace(), carpark.getSpotNumber());
        }
    }

    public Ticket managePark(String carNumber) {
        Optional<Carpark> firstAvailableCarpark = carparkList.stream()
                .filter(Carpark::isAvailable).findFirst();
        if (firstAvailableCarpark.isPresent()) {
            return firstAvailableCarpark.get().park(carNumber, firstAvailableCarpark.get().getSpotNumber());
        }
        throw new ParkingLotFullException("非常抱歉，由于车位已满，暂时无法为您停车！");
    }
}
