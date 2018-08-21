import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
/**
 * Created by Nogah on 22/03/2018.
 */


public class GUIController {
    public javafx.scene.control.TextField txtfld_filePath;
    public javafx.scene.control.TextField txtfld_newFileName;
    public javafx.scene.control.Button btn_load;
    public javafx.scene.control.Button btn_startAndSave;
    String savingPath="";

    public void loadFile(ActionEvent actionEvent)
    {
        //user choose file - only word
        FileChooser choose = new FileChooser();
        choose.setTitle("Choose your file");
        choose.setInitialDirectory(new File("C:\\"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Word files (*.docx)", "*.docx");
        choose.getExtensionFilters().add(extFilter);
        File f = choose.showOpenDialog(null);
        String filePath = f.getPath();
        txtfld_filePath.setText(filePath);
        btn_startAndSave.setDisable(false);
    }

    public void startProccess(ActionEvent actionEvent)
    {
        try
        {

            if(txtfld_newFileName.getText().equals(""))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                //alert.setTitle("Finish Saving");
                alert.setHeaderText("Error");
                alert.setContentText("Please enter file name");
                alert.showAndWait();
            }
            else
            {
                DirectoryChooser dc=new DirectoryChooser();
                dc.setInitialDirectory((new File("C:\\")));
                File selectedFile=dc.showDialog(null);
                String s=selectedFile.getAbsolutePath();
                savingPath=s;

                //sent to proccess function
                FileProcess fp=new FileProcess();
                fp.LoadFile(txtfld_filePath.getText(),savingPath,txtfld_newFileName.getText());

                btn_startAndSave.setDisable(true);
                txtfld_filePath.clear();
                savingPath="";
                txtfld_newFileName.clear();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Finish Saving");
                //alert.setHeaderText("Error");
                alert.setContentText("Your Smart Summary is in your computer!");
                alert.showAndWait();
            }

        }
        catch (Exception e)
        {

        }
    }
}
