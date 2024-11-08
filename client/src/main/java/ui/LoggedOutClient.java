package ui;

public class LoggedOutClient {
    public LoggedOutClient() {

    }

    public String eval(String line) {
        //need to parse the line
        var tokens = line.toLowerCase().split(" ");
        if (tokens.length == 0) {
            return "";
        } else {
            //cases are help, quit, login, register
            String cmd = tokens[0];
            switch (cmd) {
                case "help":
                    loggedOutHelp();
                    break;
                case "quit":
                    return "quit";
                break;

            }
        }
    }

    public void loggedOutHelp() {
        String[] helpLines = {
                "register <USERNAME> <PASSWORD> <EMAIL> - To create an account.",
                "login <USERNAME> <PASSWORD> - To play chess",
                "quit - stop playing chess",
                "help - list all possible commands"
        };
        for (String line : helpLines) {
            System.out.println(line);
        }
    }
}
