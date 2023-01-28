public class NonIdeal implements NonLinearEquation {

    public static double R = 8.314E-5; // because pressure is in bar
    public static double Maxit = 5000;
    public static double TOL = 0.00001;

    private FlowStream flowStream;

    public NonIdeal(FlowStream flowStream) {
        if (flowStream.getTemperature() > 0 && flowStream.getPressure() > 0 && flowStream.getFlowSpecies().size() > 0) {
            this.flowStream = flowStream;
        } else {
            System.out.println("Error: flow stream must contain a pressure, temperature, and at least one species!");
            System.exit(1);
        }
    }
    public NonIdeal(NonIdeal source){
        if (source.flowStream.clone().getTemperature() > 0 && source.flowStream.clone().getPressure() > 0 && source.flowStream.clone().getFlowSpecies().size() > 0) {
            this.flowStream = source.flowStream.clone();
        } else {
            System.out.println("Error: flow stream must contain a pressure, temperature, and at least one species!");
            System.exit(1);
        }
    }
    public NonIdeal clone(){
        return new NonIdeal(this);
    }
    public boolean equals (Object Comparator){
        if(!(this.getClass()==Comparator.getClass())) return false;
        if(!(this.flowStream==((NonIdeal) Comparator).flowStream)) return false;
        return true;
    }
    public FlowStream getFlowStream() {
        return this.flowStream;
    }

    public void setFlowStream(FlowStream newStream) {
        this.flowStream = new FlowStream(newStream);
    }


    public FlowStream solve() throws ConvergenceNotMetException, NoRootFoundException {

        //solving the flowstream using ideal assumptions
        IdealCase idealCase = new IdealCase(this.flowStream);
        FlowStream outlet = idealCase.solve(); // outlet is the solved flowstream
         // calculating Phi_sat
        int n = this.flowStream.getFlowSpecies().size(); // size of the inlet flowstream
        double[] b = new double[n]; // initiating the parameters to calculate b for each species
        double[] b0 = new double[n];
        double[] b1 = new double[n];
        double[] phi_sat = new double[n];
        double[] Tr = new double[n];
        double[] Tc = new double[n];
        double[] Pc = new double[n];
        double[] Wc = new double[n];
        double[] P_sat = new double[n];
        double T = this.flowStream.getTemperature();
        double P = this.flowStream.getPressure();

        for (int i = 0; i < n; i++) {
            Tc[i]=this.getFlowStream().getFlowSpecies().get(i).getCriticalParameters()[0]; //setting Tc
            Pc[i]= this.getFlowStream().getFlowSpecies().get(i).getCriticalParameters()[4]; //setting Pc
            Wc[i]= this.getFlowStream().getFlowSpecies().get(i).getCriticalParameters()[3]; // setting Wc
            P_sat[i]=IdealCase.SaturationPresurecalc(this.flowStream.getFlowSpecies().get(i),T); // setting Psat
            Tr[i] = T / Tc[i];// setting Tri
            b0[i] = 0.083 - 0.422 / Math.pow(Tr[i], 1.6); //setting b0i
            b1[i] = 0.139 - 0.173 / Math.pow(Tr[i], 4.2); // setting b1i
            b[i] = (R * Tc[i] / Pc[i]) * (b0[i] +Wc[i]* b1[i]); // setting B
            phi_sat[i] = Math.exp((b[i] * P_sat[i] )/ (R * T)); // setting phi_sat
            this.flowStream.getFlowSpecies().get(i).setPhi_sat(phi_sat[i]); // transferring phi_sat to the species
        }

        // calculating phi_v
            double[][] Tcij = new double[n][n];
            double[][] B0ij = new double[n][n];
            double[][] B1ij = new double[n][n];
            double[][] Bij = new double[n][n];
            double[][] Trij = new double[n][n];
            double[][] Vcij = new double[n][n];
            double[][] Zcij = new double[n][n];
            double[][] Wij = new double[n][n];
            double[][] Pcij = new double[n][n];
            double[][] deltaij = new double[n][n];

            // setting Tcij
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j) Tcij[i][j] = this.flowStream.getFlowSpecies().get(i).getCriticalParameters()[0];
                    else
                        Tcij[i][j] = Math.sqrt(this.flowStream.getFlowSpecies().get(i).getCriticalParameters()[0] * this.flowStream.getFlowSpecies().get(j).getCriticalParameters()[0]);
                }
            }
            //setting Wij
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j) Wij[i][j] = this.flowStream.getFlowSpecies().get(i).getCriticalParameters()[3];
                    else
                        Wij[i][j] = (this.flowStream.getFlowSpecies().get(i).getCriticalParameters()[3] + this.flowStream.getFlowSpecies().get(j).getCriticalParameters()[3]) / 2;
                }
            }
            //setting Vc
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j) Vcij[i][j] = this.flowStream.getFlowSpecies().get(i).getCriticalParameters()[1];
                    else
                        Vcij[i][j] = Math.pow((Math.cbrt(this.flowStream.getFlowSpecies().get(i).getCriticalParameters()[1]) + Math.cbrt(this.flowStream.getFlowSpecies().get(j).getCriticalParameters()[1])) / 2, 3);
                }
            }
            //setting Zc
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j) Zcij[i][j] = this.flowStream.getFlowSpecies().get(i).getCriticalParameters()[2];
                    else
                        Zcij[i][j] = (this.flowStream.getFlowSpecies().get(i).getCriticalParameters()[2] + this.flowStream.getFlowSpecies().get(j).getCriticalParameters()[2]) / 2;
                }
            }
            //setting Pc
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j) Pcij[i][j] = this.flowStream.getFlowSpecies().get(i).getCriticalParameters()[4];
                    else Pcij[i][j] = (Zcij[i][j] * R * Tcij[i][j]) / Vcij[i][j];
                }
            }
            //setting Tr
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j)
                        Trij[i][j] = T / this.flowStream.getFlowSpecies().get(i).getCriticalParameters()[0];
                    else Trij[i][j] = T / Tcij[i][j];
                }
            }
            //setting B0
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    B0ij[i][j] = 0.083 - 0.422 / Math.pow(Trij[i][j], 1.6);
                }
            }
            //setting B1
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    B1ij[i][j] = 0.139 - 0.173 / Math.pow(Trij[i][j], 4.2);
                }
            }
            //setting B
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    Bij[i][j] = R * Tcij[i][j] / Pcij[i][j] * (B0ij[i][j] + Wij[i][j] * B1ij[i][j]);
                }
            }
            // setting delta
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i == j) deltaij[i][j] = 0;
                    else deltaij[i][j] = 2 * Bij[i][j] - Bij[j][j];
                }
            }
            int iteration = 0; // counter for the do loop
            boolean Convergence = false; // checking for convergence;

            do {
                double[] lnphik = new double[n];
                double[] phik_v = new double[n];
                double[] yi = new double [n];
                double doublesum = 0;
                double sum_y = 0;
                double sum_x = 0;
                double VoverF, Zi, kMinusOne, Xi, Yi, Phi_sat, Phi_v;

                // calculating the double sum in the evil equation
                for (int k = 0; k < n; k++) {
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            yi[i]= outlet.getFlowSpecies().get(i).getVaporFraction();
                            yi[j] = outlet.getFlowSpecies().get(j).getVaporFraction();
                            doublesum += yi[i] * yi[j] * (2 * deltaij[i][k] - deltaij[i][j]);
                        }
                    }
                    //calculatting lnPhik
                    lnphik[k] = (P/ (R * T)) * (Bij[k][k] + 0.5 * doublesum);
                    //calculating Phik
                    phik_v[k] = Math.exp(lnphik[k]);
                    // transferring the value of phi_v in the species
                    outlet.getFlowSpecies().get(k).setPhi_v(phik_v[k]);
                    // resetting the doublesum
                    doublesum = 0;
                }
                // for these values of Phi_sat and Phi_v calculate a VoverF
                VoverF = RootFinder.RiddersMethod(this, 0,1);
                if (Double.isNaN(VoverF)) throw new NoRootFoundException("Error Not root found (Not a number)");
                outlet.setVaporFraction(VoverF);
                // using VoverF calculate kMinusOne and use to calculate new values of Xi and Yi
                for (int i = 0; i < n; i++) {
                    Zi = outlet.getFlowSpecies().get(i).getOverallFraction();
                    //getting phi_sat calculated previously
                    Phi_sat = outlet.getFlowSpecies().get(i).getPhi_sat();
                    //getting phi_v calculated for this iteration
                    Phi_v = outlet.getFlowSpecies().get(i).getphi_v();
                    //calculating kMinusOne
                    kMinusOne = ((P_sat[i] * Phi_sat) / (P * Phi_v)) - 1;
                    //Use it to calculate new value of Xi
                    Xi = Zi / (1 + VoverF * kMinusOne);
                    // update Xi in the species
                    outlet.getFlowSpecies().get(i).setLiquidFraction(Xi);
                    //calculate the sum of Xi
                    sum_x += Xi;
                    // do the same for Yi
                    Yi = (kMinusOne + 1) * Xi;
                    outlet.getFlowSpecies().get(i).setVaporFraction(Yi);
                    sum_y += Yi;
                }

                if (Math.abs(sum_x - sum_y) < TOL) Convergence = true; // make sure the sum of Xi and the sum of Yi is the same, if true, set convergence to true
                iteration++;// nex iteration
                sum_x=0;
                sum_y=0;
            }

            while (!Convergence && iteration < Maxit); // keep doing until convergence is true or iteration is more than Maxit
        if(!Convergence) throw new ConvergenceNotMetException("Error Convergence Not met");

        return outlet; // return the solved Flowstream
        }



        public double ReturnEquation ( double X){
            int i;
            double result = 0.0;
            double T = this.flowStream.getTemperature();
            double P = this.flowStream.getPressure();
            double Zi, P_sat, Phi_sat, Phi_v;
            double kMinusOne;

            for (i = 0; i < flowStream.getFlowSpecies().size(); i++) {

                Zi = this.flowStream.getFlowSpecies().get(i).getOverallFraction();
                P_sat = IdealCase.SaturationPresurecalc(flowStream.getFlowSpecies().get(i), T);
                Phi_sat = this.flowStream.getFlowSpecies().get(i).getPhi_sat();
                Phi_v = this.flowStream.getFlowSpecies().get(i).getphi_v();

                kMinusOne = ((P_sat * Phi_sat) / (P * Phi_v)) - 1;

                result += (Zi * kMinusOne) / (1 + X * kMinusOne);
            }
            return result;
        }
    }




