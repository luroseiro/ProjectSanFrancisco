package sample;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AlertaCarregamento {

    public static void display(String titulo, String mensagem) {

        //cria e define caixa de alerta
        Stage janela = new Stage();
        janela.setTitle(titulo);
        janela.setMinWidth(350);
        janela.setMinHeight(150);

        //texto para caixa de aleta e botão
        Label label = new Label();
        label.setText(mensagem);
        Button botao = new Button("Ok");
        botao.setMinWidth(50);
        botao.setOnAction(e -> janela.close());

        //empilhando
        VBox vBox = new VBox(25);
        vBox.getChildren().addAll(label, botao);
        vBox.setAlignment(Pos.CENTER);

        //Exibe alerta
        Scene cena = new Scene(vBox);
        janela.setScene(cena);
        janela.showAndWait();
    }

}
