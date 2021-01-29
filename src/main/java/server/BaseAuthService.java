package server;

import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements  AuthService{

    private List<Entry> entries;

    public BaseAuthService() {
        this.entries = new ArrayList<>();
        entries.add(new Entry("ivan", "password", "Neivanov"));
        entries.add(new Entry("sharik", "gav", "Auf"));
        entries.add(new Entry("otvertka", "shurup", "Kruchu-verchu"));
    }

    @Override
    public void start() {
        System.out.println("Autorized service start");
    }

    @Override
    public void stop() {
        System.out.println("Autorized service stop");
    }

    @Override
    public String getNickByLoginAndPass(String login, String pass) {
        for (Entry entry:
             entries) {
            if (login.equals(entry.login) && pass.equals(entry.password)){
                return entry.nick;
            }
        }

        return null;
    }

    private class Entry{
        private String login;
        private String password;
        private String nick;

        public Entry(String login, String password, String nick) {
            this.login = login;
            this.password = password;
            this.nick = nick;
        }
    }
}
