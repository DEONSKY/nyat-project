package proj.server;

public class SubscriberUser implements IObserver{

    @Override
    public void update(String m) {
        System.out.println("Güncellenen deger->" + m);
    }

}
