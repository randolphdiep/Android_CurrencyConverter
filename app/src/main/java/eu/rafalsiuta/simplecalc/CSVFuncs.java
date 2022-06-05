package eu.rafalsiuta.simplecalc;

import android.content.Context;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CSVFuncs {

    public static List<History> loadFile(Context context) {
        File csvfile;
        BufferedReader reader;
        CSVReader csvReader;
        ArrayList<History> lstItems = new ArrayList<>();
        String[] nextLine;
        try {
            csvfile = new File(context.getFilesDir().getAbsolutePath() + "/history.csv");
            FileInputStream fis = new FileInputStream(csvfile);
            reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            csvReader = new CSVReader(reader);
            while ((nextLine = csvReader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                if (nextLine.length != 6)
                    continue;
                History item = new History(nextLine[0], nextLine[1], nextLine[2], nextLine[3], nextLine[4], nextLine[5]);
                lstItems.add(item);
            }
        } catch (IOException e) {
            Toast.makeText(context, e.getCause().toString() + ": " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        return lstItems;
    }

    public static void saveData(Context context ,List<History> historyList) {
        try {
            CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(new FileOutputStream(context.getFilesDir().getAbsolutePath() + "/history.csv"), "UTF-8"));
            for(History item : historyList) {
                String[] line = new String[]{item.getCodeInput(), item.getCodeOutput(), item.getInput(), item.getOutput(), item.getRate(), item.getTime()};
                csvWriter.writeNext(line);
            }
            csvWriter.close();
        } catch (IOException e) {
            Toast.makeText(context, e.getCause().toString() + ": " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
