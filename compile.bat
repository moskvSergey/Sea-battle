javac .\SeaBattle\Server.java

javac .\SeaBattle\model\Model.java

javac --module-path .\Libs\javafx-sdk-19\lib --add-modules=javafx.controls,javafx.fxml --class-path ".\SeaBattle" .\SeaBattle\controller\Controller.java
javac --module-path .\Libs\javafx-sdk-19\lib --add-modules=javafx.controls,javafx.fxml --class-path ".\SeaBattle" .\SeaBattle\Main.java

pause