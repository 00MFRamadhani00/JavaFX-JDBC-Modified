/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafx_jdbc;

/**
 *
 * @author kusan
 */
public class Mahasiswa {
    private String npm;
    private String nama;
    private String alamat;

    public Mahasiswa(String npm, String nama, String alamat) {
        this.npm = npm;
        this.nama = nama;
        this.alamat = alamat;
    }

    public String getNpm() {
        return npm;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }
    
}
