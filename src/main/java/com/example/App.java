package com.example;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
class InvalidViolationException extends Exception {
 public InvalidViolationException(String message) { super(message); }
}
class PaymentException extends Exception {
 public PaymentException(String message) { super(message); }
}
class Violation {
 private final String vehicleNumber, driverName, violationType, location;
 private final double fineAmount;
 private String status;
 public Violation(String vehicleNumber, String driverName,
 String violationType, double fineAmount, String location)
 throws InvalidViolationException {
 if (vehicleNumber == null || vehicleNumber.trim().isEmpty()
 || driverName == null || driverName.trim().isEmpty()
 || violationType == null || violationType.trim().isEmpty()
 || location == null || location.trim().isEmpty())
 throw new InvalidViolationException("Required violation information is missing");
 if (fineAmount <= 0)
 throw new InvalidViolationException("Fine amount must be greater than zero");
 this.vehicleNumber=vehicleNumber;
 this.driverName=driverName;
 this.violationType=violationType;
 this.fineAmount=fineAmount;
 this.location=location;
 this.status="Unpaid";
 }
 public String getVehicleNumber(){ return vehicleNumber; }
 public String getDriverName(){ return driverName; }
 public String getViolationType(){ return violationType; }
 public double getFineAmount(){ return fineAmount; }
 public String getLocation(){ return location; }
 public String getStatus(){ return status; }
 public void markPaid(){ status="Paid"; }
}
public class App {
 private final List<Violation> violations=new ArrayList<Violation>();
 private final List<String> history=new ArrayList<String>();
 public void addViolation(Violation violation){
 if(violation==null) throw new IllegalArgumentException("Violation cannot be null");
 violations.add(violation);
 history.add("E-Challan generated for "+violation.getVehicleNumber());
 }
 public List<Violation> getViolations(){
 return Collections.unmodifiableList(violations);
 }
 public List<String> getHistory(){
 return Collections.unmodifiableList(history);
 }
 public double calculateTotalFine(){
 double total=0.0;
 for(Violation v:violations)
 if("Unpaid".equals(v.getStatus())) total+=v.getFineAmount();
 return total;
 }
 public void payFine(String vehicleNumber) throws PaymentException{
 if(vehicleNumber==null || vehicleNumber.trim().isEmpty())
 throw new PaymentException("Vehicle number cannot be empty");
 for(Violation v:violations){
 if(v.getVehicleNumber().equalsIgnoreCase(vehicleNumber)
 && "Unpaid".equals(v.getStatus())){
 v.markPaid();
 history.add("Fine paid for "+v.getVehicleNumber());
 return;
 }
 }
 throw new PaymentException("No unpaid challan found for vehicle");
 }
 public List<Violation> sortByFineDescending(){
 List<Violation> result=new ArrayList<Violation>(violations);
 Collections.sort(result,new Comparator<Violation>(){
 public int compare(Violation a, Violation b){
 return Double.compare(b.getFineAmount(),a.getFineAmount());
 }
 });
 return result;
 }
 public static double calculateLateFee(double fineAmount,int lateDays)
 throws PaymentException{
 if(fineAmount<=0 || lateDays<0)
 throw new PaymentException("Invalid fine amount or late days");
 return fineAmount*0.02*lateDays;
 }
 public static void main(String[] args){
 App system=new App();
 Scanner scanner=new Scanner(System.in);
 System.out.println("===== TRAFFIC VIOLATION & E-CHALLAN SYSTEM =====");
 System.out.print("Enter Vehicle Number: ");
 String vehicleNumber=scanner.nextLine();
 System.out.print("Enter Driver Name: ");
 String driverName=scanner.nextLine();
 System.out.print("Enter Violation Type: ");
 String violationType=scanner.nextLine();
 System.out.print("Enter Fine Amount: ");
 double fineAmount=scanner.nextDouble();
 scanner.nextLine();
 System.out.print("Enter Location: ");
 String location=scanner.nextLine();
 try{
 Violation v=new Violation(vehicleNumber,driverName,violationType,fineAmount,location);
 system.addViolation(v);
 System.out.println("\n===== E-CHALLAN GENERATED =====");
 System.out.println("Vehicle Number : "+v.getVehicleNumber());
 System.out.println("Driver Name : "+v.getDriverName());
 System.out.println("Violation : "+v.getViolationType());
 System.out.println("Fine Amount : ■"+v.getFineAmount());
 System.out.println("Location : "+v.getLocation());
 System.out.println("Status : "+v.getStatus());
 System.out.println("Total Unpaid Fine: ■"+system.calculateTotalFine());
 }catch(InvalidViolationException e){
 System.out.println("ERROR: "+e.getMessage());
 }finally{
 scanner.close();
 }
 }
}
