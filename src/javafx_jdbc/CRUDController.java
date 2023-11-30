package javafx_jdbc;

import db.DBHelper;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


public class CRUDController implements Initializable{

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnInsert;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Mahasiswa, String> colAlamat;

    @FXML
    private TableColumn<Mahasiswa, String> colNama;

    @FXML
    private TableColumn<Mahasiswa, Integer> colNpm;

    @FXML
    private TextField tfAlamat;

    @FXML
    private TextField tfNama;

    @FXML
    private TextField tfNpm;

    @FXML
    private TableView<Mahasiswa> tvData;

    @FXML
    private void handleButtonAction(ActionEvent event) {        
        
        if(event.getSource() == btnInsert){
            insertRecord();
        }else if (event.getSource() == btnUpdate){
            updateRecord();
        }else if(event.getSource() == btnDelete){
            deleteRecord();
        }
            
    }
    
    public ObservableList<Mahasiswa> getDataMahasiswa(){
        ObservableList<Mahasiswa> mhs = FXCollections.observableArrayList();
        Connection conn = DBHelper.getConnection();
        String query = "SELECT * FROM `mahasiswa`";
        Statement st;
        ResultSet rs;
        
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Mahasiswa temp;
            while(rs.next()){
                temp = new Mahasiswa(rs.getString("npm"), rs.getString("nama"), rs.getString("alamat"));
                mhs.add(temp);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return mhs;
    }
    
    public void showMahasiswa() {
        ObservableList<Mahasiswa> list = getDataMahasiswa();

        colNpm.setCellValueFactory(new PropertyValueFactory<>("npm"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        tvData.setItems(list);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showMahasiswa();
        
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        tvData.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tfNpm.setText(newSelection.getNpm());
                tfNama.setText(newSelection.getNama());
                tfAlamat.setText(newSelection.getAlamat());

                btnInsert.setDisable(true);
                btnUpdate.setDisable(false);
                btnDelete.setDisable(false);
            } else {
                tfNpm.clear();
                tfNama.clear();
                tfAlamat.clear();

                btnInsert.setDisable(false);
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
            }
        });
    }
    
    private void insertRecord(){
        // Pastikan semua field tidak kosong
        if (isEmptyTextField()) {
            showAlert("Peringatan", "Isian Tidak Lengkap", "Silakan isi semua kolom.");
            return;
        }
        
        String query = "INSERT INTO `mahasiswa` VALUES ('" + tfNpm.getText() + "','" + tfNama.getText() + "','" + tfAlamat.getText() + "')";
        update(query);
        showMahasiswa();
        
        tfNpm.clear();
        tfNama.clear();
        tfAlamat.clear();
    }
    private void updateRecord(){
        Mahasiswa selectedMahasiswa = tvData.getSelectionModel().getSelectedItem();

        if (selectedMahasiswa != null) {
            // Pastikan nama dan alamat tidak kosong
            if (tfNama.getText().isEmpty() || tfAlamat.getText().isEmpty()) {
                showAlert("Peringatan", "Isian Tidak Lengkap", "Silakan isi nama dan alamat.");
                return;
            }

            String query = "UPDATE  `mahasiswa` SET nama  = '" + tfNama.getText() + "', alamat = '" + tfAlamat.getText() + "' WHERE npm = '" + selectedMahasiswa.getNpm() + "'";
            update(query);
            showMahasiswa();

            // Setelah berhasil update, kosongkan TextField dan kembalikan keadaan tombol
            tfNpm.clear();
            tfNama.clear();
            tfAlamat.clear();
            btnInsert.setDisable(false);
            btnUpdate.setDisable(true);
        } else {
            showAlert("Peringatan", "Pilih Baris", "Pilih baris yang ingin diupdate.");
        }
    }
    private void deleteRecord(){
        Mahasiswa selectedMahasiswa = tvData.getSelectionModel().getSelectedItem();

        if (selectedMahasiswa != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi Hapus");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin ingin menghapus data ini?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    String query = "DELETE FROM `mahasiswa` WHERE npm ='" + selectedMahasiswa.getNpm() + "'";
                    update(query);
                    showMahasiswa();
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Pilih baris yang ingin dihapus.");
            alert.showAndWait();
        }
    }
    
    private void update(String query) {
        Connection conn = DBHelper.getConnection();
        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    private boolean isEmptyTextField() {
        return tfNpm.getText().isEmpty() || tfNama.getText().isEmpty() || tfAlamat.getText().isEmpty();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
