public class FlowSpecies extends Species{

     private double OverallFraction, VaporFraction, LiquidFraction;

     public FlowSpecies(double OverallFraction,double VaporFraction, double LiquidFraction){
         super();
         this.OverallFraction = OverallFraction;
         this.VaporFraction = VaporFraction;
         this.LiquidFraction = LiquidFraction;

     }
     public FlowSpecies(FlowSpecies source){
         super();
         this.OverallFraction= source.OverallFraction;
         this.VaporFraction= source.VaporFraction;
         this.LiquidFraction= source.LiquidFraction;
     }


    public FlowSpecies clone(){
         return new FlowSpecies(this);
     }
    public boolean equals (Object Comparator){
        if(!(this.getClass()==Comparator.getClass())) return false;
        if(!(this.OverallFraction==((FlowSpecies) Comparator).OverallFraction)) return false;
        if(!(this.VaporFraction==((FlowSpecies) Comparator).VaporFraction)) return false;
        if(!(this.LiquidFraction==((FlowSpecies) Comparator).LiquidFraction)) return false;
        return true;
        }

     public void setOverallFraction(double overallFraction){
         this.OverallFraction = OverallFraction;
     }
     public void setVaporFraction(double vaporFraction){
         this.VaporFraction = vaporFraction;
     }
     public void setLiquidFraction(double liquidFraction){
         this.LiquidFraction= liquidFraction;
     }
     public double getOverallFraction(){
         return this.OverallFraction;
     }
     public double getVaporFraction(){
         return this.VaporFraction;
     }
     public double getLiquidFraction(){
         return this.LiquidFraction;
     }



}
