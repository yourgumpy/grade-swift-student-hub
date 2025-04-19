
import java.text.DecimalFormat;

public class CGPA {
    public static String format(double cgpa) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(cgpa);
    }
}
