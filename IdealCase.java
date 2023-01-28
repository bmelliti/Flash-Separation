public class IdealCase implements NonLinearEquation {
    private FlowStream flowStream;

    public IdealCase(FlowStream flowStream) {
        if (flowStream.getTemperature() > 0 && flowStream.getPressure() > 0 && flowStream.getFlowSpecies().size() > 0) {
            this.flowStream = flowStream.clone();
        } else {
            System.out.println("Error: flow stream must contain a pressure, temperature, and at least one species!");
            System.exit(1);
        }
    }
    public IdealCase(IdealCase source){
        if (source.flowStream.clone().getTemperature() > 0 && source.flowStream.clone().getPressure() > 0 && source.flowStream.clone().getFlowSpecies().size() > 0) {
            this.flowStream = source.flowStream.clone();
        } else {
            System.out.println("Error: flow stream must contain a pressure, temperature, and at least one species!");
            System.exit(1);
        }
    }
    public IdealCase clone(){
        return new IdealCase(this);
    }
    public boolean equals (Object Comparator){
            if(!(this.getClass()==Comparator.getClass())) return false;
            if(!(this.flowStream==((IdealCase) Comparator).flowStream)) return false;
            return true;
    }

    public FlowStream getFlowStream() {
        return this.flowStream;
    }

    public void setFlowStream(FlowStream newStream) {
        this.flowStream = new FlowStream(newStream);
    }

    public static double SaturationPresurecalc(Species species, double T) {
        double a = species.getParameters()[0];
        double b = species.getParameters()[1];
        double c = species.getParameters()[2];
        return  (Math.exp(a - (b/(c + T))))*0.01; // Psat is too low = > K-1 = -1 calcs are not going to work V/F = NaN

    }


    public FlowStream solve() throws NoRootFoundException, ConvergenceNotMetException {
        double P = this.flowStream.getPressure();
        double VoverF = RootFinder.RiddersMethod(this, 0, 1);
        if(Double.isNaN(VoverF)) throw new NoRootFoundException("Error Not root found (Not a number)");
        double temperature = this.flowStream.getTemperature();
        double Zi, P_sat;
        double kMinusOne;
        double Xi;
        double Yi;
        for ( int i= 0; i<this.flowStream.getFlowSpecies().size(); i++) {
            Zi = this.flowStream.getFlowSpecies().get(i).getOverallFraction();
            P_sat = IdealCase.SaturationPresurecalc(flowStream.getFlowSpecies().get(i), temperature);
            kMinusOne = (P_sat / P) - 1;
            Xi = Zi / (1 + VoverF * kMinusOne);
            this.flowStream.getFlowSpecies().get(i).setLiquidFraction(Xi);
            Yi = (kMinusOne + 1) * Xi;
            this.flowStream.getFlowSpecies().get(i).setVaporFraction(Yi);
        }
        this.flowStream.setVaporFraction(VoverF);
        return this.flowStream;
    }


    public double ReturnEquation(double X) {
        int i;
        double result = 0.0;
        double T = this.flowStream.getTemperature();
        double P = this.flowStream.getPressure();
        double Zi, P_sat;
        double kMinusOne;

        for (i = 0; i < flowStream.getFlowSpecies().size(); i++) {

            Zi = this.flowStream.getFlowSpecies().get(i).getOverallFraction();
            P_sat = IdealCase.SaturationPresurecalc(flowStream.getFlowSpecies().get(i), T);

            kMinusOne = (P_sat /P) - 1;

            result += (Zi*kMinusOne)/(1 + X*kMinusOne) ;
        }
        return result;
    }
}
