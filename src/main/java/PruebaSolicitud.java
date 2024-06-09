import ex2305.Solicitud;

public class PruebaSolicitud {
    public static void main(String[] args) {
        Solicitud obj1 = new Solicitud("POO-B", 3, 2);
        Solicitud obj2 = new Solicitud("poo-b", 3, 2);
        Solicitud obj3 = new Solicitud("Web-A", 2, 3);
        System.out.println(obj1);
        System.out.println(obj2);
        System.out.println(obj3);

        if (obj1.equals(obj2)){
            System.out.println("Iguales");
        }else {
            System.out.println("Distintos");
        }

        if (obj1.equals(obj3)){
            System.out.println("Iguales");
        }else {
            System.out.println("Distintos");
        }

        if (obj2.equals(obj3)){
            System.out.println("Iguales");
        }else {
            System.out.println("Distintos");
        }

        System.out.println(obj1.compareTo(obj2));
        System.out.println(obj1.compareTo(obj3));
        System.out.println(obj2.compareTo(obj3));
    }
}
