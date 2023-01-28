import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class FlowStream {
    private List<FlowSpecies> flowSpecies;
    private double T;
    private double P;
    private boolean IsIdeal ;
    private double MolarFlowRate ;
    private double VaporFraction;

    public FlowStream(List<FlowSpecies> flowSpecies,double T,double P,boolean IsIdeal,double MolarFlowRate, double VaporFraction ) {
        this.flowSpecies= flowSpecies;
        this.T = T;
        this.P= P;
        this.IsIdeal = IsIdeal;
        this.MolarFlowRate = MolarFlowRate;
        this.VaporFraction= VaporFraction;
    }

    public FlowStream( FlowStream source){
        this.flowSpecies = source.flowSpecies;
        this.T= source.T;
        this.P= source.P;
        this.IsIdeal= source.IsIdeal;
        this.MolarFlowRate = source.MolarFlowRate;
        this.VaporFraction= source.VaporFraction;
    }
    public  FlowStream clone (){
        return new FlowStream(this);
    }


    public void addFlowSpecies(FlowSpecies flowSpecies) {;

        this.flowSpecies.add(flowSpecies);
    }
    public boolean setMolarFlowRate(double molarFlowRate) {
        if (molarFlowRate >= 0) {
            this.MolarFlowRate = molarFlowRate;
            return true;
        } else {
            return false;
        }
    }

   public void setVaporFraction(double VaporFraction){
        this.VaporFraction= VaporFraction;
   }

    public boolean setTemperature(double temperature) {
        if (temperature >= 0) {
            this.T = temperature;
            return true;
        } else {
            return false;
        }
    }
    public boolean setPressure(double pressure) {
        if (pressure >= 0) {
            this.P = pressure;
            return true;
        } else {
            return false;
        }
    }
    public void setIsIdeal(boolean isIdeal){
        this.IsIdeal=isIdeal;
    }
    public List<FlowSpecies> getFlowSpecies() {
        return(this.flowSpecies);
    }
    public double getVaporFraction(){return this.VaporFraction;}

    public double getMolarFlowRate() {
        return this.MolarFlowRate;
    }

    public double getTemperature() {
        return this.T;
    }

    public double getPressure() {
        return this.P;
    }

    public boolean getIsIdeal(){
        return this.IsIdeal;
    }

}
