package src;

import src.modelos.ConversorDeMonedas;

import java.io.IOException;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args)   {
        String opcion="";
        System.out.println("***********************************************");
        System.out.println("""
                ¡¡Bienvenidos al Conversor de Moneda en tiempo real!!
                Puedes hacer conversión entre cualquier par de Monedas mundiales.
                Seleciona dos monedas(Moneda Base y Moneda Final) por su número en la siguiente lista.
                """);
        Scanner scanner = new Scanner(System.in);
        ConversorDeMonedas conversorDeMonedas = new ConversorDeMonedas();
        conversorDeMonedas.listarMonedas();
        if (conversorDeMonedas.isStatus()) {
            while (!opcion.toLowerCase().equals("salir")) {
                System.out.println(conversorDeMonedas.getMensajeDeEntrada());
                opcion = scanner.nextLine();
                if (opcion.toLowerCase().equals("listar")) {
                    conversorDeMonedas.listarMonedas();
                } else if (opcion.toLowerCase().equals("salir")) {
                    break;
                } else {
                    try {
                        if ((Integer.valueOf(opcion)>0) ){
                            conversorDeMonedas.hacerConversion(opcion);
                        }
                    } catch (NumberFormatException e){
                        System.out.println("Error solo esriba 'Salir', 'Listar' o el número de la  moneda base: ");
                    }
                }
            }
        } else {
            System.out.println("Hubo un error en la conexión con el servidor");
        }
    }
}
