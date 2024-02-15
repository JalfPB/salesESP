package ventasEspaña;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class VentasMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	private String countryFilter;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    	// Obtener el número de línea actual
        long lineNumber = key.get();

        // Si es la primera línea, simplemente la ignoramos
        if (lineNumber == 0) {
            return;
        }
    	
    	// Parsear la línea del dataset
        String[] fields = value.toString().split(";");

        // Extraer información necesaria
        String invoiceNo = fields[0];
        String stockCode = fields[1];
        String description = fields[2];
        String quantity = fields[3];
        String invoiceDate = fields[4];
        String unitPrice = fields[5];
        String customerID = fields[6];
        String country = fields[7];
        
        context.write(new Text(country), new Text(invoiceDate + "," + unitPrice + "," + customerID));
    }

}
