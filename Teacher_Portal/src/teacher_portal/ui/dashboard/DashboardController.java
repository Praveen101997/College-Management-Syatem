
package teacher_portal.ui.dashboard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import teacher_portal.database_connection.DB;
import teacher_portal.database_connection.DaoTeacherInformation;
//import student_portal.ui.alert.alertpane;

public class DashboardController implements Initializable {

    @FXML
    private Label dashboardmain;
    @FXML
    private Label userid;
    @FXML
    private Label program;
    @FXML
    private Label discipline;
    @FXML
    private Label stage;
    @FXML
    private Label emailid;
    @FXML
    private Label dob;
    @FXML
    private Label fathername;
    @FXML
    private Label mothername;
    @FXML
    private Label address;
    @FXML
    private Label phoneno;
    @FXML
    private ImageView studentphoto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String usrid = teacher_portal.database_connection.retrieveSingleDetails.userid;
        String name = teacher_portal.database_connection.retrieveSingleDetails.name;
        try {
            retievestudentinfo(usrid);
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        dashboardmain.setText(name + " Profile");

    }

    void retievestudentinfo(String sid) throws IOException {
        if (sid.equals("")) {
            System.out.println("Userid can't be blank");
        } else {
            System.out.println("Executing Script");
            if (DaoTeacherInformation.checkData(sid)) {
                try {
                    int status = 0;
                    System.out.println("Connecting to database");
                    Connection con = DB.getConnection();
                    PreparedStatement ps = con.prepareStatement("select * from teacherinformation where userid=?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    System.out.println("Connected");
                    ps.setString(1, sid);
                    ResultSet rs = ps.executeQuery();
                    rs.beforeFirst();
                    rs.next();
                    userid.setText(rs.getString(1));
                    emailid.setText(rs.getString(3));
                    System.out.println("date retrieved");
                    dob.setText(rs.getString(4));
                    System.out.println("set");
                    fathername.setText(rs.getString(5));
                    mothername.setText(rs.getString(6));
                    address.setText(rs.getString(7));
                    phoneno.setText(String.valueOf(rs.getInt(8)));
                    byte[] pic = rs.getBytes(9);
                    InputStream myInputStream = new ByteArrayInputStream(pic);
                    Image img = new Image(myInputStream);
                    studentphoto.setImage(img);
                    con.close();
                } catch (SQLException ex) {
                    System.out.println("Exception Caught");
                }
            } else {
                System.out.println("Error: Record Not Found");
            }
        }
    }

    @FXML
    private void handleupdateprofile(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/teacher_portal/ui/dashboard/updateprofile.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Student Portal");
            stage.setScene(new Scene(parent));
            stage.show();
        
    }

}
