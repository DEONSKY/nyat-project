package proj.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserRepositoryPostgreSql implements IUserRepository {

    private Connection connect(){

        Connection conn=null;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/nyat2",
                    "postgres", "running-away-222");
            if (conn != null)
                System.out.println("Veritabanına bağlandı!");
            else
                System.out.println("Bağlantı girişimi başarısız!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    @Override
    public User findByUsername(String username){
        System.out.println("ürün aranıyor...");
        User user=null;

        String sql= "SELECT *  FROM public.user WHERE username="+"'"+ username+"'";
        System.out.println(sql);

        Connection conn=this.connect();
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //***** Bağlantı sonlandırma *****
            conn.close();

            int urunNo;
            String usernameFound;
            String passwordFound;



            String soyadi;

            while(rs.next())
            {
                //urunNo  = rs.getInt("urunNo");
                usernameFound = rs.getString("username");
                passwordFound = rs.getString("password");

                user=new User.Builder().username(usernameFound).password(passwordFound).build();
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void add(User user) {
        System.out.println("ürünü kaydediyor..."+user);
        //String sql= "INSERT INTO  \"Urun\" (\"urunNo\",\"adi\",\"birimFiyati\",\"stokMiktari\") VALUES("+urun.getUrunNo()+",\'"+urun.getAdi()+"\',"+urun.getBirimFiyati()+","+urun.getStokMiktari()+")";

        String sql= "INSERT INTO  \"user\" (\"username\",\"password\") VALUES(\'"+user.getUsername()+"\',"+user.getPassword()+")";

        Connection conn=this.connect();

        try
        {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            //***** Bağlantı sonlandırma *****
            conn.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String username) {
        System.out.println("ürün siliniyor...");

        String sql= "DELETE FROM \"user\" WHERE \"username\"="+username;

        Connection conn=this.connect();
        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            //***** Bağlantı sonlandırma *****
            conn.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
