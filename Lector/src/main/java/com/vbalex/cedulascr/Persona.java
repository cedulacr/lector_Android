package com.vbalex.cedulascr;

/**
 * Created by alex on 2/19/17.
 */

public class Persona {
    private String cedula;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private char genero;
    private String fechaNacimiento;
    private String fechaVencimiento;

    Persona() {

    }

    Persona(String _ced, String _nom, String _ape1, String _ape2,char _gen, String _fecN,String _fecV){
        this.cedula = _ced;
        this.nombre = _nom;
        this.apellido1 = _ape1;
        this.apellido2 = _ape2;
        this.genero = _gen;
        this.fechaNacimiento = _fecN;
        this.fechaVencimiento = _fecV;
    }

    public void setCedula(String _ced) {
        this.cedula = _ced;
    }

    public void setNombre(String _nom) {
        this.nombre = _nom;
    }

    public void setApellido1(String _ape1) {
        this.apellido1 = _ape1;
    }

    public void setApellido2(String _ape2) {
        this.apellido2 = _ape2;
    }

    public void setGenero(char _gen) {
        this.genero = _gen;
    }

    public void setFechaNacimiento(String _fecN) {
        this.fechaNacimiento = _fecN;
    }

    public void setFechaVencimiento(String _fecV) {
        this.fechaVencimiento = _fecV;
    }

    public String getCedula() {
        return this.cedula;
    }
    public String getNombre() {
        return this.nombre;
    }
    public String getApellido1() {
        return this.apellido1;
    }
    public String getApellido2() {
        return this.apellido2;
    }
    public char getGenero() {
        return this.genero;
    }
    public String getFechaNacimiento() {
        return this.fechaNacimiento;
    }
    public String getFechaVencimiento() {
        return this.fechaVencimiento;
    }
    @Override
    public String toString(){
        return this.cedula +" "+
                this.apellido1 +" "+
                this.apellido2 +" "+
                this.nombre +" "+
                this.fechaNacimiento +" "+
                this.fechaVencimiento;
    }
}
