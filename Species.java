public class Species {
    private double a, b,c, Tc,Vc,Zc,w,Pc, Av, Bv, Cv, Dv, Al, Bl, Cl, Dl, HeatOfVaporization, Phi_sat, Phi_v;
    String Name;
    public Species (){
       this.reset();
    }
    public void reset(){
        this.Name= null;
        this.a= 0;
        this.b= 0;
        this.c= 0;
        this.Tc= 0;
        this.Vc= 0;
        this.Zc= 0;
        this.w= 0;
        this.Pc= 0;
        this.Av= 0;
        this.Bv= 0;
        this.Cv= 0;
        this.Dv= 0;
        this.Al= 0;
        this.Bl= 0;
        this.Cl= 0;
        this.Dl= 0;
        this.HeatOfVaporization= 0;
        this.Phi_sat = 0;
        this.Phi_v = 0;
    }
    public double[] getParameters(){
        double [] parameters= new double[3];
        parameters[0] = this.a;
        parameters[1] = this.b;
        parameters[2] = this.c;
        return parameters;
    }
    public double[] getCriticalParameters(){
        double[] criticalparameters = new double[5];
        criticalparameters[0] = this.Tc;
        criticalparameters[1] = this.Vc;
        criticalparameters[2] = this.Zc;
        criticalparameters[3]= this.w;
        criticalparameters[4]= this.Pc;
        return criticalparameters;
    }
    public double[] getCPvParameters(){
        double[] CPvParameters= new double[4];
        CPvParameters[0]= this.Av;
        CPvParameters[1]= this.Bv;
        CPvParameters[2]= this.Cv;
        CPvParameters[3]= this.Dv;
        return CPvParameters;
    }
    public double[] getCPlParameters(){
        double[] CPlParameters= new double[4];
        CPlParameters[0]= this.Al;
        CPlParameters[1]= this.Bl;
        CPlParameters[2]= this.Cl;
        CPlParameters[3]= this.Dl;
        return CPlParameters;
    }
    public double getHeatOfVaporization(){
        return this.HeatOfVaporization;
    }
    public String getName(){return this.Name;}
    public double getPhi_sat(){return this.Phi_sat;}
    public double getphi_v(){return this.Phi_v;}


    public boolean setParameters(double[] Parameters){
        if(Parameters==null) return false;
        if (Parameters.length!= 3) return false;
        this.a= Parameters[0];
        this.b= Parameters[1];
        this.c= Parameters[2];
        return true;
    }
    public boolean setCriticalParameters(double[] Parameters){
        if(Parameters==null) return false;
        if (Parameters.length!= 5) return false;
        this.Tc= Parameters[0];
        this.Vc= Parameters[1];
        this.Zc= Parameters[2];
        this.w= Parameters[3];
        this.Pc= Parameters[4];
        return true;
    }
    public boolean setCpvParameters(double[] Parameters){
        if(Parameters==null) return false;
        if (Parameters.length!= 4) return false;
        this.Av= Parameters[0];
        this.Bv= Parameters[1];
        this.Cv= Parameters[2];
        this.Dv= Parameters[3];
        return true;
    }
    public boolean setCplParameters(double[] Parameters){
        if(Parameters==null) return false;
        if (Parameters.length!= 4) return false;
        this.Al= Parameters[0];
        this.Bl= Parameters[1];
        this.Cl= Parameters[2];
        this.Dl= Parameters[3];
        return true;
    }
    public void setHeatOfVaporization(double heatOfVaporization){
        this.HeatOfVaporization = heatOfVaporization;
    }
    public void setName(String Name){
        this.Name= Name;
    }
    public void setPhi_sat( double Phi_sat){ this.Phi_sat= Phi_sat;}
    public void setPhi_v( double Phi_v){ this.Phi_v= Phi_v;}
}
