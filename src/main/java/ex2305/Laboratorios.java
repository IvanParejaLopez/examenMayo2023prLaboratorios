
package ex2305;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Laboratorios {
    private int maxLabs;
    private List<Solicitud> solicitudes;
    private SortedSet<Solicitud> erroresDeAsignacion;
    private SortedMap<Integer, SortedMap<Integer, List<Solicitud>>> asignaciones;

    public Laboratorios(int x){
        if(x<0){
            throw new LabException("Argumentos erroneos");
        }else{
            this.maxLabs=x;
            this.solicitudes=new ArrayList<>();
            this.asignaciones=new TreeMap<>();
            this.erroresDeAsignacion=new TreeSet<>();
        }
    }

    protected SortedSet<Solicitud> getErroresDeAsignacion() {
        return erroresDeAsignacion;
    }

    protected void addSolicitud(Solicitud xd){
        boolean found = false;
        int pos = 0;
        while(pos<solicitudes.size()&&!found) {
            if(solicitudes.get(pos).equals(xd)){
                found=true;
            }else{
                pos++;
            }
        }
        if(!found){
            solicitudes.add(xd);
        }
    }

    public void addSolicitud(String lol){
        String[] data = lol.split(";");
        try{
            String asi = data[0];
            int dia = Integer.parseInt(data[1]);
            int fran = Integer.parseInt(data[2]);
            Solicitud aux = new Solicitud(asi, dia, fran);
            addSolicitud(aux);
        }catch (NumberFormatException e){
            throw new LabException("Argumentos erroneos");
        }
    }

    public SortedSet<Solicitud> getSolicitudesOrdenadas(){
        SortedSet<Solicitud> solicitudSortedSet = new TreeSet<>(Comparator.comparing(s -> s.getAsignatura().toUpperCase()));
        solicitudSortedSet.addAll(solicitudes);
        return solicitudSortedSet;
    }

    public void asignarLabs(){
        this.erroresDeAsignacion=new TreeSet<>();
        this.asignaciones=new TreeMap<>();
        for(Solicitud aux : solicitudes){
            asignarLabASolicitud(aux);
        }
    }

    protected void asignarLabASolicitud(Solicitud aux) {
        aux.setLab(-1);

        // Get the assignments for the day of the week
        SortedMap<Integer, List<Solicitud>> asignacionesDia = asignaciones.computeIfAbsent(aux.getDiaSem(), k -> new TreeMap<>());

        // Get the list of assignments for the time slot
        List<Solicitud> solicitudesEnHorario = asignacionesDia.computeIfAbsent(aux.getFranja(), k -> new ArrayList<>());

        // Check if there are available labs for the time slot
        if (solicitudesEnHorario.size() < maxLabs) {
            // Add the solicitud to the list of assignments
            solicitudesEnHorario.add(aux);

            // Assign the laboratory number
            int labNum = solicitudesEnHorario.size() - 1;
            aux.setLab(labNum);
        } else {
            // If no labs are available, add to erroresDeAsignacion
            erroresDeAsignacion.add(aux);
        }
    }

    @Override
    public String toString() {
        StringBuilder br = new StringBuilder();
        StringBuilder err = new StringBuilder();
        StringBuilder asi = new StringBuilder();

        // Print solicitudes
        br.append("Solicitudes: [");
        for (Solicitud aux : solicitudes) {
            br.append(aux).append(", ");
        }
        if (!solicitudes.isEmpty()) {
            br.delete(br.length() - 2, br.length());
        }
        br.append("]\n");

        // Print erroresDeAsignacion
        err.append("ErroresDeAsignacion: [");
        for (Solicitud aux2 : erroresDeAsignacion) {
            err.append(aux2).append(", ");
        }
        if (!erroresDeAsignacion.isEmpty()) {
            err.delete(err.length() - 2, err.length());
        }
        err.append("]\n");

        // Print asignaciones
        asi.append("Asignaciones: {");
        int size = asignaciones.entrySet().size();
        int aux = 1;
        for (Map.Entry<Integer, SortedMap<Integer, List<Solicitud>>> entry : asignaciones.entrySet()) {
            int dayOfWeek = entry.getKey();
            SortedMap<Integer, List<Solicitud>> dayAssignments = entry.getValue();

            asi.append(dayOfWeek).append("={");
            for (Map.Entry<Integer, List<Solicitud>> innerEntry : dayAssignments.entrySet()) {
                int timeSlot = innerEntry.getKey();
                List<Solicitud> solicitudesEnHorario = innerEntry.getValue();

                asi.append(timeSlot).append("=[");
                for (int i = 0; i < solicitudesEnHorario.size(); i++) {
                    Solicitud solicitud = solicitudesEnHorario.get(i);
                    asi.append("(").append(solicitud.getAsignatura()).append(", ")
                            .append(dayOfWeek).append(", ").append(timeSlot).append(", ")
                            .append(solicitud.getLab()).append(")");
                    if (i < solicitudesEnHorario.size() - 1) {
                        asi.append(", ");
                    }
                }
                asi.append("]");
            }
            if(aux<size){
                asi.append("},");
                aux++;
            }else{
                asi.append("}");
            }

        }
        asi.append("}");

        return "("+br + err + asi + ")";
    }

    public void cargarSolicitudesDeFichero(String file) throws IOException {
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line=br.readLine())!=null){
                try{
                    addSolicitud(line);
                }catch (NumberFormatException ignored){
                }
            }
        }catch (IOException e){
            throw new IOException("File not found");
        }
    }

    protected void mostrarAsignaciones(PrintWriter writer) {
        // Iterate through asignaciones
        for (Map.Entry<Integer, SortedMap<Integer, List<Solicitud>>> entry : asignaciones.entrySet()) {
            int dayOfWeek = entry.getKey();
            SortedMap<Integer, List<Solicitud>> dayAssignments = entry.getValue();

            // Iterate through assignments for each day
            for (Map.Entry<Integer, List<Solicitud>> innerEntry : dayAssignments.entrySet()) {
                int timeSlot = innerEntry.getKey();
                List<Solicitud> solicitudesEnHorario = innerEntry.getValue();

                // Print assignments
                writer.println("DiaSem: " + dayOfWeek + "; Franja: " + timeSlot);
                for (int i = 0; i < solicitudesEnHorario.size(); i++) {
                    Solicitud solicitud = solicitudesEnHorario.get(i);
                    writer.println("Lab: " + solicitud.getLab() +
                            ": " +solicitud);
                }
            }
        }
        writer.println("ErroresDeAsignacion:");
        for (Solicitud error : erroresDeAsignacion) {
            writer.println(error);
        }
    }
    public void guardarAsignacionesEnFichero(String fich) throws IOException{
        try (PrintWriter printWriter = new PrintWriter(fich)){
            mostrarAsignaciones(printWriter);
        }catch (IOException e){
            throw new IOException(e.getMessage());
        }
    }
}