package form;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;


public class Login {
    private JButton login;
    private JPanel loginPanel;
    private JTextField usernameText;
    private JButton register;
    private JPasswordField passwordText;
    public static Connection conn1 = null;
    public static int logged;
    JFrame jFrame = new JFrame();

    public Login() {
        login.addActionListener(event -> {

            ArrayList<Integer> id = new ArrayList<>();
            ArrayList<String> username = new ArrayList<>();
            ArrayList<String> password = new ArrayList<>();
            try{
                Statement stmt = conn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery("select * from Users");


                while(rs.next()){
                    id.add(rs.getInt("Id"));
                    username.add(rs.getString("Username"));
                    password.add(rs.getString("Password"));
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            if(verifica(usernameText.getText(),passwordText.getText(),id,username,password) > -1){
                System.out.println(verifica(usernameText.getText(),passwordText.getText(),id,username,password));
                logged = verifica(usernameText.getText(),passwordText.getText(),id,username,password);
                JOptionPane.showMessageDialog(null, "Login effettuato con successo");
                JFrame frame = new JFrame("Calcolatrice");
                frame.setContentPane(new Calcolatrice().panel1);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
            else{
                JOptionPane.showMessageDialog(jFrame, "Username o password sbagliati");
            }
        });
        register.addActionListener(e -> {
            try{
                String username = usernameText.getText();
                String password = passwordText.getText();
                if(!Objects.equals(password, "") && !Objects.equals(username, "")){

                    JOptionPane.showMessageDialog(jFrame, "Nuovo utente inserito");
                }
                else {
                    JOptionPane.showMessageDialog(jFrame, "Uno dei campi è vuoto");
                }

            } catch (Exception exception){
                exception.printStackTrace();
            }
        });
    }
    public int verifica(String u,String p,ArrayList<Integer> Id,ArrayList<String> Username,ArrayList<String>Password){
        boolean found = false;
        int ris = -1;
        int i = 0;
        while (!found && i < Id.size()){
            if(Objects.equals(Username.get(i), u) && Objects.equals(Password.get(i), p)){
                found = true;
                ris = Id.get(i);
            }
            i++;
        }
        return ris;
    }

    public static void main(String[] args) {

        try {
             conn1 = DriverManager.getConnection("jdbc:mysql://127.0.0.1/Calcolatrice", "root", "");
            if (conn1.isValid(5)) {
                System.out.println("Connection is successful.");
                JFrame frame = new JFrame("Login");
                frame.setContentPane(new Login().loginPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            } else {
                System.out.println("Connection is not valid.");
            }
        } catch (SQLException e) {
            System.out.println("Connection failed. Error message: " + e.getMessage());
        }
    }
}
