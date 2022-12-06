import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

 
public class Main extends Application{
     
    public static void main(String[] args) {
        		
        Application.launch(args);
		
    }
     
    @Override
    public void start(Stage stage) throws Exception {
         
        Parent root = FXMLLoader.load(Main.class.getResource("view/View.fxml")); 
        Scene scene = new Scene(root);
         
        stage.setScene(scene);
         
        stage.setTitle("Sea Battle");
        stage.setWidth(900);
        stage.setHeight(500);
         
        stage.show();
    }
}