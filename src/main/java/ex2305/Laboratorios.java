package ex2305;


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
            asignaciones = new TreeMap<>();
            erroresDeAsignacion = new TreeSet<>();
            solicitudes = new ArrayList<>();
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
                addSolicitud(new Solicitud(nom, num, n));
            }
        }catch (Exception e){
            throw new LabException(e.getMessage());
        }
    }
    public SortedSet<Solicitud> getSolicitudesOrdenadas(){
        SortedSet<Solicitud> sol = new TreeSet<>(Comparator.comparing(s -> s.getAsignatura().toUpperCase()));
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


    protected void asignarLabASolicitud(Solicitud aux) {
        // Inicializa el laboratorio de la solicitud a -1 (sin asignar)
        aux.setLab(-1);

        // Obtiene las asignaciones para el día de la semana de la solicitud.
        // Si no existen, crea un nuevo TreeMap para ese día.
        SortedMap<Integer, List<Solicitud>> asignacionesDia = asignaciones.getOrDefault(aux.getDiaSem(), new TreeMap<>());

        // Obtiene la lista de asignaciones para la franja horaria de la solicitud.
        // Si no existe una lista para esa franja, crea una nueva ArrayList.
        List<Solicitud> solicitudesEnHorario = asignacionesDia.getOrDefault(aux.getFranja(), new ArrayList<>());

        // Verifica si hay laboratorios disponibles para la franja horaria.
        if (solicitudesEnHorario.size() < maxLabs) {
            // Si hay laboratorios disponibles, añade la solicitud a la lista de asignaciones.
            solicitudesEnHorario.add(aux);

            // Asigna un número de laboratorio a la solicitud.
            // El número del laboratorio es el índice de la solicitud en la lista (empezando desde 0).
            int labNum = solicitudesEnHorario.size() - 1;
            aux.setLab(labNum);

            // Actualiza asignacionesDia con la nueva lista de solicitudes en horario.
            asignacionesDia.put(aux.getFranja(), solicitudesEnHorario);

            // Actualiza el mapa de asignaciones con el nuevo asignacionesDia.
            asignaciones.put(aux.getDiaSem(), asignacionesDia);
        } else {
            // Si no hay laboratorios disponibles, añade la solicitud a la lista de errores de asignación.
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
    public void cargarSolicitudesDeFichero(String fich) throws IOException {
        try {
            File file = new File(fich);
            try (Scanner scanner = new Scanner(file)){
                while (scanner.hasNextLine()){
                    try {
                        String s = scanner.nextLine();
                        addSolicitud(s); //sé que me sobran try, pero más vale que sobren a que falten
                    }catch (Exception e){
                        //no hacer nada
                    }
                }
            }catch (IOException E){
                throw new IOException(E.getMessage());
            }catch (Exception e){
                //no hacer nada
            }
        }catch (IOException e){
            throw new IOException("File not found");
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
