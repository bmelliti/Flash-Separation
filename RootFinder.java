public class RootFinder {
    public static double DeltaX=0.01;
    public static double Maxit= 5000;
    public static double TOL= 0.00001;

    public static double ReturnRootBound( NonLinearEquation E, double LowerLimit, double upperlimit) throws ConvergenceNotMetException, NoRootFoundException {
        double i= 0;
        double X=LowerLimit;
        boolean Rootbound = false;
        do {
            if (E.ReturnEquation(X) * E.ReturnEquation(X + DeltaX) < 0)
                Rootbound = true;
            else
                X = X + DeltaX;
            i++;
        }
        while ( ! Rootbound && i < Maxit && X<upperlimit);

        return X;
    }
    public static double RiddersMethod(NonLinearEquation E, double LowerLimit, double upperlimit) throws ConvergenceNotMetException, NoRootFoundException {
        double xM, xR_old, xR, error;
        double xL = ReturnRootBound(E,LowerLimit,upperlimit);
        double xU = xL + DeltaX;
        xR = 0;
        double fL, fU, fM, fR;
        int i = 0;
        do
        {
            xR_old = xR;
            xM = (xL + xU)/2.;
            fL = E.ReturnEquation(xL);
            fU = E.ReturnEquation(xU);
            fM = E.ReturnEquation(xM);
            xR = xM + (xM - xL)*(Math.signum(fL - fU)*fM)/(Math.sqrt(fM*fM - fL*fU));
            fR = E.ReturnEquation(xR);
            if (Double.isNaN(xR)) {
                return xR;
            }
            if(xR < xM)
            {
                if(fL*fR < 0) xU = xR;
                else if(fR*fM < 0)
                {
                    xL = xR;
                    xU = xM;
                }
                else xL = xR;
            }
            else
            {
                if(fL*fM < 0) xU = xM;
                else if(fM*fR < 0)
                {
                    xL = xM;
                    xU = xR;
                }
                else xL = xR;
            }
            error = Math.abs(xR - xR_old)/xR;
          i++;
        }
        while (error> TOL && i<Maxit);
        if (xR < 0) System.out.println("ERROR negative number");
        return xR;
    }
}
