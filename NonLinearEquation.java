public interface NonLinearEquation {
    public double ReturnEquation(double X) throws ConvergenceNotMetException, NoRootFoundException;
}
