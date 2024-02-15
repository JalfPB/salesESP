package ventasEspaña;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class VentasReducer extends Reducer<Text, Text, Text, Text> {

	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context)
	        throws IOException, InterruptedException {
	    // Obtenemos el ID del cliente (si fue proporcionado como parámetro)
	    String customerID = context.getConfiguration().get("customerID");

	    int totalVentas = 0;
	    double importeTotal = 0;

	    Map<String, Integer> ventasPorMes = new HashMap<>();
	    Map<String, Double> importePorMes = new HashMap<>();

	    for (Text value : values) {
	        String[] campos = value.toString().split(",");
	        if (campos.length != 3) {
	            // Los campos no tienen el formato esperado, ignoramos este valor
	            continue;
	        }
	        String fecha = campos[0];
	        double precio = Double.parseDouble(campos[1]);
	        String clienteID = campos[2];

	        // Si se proporcionó un customerID y no coincide con el del registro, lo ignoramos
	        if (customerID != null && !clienteID.equals(customerID))
	            continue;

	        totalVentas++;
	        importeTotal += precio;
	        String mes = fecha.split("/")[1].split(" ")[0];
	        ventasPorMes.put(mes, ventasPorMes.getOrDefault(mes, 0) + 1);
	        importePorMes.put(mes, importePorMes.getOrDefault(mes, 0.0) + precio);
	    }

	    StringBuilder sb = new StringBuilder();
	    sb.append("Ventas totales: ").append(totalVentas).append("\n");
	    sb.append("Importe total: ").append(importeTotal).append("\n");
	    sb.append("Ventas por mes:\n");
	    for (Map.Entry<String, Integer> entry : ventasPorMes.entrySet()) {
	        sb.append("Mes ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
	    }
	    sb.append("Importe por mes:\n");
	    for (Map.Entry<String, Double> entry : importePorMes.entrySet()) {
	        sb.append("Mes ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
	    }

	    context.write(key, new Text(sb.toString()));
	}

}
