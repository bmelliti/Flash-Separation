public class OptimizedT {

    public static void OptimizeT(double DewT, double BubbleT, String optmizedFlowspecies, double Tstep, FlowStream inletStream1 ) throws NoRootFoundException, ConvergenceNotMetException {

       // sets species name to lower case
        optmizedFlowspecies=optmizedFlowspecies.toLowerCase();
        int n = inletStream1.getFlowSpecies().size();
        //asking for the Ts checking the size of the Temper

            // ceil = number of iterations of the temperature
            double ceil = Math.ceil((DewT - BubbleT) / Tstep);
            double[] ti= new double[(int)ceil];
            double[] xi=new double[(int)ceil];
            double[] yi=new double[(int)ceil];
            // loop for the number of iterations
            for (int j = 0; j< xi.length; j++){
                // if j is in the end of the iterations: the Temperature is going to be DewT
                if (j== (int)ceil) ti[j]=DewT;
                // start T by the bubble temperature
                else ti[j]=BubbleT;
                //set the temperature

                double T =inletStream1.getTemperature();
                // solve the stream for that temperature
                if(inletStream1.getIsIdeal()){
                    inletStream1.setTemperature(ti[j]);
                    IdealCase idealCase = new IdealCase(inletStream1);
                    FlowStream outletStreamideal = idealCase.solve();
                    double x = inletStream1.getFlowSpecies().get(0).getLiquidFraction();// solved stream
                    // looping through all the species of the flowstream
                    for (int a = 0; a< n; a++){
                        // if the name is equal
                        String name =outletStreamideal.getFlowSpecies().get(a).getName().toLowerCase();
                        boolean check = outletStreamideal.getFlowSpecies().get(a).getName().toLowerCase().equals(optmizedFlowspecies);
                        if (check){
                            // populate Xi
                            // and Yi arrays for each iteration

                            xi[j]=outletStreamideal.getFlowSpecies().get(a).getLiquidFraction();
                            yi[j]=outletStreamideal.getFlowSpecies().get(a).getVaporFraction();
                        }
                    }
                }
                if(!inletStream1.getIsIdeal()){
                    inletStream1.setTemperature(ti[j]);
                    NonIdeal nonideal = new NonIdeal(inletStream1);
                    FlowStream outletStreamNonideal = nonideal.solve(); // solved stream
                    // looping through all the species of the flowstream
                    for (int a = 0; a< n; a++){
                        // if the name is equal
                        if (outletStreamNonideal.getFlowSpecies().get(a).getName().toLowerCase().equals(optmizedFlowspecies)){
                            // populate Xi and Yi arrays for each iteration
                            xi[j]=outletStreamNonideal.getFlowSpecies().get(a).getLiquidFraction();
                            yi[j]=outletStreamNonideal.getFlowSpecies().get(a).getVaporFraction();
                        }
                    }
                }
                // adding a Tstep to BubbleT each iteration
                    BubbleT+=Tstep;
                }
            // sorting Xi and Yi obtained for the previous calcs
                double Xi_max = 0;
                int Max_xi=0;
                for(int i= 0 ; i< xi.length; i++) {
                    if (i == 0) Xi_max = xi[i];
                    else if (xi[i] > xi[i - 1])
                        Xi_max = xi[i];
                        Max_xi = i;
                }

                    double Yi_max=0;
                    int Max_yi = 0;
                    for(int b= 0 ; b< xi.length; b++) {
                        if (b == 0) Yi_max = yi[b];
                        else if (yi[b] > yi[b - 1])
                            Yi_max = yi[b];
                            Max_yi = b;
                    }
                        if (Xi_max<Yi_max){
                            System.out.printf("The optimised separation for chemical: %s happens at temperature = %f [kelvin], the vapor fraction obtained is %f ",optmizedFlowspecies,ti[Max_yi],yi[Max_yi]);
                        }else System.out.printf("The optimised separation for chemical: %s happens at temperature = %f [kelvin], the liquid fraction obtained is %f ",optmizedFlowspecies,ti[Max_xi],xi[Max_xi]);

                    }
    }

