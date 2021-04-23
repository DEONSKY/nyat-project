package proj.server;

public class User {
    private int id;
    private String username;
    private String password;
    ISubject publisher;

    public User(Builder builder){
        this.id=builder.id;
        this.username=builder.username;
        this.password=builder.password;
        this.publisher = builder.publisher;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        publisher.notify("{\"isim\":"+this.username+",\"sifre\":"+this.password+"}");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class Builder {

        private int id;
        private String username, password;
        ISubject publisher;

        public Builder() {
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder publisher(ISubject publisher){
            this.publisher = publisher;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}
