import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

public class UserInterface{
    public static void UI(FlowStream inletStream1) throws NoRootFoundException, ConvergenceNotMetException, FileNotFoundException {
        // initiate the scanner
        Scanner scan = new Scanner(System.in);
        //initiate the number formatter
        NumberFormat df = new DecimalFormat("#0.000");
        // number of the flow species
        System.out.println("Preset Inlet stream contains Ethane, Pentane, Hexane, Cyclohexane, Water and Nitrogen would you like to [a]dd d? if not press [d]one");
        char choice = scan.next().charAt(0);
        if(choice=='a'){
            FlowSpecies customspecies = new FlowSpecies(0.2,0.2,0);
            System.out.println("Enter the name of the species");
            String Name = scan.next();
            customspecies.setName(Name);
            double[] Antoine= new double[3];
            for(int i = 0; i<3; i++){
                System.out.println("Enter the Antoine constants for this species in order a, b and c:");
                Antoine[i]= scan.nextDouble();
            }
            customspecies.setParameters(Antoine);
            double[] CriticalParams= new double[5];
            for(int i = 0; i<5; i++){
                System.out.println("Enter the critical parameters for this species in order Tc, Vc, Zc, Wc and Pc:");
                CriticalParams[i]= scan.nextDouble();
            }
            customspecies.setCriticalParameters(CriticalParams);
            double[] CpvParams= new double[4];
            for(int i = 0; i<4; i++){
                System.out.println("Enter the Vapor heat Capacity parameters for this species in order A, B, C and D:");
                CpvParams[i]= scan.nextDouble();
            }
            customspecies.setCpvParameters(CpvParams);
            double[] CpLParams= new double[4];
            for(int i = 0; i<4; i++){
                System.out.println("Enter the Liquid heat Capacity parameters for this species in order C1, C2, C3 and C4:");
                CpLParams[i]= scan.nextDouble();
            }
            customspecies.setCplParameters(CpLParams);
            System.out.println("Enter the Heat of vaporization of this species");
            double HeatOfvap = scan.nextDouble();
            customspecies.setHeatOfVaporization(HeatOfvap);
            inletStream1.addFlowSpecies(customspecies);
        }

        int n = inletStream1.getFlowSpecies().size();
        //calculate the bubble point
        BubblePoint bubblePoint = new BubblePoint(inletStream1);
        double BubbleT = bubblePoint.BubbleTempcalc();
        //calculate the dew point
        DewPoint dewPoint = new DewPoint(inletStream1);
        double DewT = dewPoint.DewPointCalc();
        // asking for the Pressure
        System.out.println("Enter the Operating Pressure in Bar");
        double P = scan.nextDouble();
        // setting the Pressure
        inletStream1.setPressure(P);
        //asking for the molar rate
        System.out.println("Enter the molar Flow rate in Mol/s");
        double F = scan.nextDouble();
        //setting the molar rate
        inletStream1.setMolarFlowRate(F);
        // initiating the overall species
        double[] overallfractions = new double [n];
        // initiating the vapor fractions
        double[] Vaporfractions = new double [n];
        // initiating the names
        String[] Name = new String[n];
        for(int j= 0; j<n; j++) {
            Name[j]= inletStream1.getFlowSpecies().get(j).getName();
            //asking for the molar fractions of each species
            System.out.println("Enter the Molar fraction of Flow species\t"+Name[j]);
            overallfractions[j] = scan.nextDouble();
            //setting the fraction is the inlet stream
            inletStream1.getFlowSpecies().get(j).setOverallFraction(overallfractions[j]);
        }
         // asking if all flow species are in liquid phase
        System.out.println("Are all the species in the Flow stream in liquid phase? [y]es, [n]o");
        choice = scan.next().charAt(0);
        // if yest set vapor fraction to 0
        if(choice=='y')
            for(int j= 0; j<n; j++) {
                inletStream1.getFlowSpecies().get(j).setLiquidFraction(overallfractions[j]);
                inletStream1.getFlowSpecies().get(j).setVaporFraction(0);
            }
        // if no ask for vapor fractions and set them in species, liquid fractions will be Zi-Yi
        if(choice=='n')
            for(int j= 0; j<n; j++){
                System.out.println("Enter the Vapor fraction for Flow species\t"+Name[j]);
                Vaporfractions[j]= scan.nextDouble();
                inletStream1.getFlowSpecies().get(j).setVaporFraction(Vaporfractions[j]);
                inletStream1.getFlowSpecies().get(j).setLiquidFraction(overallfractions[j]-Vaporfractions[j]);
            }

        //ask ideal o non ideal
        System.out.println("Would like to solve using ideal assumptions? [y]es, [n]o ");
        choice = scan.next().charAt(0);
        if (choice== 'y')
            inletStream1.setIsIdeal(true);
        if(choice== 'n')
            inletStream1.setIsIdeal(false);

        // ask for the case
        System.out.println("What case would you like to solve for ? 1- Isothermal 2- Adiabatic 3- Optimization");
        int Case = scan.nextInt();
        switch (Case){
            case 1 :
                // ask for the operation T
                System.out.println("Enter the Operating Temperature in Kelvin\n");
                double T = scan.nextDouble();
                //check if separation is possible
                if (T<BubbleT || T>DewT) {
                    System.out.println("Separation is not possible Temperature is not within Bubble Temperature and Dew Temperature\n");
                    System.exit(1);
                }
                // if possible set the T
                else{
                inletStream1.setTemperature(T);}
                // if ideal
                if (inletStream1.getIsIdeal()){
                    IdealCase idealCase = new IdealCase(inletStream1);
                    //solve the flowstream
                    FlowStream outletStreamideal = idealCase.solve();
                    EnergyBalance ProcessedStream = new EnergyBalance(inletStream1,outletStreamideal);
                    //calculate Q
                    double Q = ProcessedStream.ReturnEquation(outletStreamideal.getTemperature());
                    System.out.println("The Vapor Flow rate is\t"+ df.format(outletStreamideal.getVaporFraction()*outletStreamideal.getMolarFlowRate()));
                    //display the vapor fraction of each species
                    for(int j= 0; j<n; j++) {
                        System.out.println("Vapor Fraction of \n"+outletStreamideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamideal.getFlowSpecies().get(j).getVaporFraction()));
                        System.out.println("Liquid Fraction of \n"+outletStreamideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamideal.getFlowSpecies().get(j).getLiquidFraction()));
                    }
                    //display the Q of each species
                    System.out.println("Amount of energy required to keep the Temperature Constant is\n"+df.format(Q));
                    //fileIO
                    System.out.println("Would you like these results to be printed on a file ? [y]es, [n]o");
                    choice = scan.next().charAt(0);
                    // if yes do it
                    if (choice== 'y') {
                        System.out.println("Enter a File name\n");
                        String filename = scan.next();
                        FileOutputStream fileOutputStream = new FileOutputStream(filename + "txt");
                        PrintWriter printWriter = new PrintWriter(fileOutputStream);
                        printWriter.println("The Vapor Flow rate is\n"+ df.format(outletStreamideal.getVaporFraction()*outletStreamideal.getMolarFlowRate()));
                        for(int j= 0; j<n; j++) {
                            printWriter.println("Vapor Fraction of  species:\n"+outletStreamideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamideal.getFlowSpecies().get(j).getVaporFraction()));
                            printWriter.println("Liquid Fraction of  species:\n"+outletStreamideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamideal.getFlowSpecies().get(j).getLiquidFraction()));
                        }
                        printWriter.println("Amount of energy required to keep the Temperature Constant is\n"+df.format(Q));
                        printWriter.close();
                    }
                    //if no exit
                    if(choice== 'n')
                        System.exit(1);
                }
                // if non ideal
                else{
                    NonIdeal NonidealCase = new NonIdeal(inletStream1);
                    //solve the flowstream
                    FlowStream outletStreamNonideal = NonidealCase.solve();
                    EnergyBalance ProcessedStream = new EnergyBalance(inletStream1,outletStreamNonideal);
                    //calculate Q
                    double Q = ProcessedStream.ReturnEquation(outletStreamNonideal.getTemperature());
                    //display the flow rates of the vapor phase
                    System.out.println("The Vapor Flow rate is\n"+ df.format(outletStreamNonideal.getVaporFraction()*outletStreamNonideal.getMolarFlowRate()));
                    //display the flow rates of the species
                    for(int j= 0; j<n; j++) {
                        System.out.println("Vapor Fraction of  species:\n"+outletStreamNonideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamNonideal.getFlowSpecies().get(j).getVaporFraction()));
                        System.out.println("Liquid Fraction of  species:\n"+outletStreamNonideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamNonideal.getFlowSpecies().get(j).getLiquidFraction()));
                    }
                    System.out.println("Amount of energy required to keep the Temperature Constant is\n"+df.format(Q));
                    //fileIO
                    System.out.println("Would you like these results to be printed on a file ? [y]es, [n]o");
                    choice = scan.next().charAt(0);

                    if (choice== 'y') {
                        System.out.println("Enter a File name\n");
                        String filename = scan.next();
                        FileOutputStream fileOutputStream = new FileOutputStream(filename + "txt");
                        PrintWriter printWriter = new PrintWriter(fileOutputStream);
                        printWriter.println("The Vapor Flow rate is\n"+ df.format(outletStreamNonideal.getVaporFraction()*outletStreamNonideal.getMolarFlowRate()));
                        for(int j= 0; j<n; j++) {
                            printWriter.println("Vapor Fraction of  species:\n"+outletStreamNonideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamNonideal.getFlowSpecies().get(j).getVaporFraction()));
                            printWriter.println("Liquid Fraction of  species:\n"+outletStreamNonideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamNonideal.getFlowSpecies().get(j).getLiquidFraction()));
                        }
                        printWriter.println("Amount of energy required to keep the Temperature Constant is\n"+df.format(Q));
                        printWriter.close();
                    }
                    if(choice== 'n')
                       System.exit(1);
                }
            case 2 :
                System.out.println("Enter the Inlet Temperature in Kelvin\n");
                double Tin = scan.nextDouble();
                // ask for the inlet T
                System.out.println("Enter the Operating Temperature in Kelvin\n");
                //check if separation is possible
                if (Tin<BubbleT || Tin>DewT) {
                    System.out.println("Separation is not possible Temperature is not within Bubble Temperature and Dew Temperature\n");
                    System.exit(1);
                }
                // if possible set the T
                else{
                    inletStream1.setTemperature(Tin);}

                if (inletStream1.getIsIdeal()){
                    IdealCase idealCase = new IdealCase(inletStream1);
                    FlowStream outletStreamideal = idealCase.solve();
                    EnergyBalance ProcessedStream = new EnergyBalance(inletStream1,outletStreamideal);
                    //solve for operation T necessary for adiabatic
                    double T_adiabatic = ProcessedStream.SolveT();
                    System.out.println("The Vapor Flow rate is\n"+ df.format(outletStreamideal.getVaporFraction()*outletStreamideal.getMolarFlowRate()));
                    for(int j= 0; j<n; j++) {
                        System.out.println("Vapor Fraction of each species:\n"+outletStreamideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamideal.getFlowSpecies().get(j).getVaporFraction()));
                        System.out.println("Liquid Fraction of each species:\n"+outletStreamideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamideal.getFlowSpecies().get(j).getLiquidFraction()));
                    }
                    System.out.println("The Temperature required for an Adiabatic Flash is\n"+df.format(T_adiabatic));
                    //fileIO
                    System.out.println("Would you like these results to be printed on a file ? [y]es, [n]o");
                    choice = scan.next().charAt(0);

                    if (choice== 'y') {
                        System.out.println("Enter a File name\n");
                        String filename = scan.next();
                        FileOutputStream fileOutputStream = new FileOutputStream(filename + "txt");
                        PrintWriter printWriter = new PrintWriter(fileOutputStream);
                        printWriter.println("The Vapor Flow rate is\n"+ df.format(outletStreamideal.getVaporFraction()*outletStreamideal.getMolarFlowRate()));
                        for(int j= 0; j<n; j++) {
                            printWriter.println("Vapor Fraction of  species:\n"+outletStreamideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamideal.getFlowSpecies().get(j).getVaporFraction()));
                            printWriter.println("Liquid Fraction of  species:\n"+outletStreamideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamideal.getFlowSpecies().get(j).getLiquidFraction()));
                        }
                        printWriter.println("The Temperature required for an Adiabatic Flash is\n"+df.format(T_adiabatic));
                        printWriter.close();
                    }
                    if(choice== 'n')
                        System.exit(1);
                }
                else{
                    NonIdeal NonidealCase = new NonIdeal(inletStream1);
                    FlowStream outletStreamNonideal = NonidealCase.solve();
                    EnergyBalance ProcessedStream = new EnergyBalance(inletStream1,outletStreamNonideal);
                    double T_adiabatic = ProcessedStream.SolveT();
                    System.out.println("The Vapor Flow rate is\n"+ df.format(outletStreamNonideal.getVaporFraction()*outletStreamNonideal.getMolarFlowRate()));
                    for(int j= 0; j<n; j++) {
                        System.out.println("Vapor Fraction of each species:\n"+outletStreamNonideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamNonideal.getFlowSpecies().get(j).getVaporFraction()));
                        System.out.println("Liquid Fraction of each species:\n"+outletStreamNonideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamNonideal.getFlowSpecies().get(j).getLiquidFraction()));
                    }
                    System.out.println("Amount of energy required to keep the Temperature Constant is\n"+df.format(T_adiabatic));
                    //fileIO
                    System.out.println("Would you like these results to be printed on a file ? [y]es, [n]o");
                    choice = scan.next().charAt(0);

                    if (choice== 'y') {
                        System.out.println("Enter a File name\n");
                        String filename = scan.next();
                        FileOutputStream fileOutputStream = new FileOutputStream(filename + "txt");
                        PrintWriter printWriter = new PrintWriter(fileOutputStream);
                        printWriter.println("The Vapor Flow rate is\n"+ df.format(outletStreamNonideal.getVaporFraction()*outletStreamNonideal.getMolarFlowRate()));
                        for(int j= 0; j<n; j++) {
                            printWriter.println("Vapor Fraction of  species:\n"+outletStreamNonideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamNonideal.getFlowSpecies().get(j).getVaporFraction()));
                            printWriter.println("Liquid Fraction of  species:\n"+outletStreamNonideal.getFlowSpecies().get(j).getName()+"\t is:\t"+df.format(outletStreamNonideal.getFlowSpecies().get(j).getLiquidFraction()));
                        }
                        printWriter.println("The Temperature required for an Adiabatic Flash is\n"+df.format(T_adiabatic));
                        printWriter.close();
                    }
                    if(choice== 'n')
                        System.exit(1);

                }
            case 3 :
                // ask the user for the Optimized species name
                String OptimizedFlowSpecies;
                System.out.println("Enter the name of the species to be optimized\n");

                OptimizedFlowSpecies = scan.next();

                //ask for the Tstep
               boolean Check=false;
                double tStep=0;
                while(!Check) {
                    System.out.println("Please enter the temperature step in Kelvin\n");
                     tStep= scan.nextDouble();
                    if (tStep > (DewT - BubbleT)) {
                        System.out.println("Temperature step is too big please enter a value smaller then (DewT - BubbleT)\n");
                    }
                    if (tStep < (DewT - BubbleT) / 100) {
                        System.out.println("Temperature step is too small please enter a value bigger than (DewT - BubbleT)/100\n");
                    }
                    else Check=true;
                }
                OptimizedT.OptimizeT(DewT,BubbleT,OptimizedFlowSpecies,tStep,inletStream1);

        }
        scan.close();
    }
}


