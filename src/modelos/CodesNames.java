package src.modelos;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public record CodesNames(@SerializedName("supported_codes") Map<String,String> supported_codes) {
}
