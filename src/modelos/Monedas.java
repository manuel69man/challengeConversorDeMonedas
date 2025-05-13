package src.modelos;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParser;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/*
Registro usado para obtener los distintas divisas
* */

public record Monedas(
        @SerializedName("result") String result,
        @SerializedName("conversion_rates") Map<String,Double> conversion_rates,
        @SerializedName ("error-type") String errortype) {
}

