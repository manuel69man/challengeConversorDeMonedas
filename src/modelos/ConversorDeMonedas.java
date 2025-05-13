package src.modelos;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import src.exceptions.MyExceptions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ConversorDeMonedas {
    private ArrayList<String> arrayCode;
    private CodesNames codesNames;
    private String mensajeDeEntrada;
    private int monedaBase,monedaDestino,montoParaCalcular=0;
   // private ArrayList<Moneda> arrayMonedas = new ArrayList<>();
    private String uriTarifas="https://v6.exchangerate-api.com/v6/6648dbca36f9b0fda04440db/latest/USD";
    private String uriCodes="https://v6.exchangerate-api.com/v6/6648dbca36f9b0fda04440db/codes";
    private String uriPair = "https://v6.exchangerate-api.com/v6/6648dbca36f9b0fda04440db/pair/";
    private boolean status;
    private Gson gson;
    Monedas monedas;
    private Map<String,Double> map;
    private Map<String,String> mapnames;

    public void setMonedaBase(int monedaBase) {
        this.monedaBase = monedaBase;
    }

    public void setMonedaDestino(int monedaDestino) {
        this.monedaDestino = monedaDestino;
    }

    public Map<String, Double> getMap() {
        return map;
    }

    public String getMensajeDeEntrada() {
        return mensajeDeEntrada;
    }

    public void setMensajeDeEntrada(String mensajeDeEntrada) {
        this.mensajeDeEntrada = mensajeDeEntrada;
    }
    public boolean isStatus() {
        return status;
    }

    public ConversorDeMonedas(){
        setMensajeDeEntrada("Esriba 'Salir', 'Listar' o el número de la moneda base: ");
        gson= new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setPrettyPrinting()
                .create();
        initValues(Monedas.class);
        initValues(CodesNames.class);

    }

    private void initValues(Class clase) {
        try {
            String uri="";
            if (clase.getName().toLowerCase().contains("monedas")){
                uri=uriTarifas;
            } else if (clase.getName().toLowerCase().contains("codes")) {
                uri=uriCodes;
            }

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .build();
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            if (response.statusCode()!=404) { //Evaluamos errores en endpoints nal escritos
                this.status=true;

                if (clase.getName().toLowerCase().contains("monedas")){
                    monedas = (Monedas) gson.fromJson(json,clase);

                    if (monedas.errortype()!=null){
                        throw new MyExceptions(monedas.errortype());
                    } else {
                        map = monedas.conversion_rates();
                    }

                } else if (clase.getName().toLowerCase().contains("codes")) {
                    codesNames = (CodesNames) gson.fromJson(json,clase);
                    mapnames = codesNames.supported_codes();
                }

            } else {
                throw new MyExceptions("Error 404, Dirección web no existe");
            }
        } catch (MyExceptions e){
            System.out.println("ERROR: "+e.getMessage());
        }catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /*
    Este metodos sirve para Mostras todas la monedas dispoblies en la api para uso del usuario
     */

    public void listarMonedas() {
        if (codesNames!=null) {
            arrayCode = new ArrayList<>();
            for (String st:codesNames.supported_codes().keySet()) {
                arrayCode.add(st);
            }
            ArrayList<String> arrayNames = new ArrayList<>();
            for (String st:codesNames.supported_codes().values()) {
                arrayNames.add(st);
            }
            int longMax =  arrayNames.get(0).length(); //    stNames[0].length();
            for (String st : /*stNames*/arrayNames) {
                if (longMax <= st.length()) {
                    longMax = st.length();
                }
            }
            String stAux = "";
            for (int i = 0; i < arrayCode.size(); i++) {
                stAux = i + 1 + ")" + arrayCode.get(i) + ": " + arrayNames.get(i) + " ";

                if (stAux.length() < 48) {
                    stAux = stAux + " ".repeat(48 - stAux.length());
                }
                System.out.print(stAux);
                if ((i + 1) % 5 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
        }
    }

    public void hacerConversion(String opcion) {

        if ((monedaBase==0) && (monedaDestino==0)){
            setMonedaBase(Integer.valueOf(opcion));
            setMensajeDeEntrada(getMensajeDeEntrada().replace("base: ","destino: "));

        } else if ((monedaBase!=0) && (monedaDestino==0)){
            setMonedaDestino(Integer.valueOf(opcion));
            setMensajeDeEntrada("Ingrese el valor que desea convertir: ");

        } else if ((monedaDestino!=0) && (monedaBase!=0) && (montoParaCalcular==0)){
                montoParaCalcular=Integer.valueOf(opcion);

                ArrayList<String> array = new ArrayList<>();
                for (String code : map.keySet()){
                    array.add(code);
                }

            try {
                String uri=uriPair+arrayCode.get(monedaBase-1)+"/"+arrayCode.get(monedaDestino-1)+"/"+opcion;

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(uri))
                        .build();
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());

                String json = response.body();
                MonedasConvertidas monedasConvertidas = gson.fromJson(json, MonedasConvertidas.class);

                if ((response.statusCode()!=404) && monedasConvertidas.result().equals("success")) {
                    System.out.println(montoParaCalcular+" "+ monedasConvertidas.base_code()+
                            " son "+monedasConvertidas.conversion_result()+ " "+ monedasConvertidas.target_code());
                    listarMonedas();
                    setMensajeDeEntrada("Esriba 'Salir', 'Listar' o el número de la moneda base: ");
                    monedaBase=0;
                    monedaDestino=0;
                    montoParaCalcular=0;
                } else {
                    throw new MyExceptions("Error 404, Dirección web no existe");
                }
            } catch (MyExceptions e){
                System.out.println("ERROR: "+e.getMessage());
            }catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
