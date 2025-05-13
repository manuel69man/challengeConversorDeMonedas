package src.modelos;

import com.google.gson.annotations.SerializedName;
/*
Este registro lo usamos para obtener de la api a trabes de clase Gson el resultados de la conversion de las divisas
*/
public record MonedasConvertidas(
        @SerializedName("result") String result,
        @SerializedName("base_code") String base_code,
        @SerializedName("target_code") String target_code,
        @SerializedName("conversion_rate") Double conversion_rate,
        @SerializedName("conversion_result") Double conversion_result,
        @SerializedName ("error-type") String errortype) {
}
