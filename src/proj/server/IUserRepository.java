package proj.server;

public interface IUserRepository {
    User findByUsername(String username);
    //List<Urun> tumUrunler();
    void add(User user);
    void delete(String username);
}
