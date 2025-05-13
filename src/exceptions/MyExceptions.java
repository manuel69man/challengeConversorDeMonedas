package src.exceptions;

public class MyExceptions extends  RuntimeException{
    String mensaje;

    public MyExceptions(String excepcion) {
        this.mensaje = excepcion;
    }

    @Override
    public String getMessage() {
        return this.mensaje;
    }
}
