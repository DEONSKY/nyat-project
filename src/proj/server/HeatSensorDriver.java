package proj.server;

import java.util.Random;
import java.util.TimerTask;


public class HeatSensorDriver extends TimerTask implements IHeatSensor {

    Random rand= new Random();
    ISubject publisher = new Publisher() ;
    int heat;

    HeatSensorDriver(){
    }
    @Override
    public void addSubscriber(SubscriberUser subscriberUser){
        publisher.attach(subscriberUser);
        System.out.println("Heat Sub added");
    }

    @Override
    public void run() {
        int newHeat = rand.nextInt(1000);
        publisher.notify("New heat : "+newHeat);
        this.heat= newHeat;
    }

    @Override
    public int getHeat() {

        return this.heat;

    }



}
