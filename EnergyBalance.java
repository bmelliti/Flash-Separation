public class EnergyBalance implements NonLinearEquation{

    private FlowStream inlet;
    private FlowStream outlet;
    public static double R = 8.314;
    public static double Tref = 298.;

    public EnergyBalance (FlowStream inlet, FlowStream outlet) {
        this.inlet = inlet.clone();
        this.outlet = outlet.clone();
    }
    public EnergyBalance (EnergyBalance source){
        this.inlet = source.getInlet();
        this.outlet= source.getOutlet();
    }
    public EnergyBalance clone(){
        return new EnergyBalance(this);
    }
    public boolean equals (Object Comparator){
        if(!(this.getClass()==Comparator.getClass())) return false;
        for (int i = 0; i< this.inlet.getFlowSpecies().size();i++){
            if(!(this.inlet.getFlowSpecies().get(i)==((EnergyBalance) Comparator).getInlet().getFlowSpecies().get(i)));
            return false;
        }
        for (int i = 0; i< this.inlet.getFlowSpecies().size();i++) {
            if (!(this.outlet.getFlowSpecies().get(i) == ((EnergyBalance) Comparator).getOutlet().getFlowSpecies().get(i)));
            return false;
        }
        return true;
    }
    public void setInlet(FlowStream inlet) {
        this.inlet =  inlet.clone();
    }
    public void setOutlet(FlowStream outlet) {
        this.outlet = outlet.clone();
    }
    public FlowStream getInlet() {
        return this.inlet; // Permit modification of the inlet via this method
    }
    public FlowStream getOutlet() {
        return this.outlet;
    }

    public double SolveT() throws ConvergenceNotMetException, NoRootFoundException {
        return RootFinder.RiddersMethod(this, 200,1000);
    }


    public double ReturnEquation(double X) throws ConvergenceNotMetException, NoRootFoundException {
        //It sets temperature to the outlet stream to the value X given to return value
        this.outlet.setTemperature(X);
        //finds a V/F for this temperature using rootfinder
         if (this.outlet.getIsIdeal()==true){ // if its an ideal setream
            IdealCase idealCase = new IdealCase(this.outlet);
            this.outlet= idealCase.solve(); // this.outlet is now solved for this temperature
        }
        if (this.outlet.getIsIdeal()==false) { // if its a non ideal stream
            NonIdeal nonIdeal = new NonIdeal(this.outlet);
            this.outlet = nonIdeal.solve();
        }
        // initiate the size of the all the arrays in the method
        int n = this.inlet.getFlowSpecies().size();
        // initiate the array to calculate the vapor enthalpy of each flow species in the inlet stream
        double[] enthalpyV_inlet = new double[n];
        for (int i = 0; i < n; i++)
            enthalpyV_inlet[i] = R * (this.inlet.getFlowSpecies().get(i).getCPvParameters()[0] * (X- Tref) + 0.5*this.inlet.getFlowSpecies().get(i).getCPvParameters()[1] * (Math.pow(X, 2) - Math.pow(Tref, 2)) + (1/3) * this.inlet.getFlowSpecies().get(i).getCPvParameters()[2] * (Math.pow(X, 3) - Math.pow(Tref, 3)) - 0.5 * this.inlet.getFlowSpecies().get(i).getCPvParameters()[3] * ((1 / X)- (1/Tref)));        // initiate the array to calculate the liquid enthalpy of each flow species in the inlet stream
        double[] enthalpyL_inlet = new double[n];
        for (int i = 0; i < n; i++)
            enthalpyL_inlet[i] = (this.inlet.getFlowSpecies().get(i).getCPlParameters()[0] * (X -Tref) + 0.5*this.inlet.getFlowSpecies().get(i).getCPlParameters()[1] * (Math.pow(X, 2) - Math.pow(Tref, 2)) + (1/3) * this.inlet.getFlowSpecies().get(i).getCPlParameters()[2] * (Math.pow(X, 3) - Math.pow(Tref, 3)) + 0.25 * this.inlet.getFlowSpecies().get(i).getCPlParameters()[3] * (Math.pow(X, 4) - Math.pow(Tref, 4)));        //initiate the array to calculate the vapor enthalpy of each flow species in the outlet
        double[] enthalpyV_outlet = new double[n];
        for (int i = 0; i < n; i++)
            enthalpyV_outlet[i] = R * (this.outlet.getFlowSpecies().get(i).getCPvParameters()[0] * (X- Tref) + 0.5*this.outlet.getFlowSpecies().get(i).getCPvParameters()[1] * (Math.pow(X, 2) - Math.pow(Tref, 2)) + (1/3) * this.outlet.getFlowSpecies().get(i).getCPvParameters()[2] * (Math.pow(X, 3) - Math.pow(Tref, 3)) - 0.5 * this.outlet.getFlowSpecies().get(i).getCPvParameters()[3] * ((1 / X)- (1/Tref)));        //initiate the array to calculate the vapor enthalpy of each flow species in the outlet
        double[] enthalpyL_outlet = new double[n];
        for (int i = 0; i < n; i++)
            enthalpyL_outlet[i] = (this.outlet.getFlowSpecies().get(i).getCPlParameters()[0] * (X -Tref) + 0.5*this.outlet.getFlowSpecies().get(i).getCPlParameters()[1] * (Math.pow(X, 2) - Math.pow(Tref, 2)) + (1/3) * this.outlet.getFlowSpecies().get(i).getCPlParameters()[2] * (Math.pow(X, 3) - Math.pow(Tref, 3)) + 0.25 * this.outlet.getFlowSpecies().get(i).getCPlParameters()[3] * (Math.pow(X, 4) - Math.pow(Tref, 4)));        double EnthalpyFeed = 0;
        double EnthalpyVapor = 0;
        double EnthalpyLiquid = 0;
        double F = this.inlet.getMolarFlowRate();
        double Q;
        // calculates the sum of all enthalpies in the feed,outlet liquid and outlet vapor
        for (int i = 0; i < n; i++) {
            EnthalpyFeed += (F/1000) * (this.inlet.getFlowSpecies().get(i).getLiquidFraction() * enthalpyL_inlet[i] + this.inlet.getFlowSpecies().get(i).getVaporFraction()* (this.inlet.getFlowSpecies().get(i).getHeatOfVaporization()+enthalpyV_inlet[i]));
            EnthalpyLiquid +=  (1 - this.outlet.getVaporFraction()) *this.outlet.getFlowSpecies().get(i).getLiquidFraction() * (F/1000) * enthalpyL_outlet[i];
            EnthalpyVapor += this.outlet.getVaporFraction() * this.outlet.getFlowSpecies().get(i).getVaporFraction() * (F/1000) * (this.outlet.getFlowSpecies().get(i).getHeatOfVaporization() + enthalpyV_outlet[i]);
        }
        Q=EnthalpyFeed-EnthalpyVapor - EnthalpyLiquid ;
        return Q;
    }
}
