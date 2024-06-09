
import ex2305.*;

import java.io.PrintWriter;

public class PruebaLabs {
	public static void main(String[] args) {
		prueba1();
		prueba2();
		prueba3();
		prueba4();
	}
	private static void prueba1() {
		try {
			System.out.println("\nPrueba1:");
			Laboratorios labs = new Laboratorios(3);
			labs.addSolicitud("POO-A; 1; 3");
			labs.addSolicitud("POO-B; 1; 3");
			labs.addSolicitud("POO-A; 2; 1");
			labs.addSolicitud("POO-B; 2; 1");
			labs.addSolicitud("Prg-A; 2; 1");
			labs.addSolicitud("Prg-B; 2; 1");
			labs.addSolicitud("PSC-A; 1; 1");
			labs.addSolicitud("PSC-B; 5; 1");
			labs.addSolicitud("IAx-A; 2; 1");
			labs.addSolicitud("IAx-B; 2; 1");
			labs.addSolicitud("Web-A; 1; 3");
			labs.addSolicitud("Web-B; 1; 3");
			labs.addSolicitud("Sec-A; 2; 1");
			labs.addSolicitud("Sec-B; 2; 1");
			labs.addSolicitud("PSC-A; 1; 1");
			labs.addSolicitud("PSC-B; 5; 1");
			labs.addSolicitud("IAx-A; 2; 1");
			labs.addSolicitud("IAx-B; 2; 1");
			labs.asignarLabs();
			System.out.println("\nLaboratorios:");
			System.out.println(labs);
			System.out.println("\nSolicitudes Ordenadas:");
			System.out.println(labs.getSolicitudesOrdenadas());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void prueba2() {
		try {
			System.out.println("\nPrueba2:");
			Laboratorios labs = new Laboratorios(3);
			labs.cargarSolicitudesDeFichero("data/solicitudes1.txt");
			labs.asignarLabs();
			System.out.println("\nLaboratorios:");
			System.out.println(labs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void prueba3() {
		try {
			System.out.println("\nPrueba3:");
			Laboratorios labs = new Laboratorios(3);
			labs.cargarSolicitudesDeFichero("data/solicitudes2.txt");
			labs.asignarLabs();
			System.out.println("\nLaboratorios:");
			System.out.println(labs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void prueba4() {
		try {
			System.out.println("\nPrueba4:");
			Laboratorios labs = new Laboratorios(3);
			labs.addSolicitud("POO-A; 1; 3");
			labs.addSolicitud("POO-B; 1; 3");
			labs.addSolicitud("POO-A; 2; 1");
			labs.addSolicitud("POO-B; 2; 1");
			labs.addSolicitud("Prg-A; 2; 1");
			labs.addSolicitud("Prg-B; 2; 1");
			labs.addSolicitud("PSC-A; 1; 1");
			labs.addSolicitud("PSC-B; 5; 1");
			labs.addSolicitud("IAx-A; 2; 1");
			labs.addSolicitud("IAx-B; 2; 1");
			labs.addSolicitud("Web-A; 1; 3");
			labs.addSolicitud("Web-B; 1; 3");
			labs.addSolicitud("Sec-A; 2; 1");
			labs.addSolicitud("Sec-B; 2; 1");
			labs.asignarLabs();
			System.out.println("\nLaboratorios:");
			System.out.println(labs);
			labs.guardarAsignacionesEnFichero("data/asignaciones.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
