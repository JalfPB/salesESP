package ventasEspaña;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class VentasDriver {
	
	public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Uso: VentasDriver <entrada> <salida> [customerID]");
            System.exit(1);
        }

     // Configurar trabajo MapReduce
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Sales Analysis");
        job.setJarByClass(VentasDriver.class);
        
        
        if(args.length>2) {
        // Pasar el cliente como una propiedad de configuración
        job.getConfiguration().set("customerID", args[2]);
        }

        // Configurar clases Mapper y Reducer
        job.setMapperClass(VentasMapper.class);
        job.setReducerClass(VentasReducer.class);

        // Configurar tipos de salida
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Configurar archivos de entrada y salida
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Ejecutar el trabajo MapReduce
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
