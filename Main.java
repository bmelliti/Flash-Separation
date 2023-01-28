
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ConvergenceNotMetException, NoRootFoundException {
        List<FlowSpecies> presetSpecies = new ArrayList<FlowSpecies>();
        FlowSpecies ethane = new FlowSpecies(0.2, 0, 0.2);
        double[] ethane_params = new double[]{13.88, 1582, -13.76};
        double[] ethane_criticalparams = new double[]{305.3, 145.5E-6, 0.279, 0.100, 48.72};
        double[] ethane_vapCPparams = new double[]{1.131, 1.92E-02, -5.56E-06, 0};
        double[] ethane_liqCPparams = new double[]{4.4009E1, 8.9718E4, 9.1877E2, -1.886E3, 0};
        ethane.setName("Ethane");
        ethane.setParameters(ethane_params);
        ethane.setCriticalParameters(ethane_criticalparams);
        ethane.setCpvParameters(ethane_vapCPparams);
        ethane.setCplParameters(ethane_liqCPparams);
        ethane.setHeatOfVaporization(15500.3);
        presetSpecies.add(ethane);

        double[] pentane_params = new double[]{13.98, 2555, -36.25};
        double[] pentane_criticalparams = new double[]{469.7, 313.0E-6, 0.270, 0.252, 33.70};
        double[] pentane_vapCPparams = new double[]{2.464, 4.54E-02, -1.41E-05, 0};
        double[] pentane_liqCPparams = new double[]{1.5908E5, -2.705E2, 9.9537E-1, 0};
        FlowSpecies pentane = new FlowSpecies(0.2, 0, 0.2);
        pentane.setName("Pentane");
        pentane.setParameters(pentane_params);
        pentane.setCriticalParameters(pentane_criticalparams);
        pentane.setCpvParameters(pentane_vapCPparams);
        pentane.setCplParameters(pentane_liqCPparams);
        pentane.setHeatOfVaporization(27623.75);
        presetSpecies.add(pentane);

        double[] hexane_params = new double[]{13.83, 2714, -47.8};
        double[] hexane_criticalparams = new double[]{507.6, 371E-6, 0.266, 0.301, 30.25};
        double[] hexane_vapCPparams = new double[]{3.025, 5.37E-02, -1.68E-05, 0};
        double[] hexane_liqCPparams = new double[]{1.7212E5, -1.8378E2, 8.8734E-1, 0};
        FlowSpecies hexane = new FlowSpecies(0.2, 0, 0.2);
        hexane.setName("Hexane");
        hexane.setParameters(hexane_params);
        hexane.setCriticalParameters(hexane_criticalparams);
        hexane.setCpvParameters(hexane_vapCPparams);
        hexane.setCplParameters(hexane_liqCPparams);
        hexane.setHeatOfVaporization(30927.77778);
        presetSpecies.add(hexane);

        double[] cyclohexane_params = new double[]{13.79, 2795, -41.11};
        double[] cyclohexane_criticalparams = new double[]{553.6, 308.0E-6, 0.273, 0.210, 40.73};
        double[] cyclohexane_vapCPparams = new double[]{-3.876, 6.32E-02, -2.09E-05, 0};
        double[] cyclohexane_liqCPparams = new double[]{-2.206E5, 3.1183E3, -9.4216, 1.0687E-2};
        FlowSpecies cyclohexane = new FlowSpecies(0.2, 0, 0.2);
        cyclohexane.setName("Cyclohexane");
        cyclohexane.setParameters(cyclohexane_params);
        cyclohexane.setCriticalParameters(cyclohexane_criticalparams);
        cyclohexane.setCpvParameters(cyclohexane_vapCPparams);
        cyclohexane.setCplParameters(cyclohexane_liqCPparams);
        cyclohexane.setHeatOfVaporization(31690.4);
        presetSpecies.add(cyclohexane);

        double[] water_params = new double[]{16.54, 3985, -39};
        double[] water_criticalparams = new double[]{647.1, 55.9E-6, 0.229, 0.345, 220.55};
        double[] water_vapCPparams = new double[]{3.470, 1.450E-3, 0, 0.121E-5};
        double[] water_liqCPparams = new double[]{2.7637E5, -2.0901E3, 8.125, -1.4116E-2, 9.3701E-6};
        FlowSpecies water = new FlowSpecies(0.1, 0, 0.1);
        water.setName("Water");
        water.setParameters(water_params);
        water.setCriticalParameters(water_criticalparams);
        water.setCpvParameters(water_vapCPparams);
        water.setCplParameters(water_liqCPparams);
        water.setHeatOfVaporization(43990.);
        presetSpecies.add(water);

        double[] nitrogen_params = new double[]{13.45, 658, -2.85};
        double[] nitrogen_criticalparams = new double[]{126.2, 89E-6, 0.288, 0.037, 33.9};
        double[] nitrogen_vapCPparams = new double[]{3.28, 5.93E-04, 0, 4.00E-08};
        double[] nitrogen_liqCPparams = new double[]{3.28, 5.93E-04, 0, 4.00E-08};
        FlowSpecies nitrogen = new FlowSpecies(0.1, 0.1, 0);
        nitrogen.setName("Nitrogen");
        nitrogen.setParameters(nitrogen_params);
        nitrogen.setCriticalParameters(nitrogen_criticalparams);
        nitrogen.setCpvParameters(nitrogen_vapCPparams);
        nitrogen.setCplParameters(nitrogen_liqCPparams);
        nitrogen.setHeatOfVaporization(5850.0);
        presetSpecies.add(nitrogen);

        // add way to add another species

        FlowStream inletStream1 = new FlowStream(presetSpecies, 360, 3, true, 1, 0);
        //OptimizedT.OptimizeT(360,120,"ethane", 20, inletStream1);
        try {
           UserInterface.UI(inletStream1);
        }
        catch (NoRootFoundException ex1){
          System.out.println(ex1.getMessage());
        }
        catch (ConvergenceNotMetException ex2){
            System.out.println(ex2.getMessage());
       }
        catch (FileNotFoundException ex3){
            System.out.println(ex3.getMessage());
        }
        finally {
            System.out.println("This project is done !");
        }
    }
}