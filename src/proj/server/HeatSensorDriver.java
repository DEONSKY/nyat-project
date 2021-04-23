package proj.server;

import java.util.Random;

public class HeatSensorDriver implements IHeatSensor,IObserver {

    Random rand= new Random();
    ISubject publisher;
    @Override
    public int getHeat() {
        publisher.notify("Çalıştı");
        return rand.nextInt(1000);

    }
    @Override
    public void update(String mesaj) {
        System.out.println("Abone1 e gelen mesaj->" + mesaj);
    }
}
