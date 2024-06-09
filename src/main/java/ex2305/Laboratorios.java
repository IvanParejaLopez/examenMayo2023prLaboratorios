package ex2305;

import com.sun.org.apache.bcel.internal.generic.IOR;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.security.Key;
import java.util.*;

public class Laboratorios {
    private int maxLabs;
    private List<Solicitud> solicitudes;
    private SortedSet<Solicitud> erroresDeAsignacion;
    private SortedMap<Integer, SortedMap<Integer, List<Solicitud>>> asignaciones; //diaSem, franja horaria

    public Laboratorios(int max){
        if (max < 1){
            throw new LabException("Arg no valido");
        }else {
            maxLabs = max;
        }
    }
    protected SortedSet<Solicitud> getErroresDeAsignacion(){
        return erroresDeAsignacion;
    }
    protected void addSolicitud(Solicitud solicitud){
        if (!solicitudes.contains(solicitud)){
            solicitudes.add(solicitud);
        }
    }
    public void addSolicitud(String args){
        try {
            try (Scanner scanner = new Scanner(args)){
                scanner.useDelimiter("\\s*[;]\\s*");
                String nom = scanner.next();
                int num = scanner.nextInt();
                int n = scanner.nextInt();
                solicitudes.add(new Solicitud(nom, num, n));
            }
        }catch (Exception e){
            throw new LabException(e.getMessage());
        }
    }
    public SortedSet<Solicitud> getSolicitudesOrdenadas(){
        SortedSet<Solicitud> sol = new TreeSet<>(Comparator.comparing(Solicitud::getAsignatura));
        sol.addAll(solicitudes);
        return sol;
    }
    public void asignarLabs(){
        erroresDeAsignacion.clear();
        asignaciones.clear();

        for (Solicitud solicitud: solicitudes){
            asignarLabASolicitud(solicitud);
        }
    }

    protected void asignarLabASolicitud(Solicitud solicitud){
        solicitud.setLab(-1);
        SortedMap<Integer, List<Solicitud>> lista = asignaciones.get(solicitud.getDiaSem());

        if (lista.get(solicitud.getFranja()).size() < maxLabs){
            solicitudes.add(solicitud);
            solicitudes.get(solicitudes.indexOf(solicitud)).setLab(solicitudes.indexOf(solicitud)); //nose
        }else {
            erroresDeAsignacion.add(solicitud);
        }
    }

    @Override
    public String toString() {
        return "Solicitudes: " + solicitudes + " Errores de Asignacion: "
                + erroresDeAsignacion + "Asignaciones: " + asignaciones;
    }
    public void cargarSolicitudesDeFichero(String fich) throws IOException {
        try {
            File file = new File(fich);
            try (Scanner scanner = new Scanner(file)){
                addSolicitud(scanner.next()); //ns si esta bn
            }catch (IOException E){
                throw new IOException(E.getMessage());
            }catch (Exception e){
                //no hacer nada
            }
        }catch (IOException e){
            throw new IOException(e.getMessage());
        }
    }
    public void guardarAsignacionesEnFichero(String fich) throws IOException{
        try (PrintWriter printWriter = new PrintWriter(fich)){
            mostrarAsignaciones(printWriter);
        }catch (IOException e){
            throw new IOException(e.getMessage());
        }
    }

    protected void mostrarAsignaciones(PrintWriter printWriter){
        for (Map.Entry<Integer, SortedMap<Integer, List<Solicitud>>> list : asignaciones.entrySet()){
            printWriter.println( "DiaSem: " + list.getKey());
            for (Map.Entry<Integer, List<Solicitud>> asig : list.getValue().entrySet()){
                printWriter.println("Franja: " + asig.getKey() + "Lab: " + asig.getValue());
            }
        }
        printWriter.println("Errores de asignacion: " + "\n" + erroresDeAsignacion);
    }
}
