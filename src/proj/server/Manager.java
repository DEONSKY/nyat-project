package proj.server;

public class Manager {
    ManagerStatus managerStatus;
    private final IUserRepository userRepository;
    private final IHeatSensor heatSensor;
    private final ICooler cooler;


    public Manager(IUserRepository userRepository, IHeatSensor heatSensor, ICooler cooler) {
        this.userRepository = userRepository;
        this.heatSensor = heatSensor;
        this.cooler = cooler;
    }

    public boolean Login(String username,String password){
        User user =userRepository.findByUsername(username);
        if(user!=null&&user.getPassword().equals(password)){
            return true;
        }
        return false;
    }

    public int getHeat(){
        return heatSensor.getHeat();
    }
    public void setCoolerOn(){
        cooler.setCoolerOn();
    }
    public void setCoolerOff(){
        cooler.setCoolerOff();
    }
    public boolean getCoolerStatus(){
        return cooler.getCoolerStatus();
    }



}
