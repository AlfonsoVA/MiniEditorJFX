import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture; 
import javafx.scene.text.FontWeight; 
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import java.awt.image.BufferedImage;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Editor extends Application{
    public static void main(String[]args){
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Editor de Imagenes basico");        
        FileChooser fChooser;                        
        fChooser = new FileChooser();
        Button btnImg = new Button("Abrir Imagen");
        Button btnImgSave = new Button("Guardar Imagen");
        Button btnGN = new Button("Girar 90 grados");        
        Button btnEG = new Button("Escala de grises");
        Label imgStatus = new Label("Bienvenido, seleccione una imagen para empezar");
        imgStatus.setFont(Font.font("lucida console", FontWeight.BLACK, FontPosture.REGULAR, 15));
        fChooser.setTitle("Elegir Imagen");
        ImageView imgView = new ImageView();
        fChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
        
        btnImg.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                File fimg = fChooser.showOpenDialog(primaryStage);
                if(fimg == null){                    
                    return;
                }                
                Image img = new Image(fimg.toURI().toString());                
                imgView.setImage(img);
                imgView.setX(25);
                imgView.setY(10);
                imgView.setFitWidth(500);
                imgView.setFitHeight(400);
                imgView.setPreserveRatio(true);
                imgStatus.setText("Imagen cargada");
            }
        });

        btnGN.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                if(imgView.getImage() == null){
                    return;
                }
                imgView.setRotate(imgView.getRotate() + 90);
            }
        });

        btnEG.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                if(imgView.getImage() != null){
                    ColorAdjust geAdjust = new ColorAdjust();
                    geAdjust.setSaturation(-1);
                    imgView.setEffect(geAdjust);                                   
                }                
            }
        });

        btnImgSave.setOnAction(s -> guardaImagen(imgView, imgStatus, primaryStage));

        BorderPane bPane = new BorderPane();         
        HBox hButtons = new HBox();
        HBox hLabel = new HBox();        
        hButtons.setPadding(new Insets(10));
        hLabel.setPadding(new Insets(20));        
        hLabel.setAlignment(Pos.BASELINE_CENTER);
        hButtons.setAlignment(Pos.BASELINE_CENTER);
        hLabel.getChildren().add(imgStatus);
        hButtons.getChildren().add(btnImg);
        hButtons.getChildren().add(btnImgSave);
        hButtons.getChildren().add(btnGN);        
        hButtons.getChildren().add(btnEG);
        bPane.setBottom(hButtons);              
        bPane.setCenter(imgView);                                   
        bPane.setTop(hLabel);   
        primaryStage.setScene(new Scene(bPane, 520, 640));
        primaryStage.show();
    }

    public void guardaImagen(ImageView imgView, Label status, Stage primaryStage){
        if(imgView.getImage() != null){
            try{                   
                DirectoryChooser dirChooser = new DirectoryChooser();
                dirChooser.setTitle("Seleccionar carpeta de guardado");                
                File fimgsave = dirChooser.showDialog(primaryStage);                                 
                if(fimgsave != null){
                    TextInputDialog inputName = new TextInputDialog("Sin Titulo");             
                    inputName.setHeaderText("Ingrese el nombre de la nueva imagen");                    
                    inputName.showAndWait();
                    File fimg = new File(fimgsave.toURI().toString().replace("file:/", "") + inputName.getEditor().getText() + ".png");
                    BufferedImage buffimg = SwingFXUtils.fromFXImage(imgView.snapshot(null, null), null);        
                    ImageIO.write(buffimg, "png", fimg);
                    status.setText("Imagen guardada");
                    Desktop desk = Desktop.getDesktop();
                    File fExplorer = new File(fimgsave.toURI().toString().replace("file:/", ""));
                    desk.open(fExplorer);
                }                
            }catch(IOException ioe){
                System.out.println("Imagen no pudo ser guardada: \n" + ioe.getMessage());
            }                                            
        }            
    }
}