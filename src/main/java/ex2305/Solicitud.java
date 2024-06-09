package ex2305;

import java.util.StringJoiner;

public class Solicitud {
    private String asignatura;
    private int diaSem;
    private int franja;
    private int lab;

    public Solicitud(String nom, int dSem, int hora){
        if (diaSem >0 && diaSem <8 && hora > 0 && hora < 4){
            asignatura = nom;
            diaSem = dSem;
            franja = hora;
            lab = -1;
        }else {
            throw new LabException("Args no válidos");
        }
    }

    public String getAsignatura() {
        return asignatura;
    }

    public int getDiaSem() {
        return diaSem;
    }

    public int getFranja() {
        return franja;
    }

    public int getLab() {
        return lab;
    }

    public void setDiaSem(int diaSem) {
        if (diaSem > 0 && diaSem < 8){
            this.diaSem = diaSem;
        }else {
            throw new LabException("Args no válidos");
        }
    }

    public void setFranja(int franja) {
        if (franja > 0 && franja < 4){
            this.franja = franja;
        }else {
            throw new LabException("Args no válidos");
        }
    }

    public void setLab(int lab) {
        this.lab = lab;
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner("(" + ", " + ", " + ", " + ")" );
        stringJoiner.add(asignatura);
        stringJoiner.add(String.valueOf(diaSem));
        stringJoiner.add(String.valueOf(franja ));
        stringJoiner.add(String.valueOf( lab));
        return stringJoiner.toString();
    }
}
