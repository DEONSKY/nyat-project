package proj.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientRunner {

    public static void main(String[] args) throws IOException {

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        final String HOST = "localhost";
        final int PORT = 8001;
        try {
            socket = new Socket(HOST, PORT); // "localhost" ya da sunucu IP adresi
            // input stream ve output stream olusuyor
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Sunucu bulunamadi");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("I/O exception:" + e.getMessage());
            System.exit(1);
        }
        System.out.println("Sunucuya baglanildi.");
        // klavyeden girdi: stdIn
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        System.out.println("Lütfen Komutları Çalıştımak için önce(Login:username:password) Login olun.(default->Login:asd:123)");
        System.out.println("Kayıtlı hesabınız yoksa Register:username:password formatında kaydetebilirsiniz");
        System.out.println("(baglantiyi kesmek icin: end) ...");
        boolean isLogged=false;



        while (!(userInput = stdIn.readLine()).equals("end")) {
            if(userInput.equals("help")){
                System.out.println(
                        "Sunucuya Login olmak için Login:username:password formatında bilgilerinizi girin\n" +
                        "Login olduktan sonra kullanılabilecek komutlar\n"+
                        "getHeat = Observer yardımı ile güncellenen son değeri getirir\n"+
                        "setCoolerOn = Soğutucuyu açar\n"+
                        "setCoolerOff = Soğutucuyu Kapatır\n"+
                        "getCoolerStatus = Soğutucunun açık yada kapalı olduğu bilgisini getirir\n" +
                                "end = Bağlantıyı sonlandırır"
                );
            }
            else if(isLogged){
                out.println(userInput);
                System.out.println("Sunucudan gelen: " + in.readLine());
            }else{

                out.println(userInput);

                if(in.readLine().equals("true")){
                    isLogged=true;
                    System.out.println("Sunucudan gelen: Yetkilendirme Başarılı");
                }else{
                    System.out.println("Sunucudan gelen: Yetkilendirme Başarısız");
                    System.out.println("Lütfen Komutları Çalıştımak için önce(Login:username:password) Login olun.");
                };
            }



        }
        out.println(userInput);
        System.out.println("Baglanti kesiliyor...");

        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }
}
