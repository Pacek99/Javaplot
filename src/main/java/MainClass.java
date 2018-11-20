import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import javax.swing.JOptionPane;

public class MainClass {

	private static Map<Double, Double> map;
	private static String sensor = "AK09918C";
	//private static String activity = "elevatorDown";
 
	public static void main(String csvFile, String activity, boolean osZ) {
            //String csvFile = "resources/indora-1540554936805.csv";
            //String csvFile = "resources/" + file;
            try {
                JavaPlot p = new JavaPlot();
		double[][] data = null;
                if (osZ) {
                    data = filterCSVFileBySensorAndActivityOnlyZAxis(sensor, activity, csvFile);
                } else {
                    data = filterCSVFileBySensorAndActivity(sensor, activity, csvFile);
                }
		if (data == null) {
			System.out.println("Data array is empty!");
			return;
		}
		PlotStyle myPlotStyle = new PlotStyle();
		myPlotStyle.setStyle(Style.HISTEPS);
		myPlotStyle.setLineWidth(1);
		DataSetPlot s = new DataSetPlot(data);
		s.setTitle("Graf " + activity);
		s.setPlotStyle(myPlotStyle);
		p.addPlot(s);
		
		p.plot();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Zadana aktivita sa v dátach nenachadza!");
                System.out.println("Zadana aktivita sa v dátach nenachadza!");
            }		
	}

	public static double[][] filterCSVFileBySensorAndActivity(String sensor, String activity, String csvFile) {
		String line = "";
		String cvsSplitBy = "\t";
		map = new HashMap<Double, Double>();

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			while ((line = br.readLine()) != null) {
				String[] zaznam = line.split(cvsSplitBy);

				for (int i = 0; i < zaznam.length; i++) {

					if (zaznam[2].equals(sensor) && zaznam[1].equals(activity)) {
						map.put(Double.valueOf(zaznam[0]),
								Double.valueOf(Math.sqrt(Double.parseDouble(zaznam[3]) * Double.parseDouble(zaznam[3])
										+ Double.parseDouble(zaznam[4]) * Double.parseDouble(zaznam[4])
										+ Double.parseDouble(zaznam[5]) * Double.parseDouble(zaznam[5]))));
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		double[][] data = new double[map.size()][2];
		Set entries = map.entrySet();
		Iterator entriesIterator = entries.iterator();

		int i = 0;
		while (entriesIterator.hasNext()) {

			Map.Entry mapping = (Map.Entry) entriesIterator.next();

			data[i][0] = (double) mapping.getKey();
			data[i][1] = (double) mapping.getValue();

			i++;
		}
		return data;
	}
	
	public static double[][] filterCSVFileBySensorAndActivityOnlyZAxis(String sensor, String activity, String csvFile) {
		String line = "";
		String cvsSplitBy = "\t";
		map = new HashMap<Double, Double>();

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			while ((line = br.readLine()) != null) {
				String[] zaznam = line.split(cvsSplitBy);

				for (int i = 0; i < zaznam.length; i++) {

					if (zaznam[2].equals(sensor) && zaznam[1].equals(activity)) {
						map.put(Double.valueOf(zaznam[0]),
								Double.valueOf(Double.parseDouble(zaznam[5])));
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		double[][] data = new double[map.size()][2];
		Set entries = map.entrySet();
		Iterator entriesIterator = entries.iterator();

		int i = 0;
		while (entriesIterator.hasNext()) {

			Map.Entry mapping = (Map.Entry) entriesIterator.next();

			data[i][0] = (double) mapping.getKey();
			data[i][1] = (double) mapping.getValue();

			i++;
		}
		return data;
	}
}
