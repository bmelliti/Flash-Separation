public class BubblePoint implements NonLinearEquation {

    private FlowStream flowStream;

    public BubblePoint(FlowStream flowStream) {
        if (flowStream.getTemperature() > 0 && flowStream.getPressure() > 0 && flowStream.getFlowSpecies().size() > 0) {
            this.flowStream = flowStream.clone();
        } else {
            System.out.println("Error: flow stream must contain a pressure, temperature, and at least one species!");
            System.exit(1);
        }
    }
    public BubblePoint(BubblePoint source){
        if (source.flowStream.getTemperature()> 0 && flowStream.getPressure() > 0 && flowStream.getFlowSpecies().size() > 0) {
            this.flowStream = source.flowStream.clone();
        } else {
            System.out.println("Error: flow stream must contain a pressure, temperature, and at least one species!");
            System.exit(1);
        }
    }
    public BubblePoint clone(){
        return new BubblePoint(this);
    }

    public boolean equals (Object Comparator){
        if(!(this.getClass()==Comparator.getClass())) return false;
        for (int i = 0; i< this.flowStream.getFlowSpecies().size();i++){
            if(!(this.flowStream.getFlowSpecies().get(i)==((BubblePoint) Comparator).flowStream.getFlowSpecies().get(i)));
                return false;
        }
        return true;
    }
    public void setFlowStream(FlowStream flowStream){
        this.flowStream = flowStream.clone();
    }
    public FlowStream getFlowStream(){
        return this.flowStream;
    }

    public double BubbleTempcalc() throws NoRootFoundException, ConvergenceNotMetException {
        double Tbubble = RootFinder.RiddersMethod(this,100,1000);
        if(Tbubble < 0 ) throw new NoRootFoundException("ERROR Bubble temperature is negative");
        if(Double.isNaN(Tbubble)) throw new NoRootFoundException("ERROR Bubble temperature is not a number");
        return Tbubble;
    }

    public double ReturnEquation(double X){
        int i;
        double result = 0;
        int n = this.flowStream.getFlowSpecies().size();
        double T=  this.flowStream.getTemperature();
        double P = this.flowStream.getPressure();
        double [] Psat= new double[n];
        double [] ki= new double[n];
        double [] xi = new double[n];

        // Determine if any of the species is likely to be non-condensable, and if so, ignore it
        for (i = 0; i < n; i++) {
            double Tc = this.flowStream.getFlowSpecies().get(i).getCriticalParameters()[0];
            if (T > Tc)
                this.flowStream.getFlowSpecies().get(i).setOverallFraction(0);
        }
        // ideal case Ki = Psat/P

    for (i = 0; i < n; i++) {
        xi[i] = this.flowStream.getFlowSpecies().get(i).getOverallFraction();
        Psat[i] = IdealCase.SaturationPresurecalc(this.flowStream.getFlowSpecies().get(i), X);
        ki[i] = Psat[i] / P;
        result += xi[i] * ki[i];
   }

        return (result - 1);
}

}




