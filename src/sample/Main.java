package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) throws IOException {

        /*//Leitura de arquivos
        String fileName = "C:\\Users\\lf_ro\\Downloads\\test.txt";

        try {
            ReadFile file = new ReadFile(fileName);
            String[] aryLines = file.OpenFile();
        }
        catch (IOException e) {
            System.out.println("Erro ao ler arquivo!");
        }*/

        //Interface
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Project San Francisco");
        primaryStage.setScene(new Scene(root, 1100, 650));
        primaryStage.show();
    }
}
