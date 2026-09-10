package com.example;
import org.junit.Test;
import static org.junit.Assert.*;
public class AppTest {
 @Test
 public void testValidViolation() throws Exception {
 Violation v=new Violation("TN01AB1234","Ravi","Speeding",1000.0,"Vellore");
 assertEquals("TN01AB1234",v.getVehicleNumber());
 assertEquals("Unpaid",v.getStatus());
 }
 @Test
 public void testTotalUnpaidFine() throws Exception {
 App s=new App();
 s.addViolation(new Violation("TN01AA1111","A","Speeding",1000.0,"Vellore"));
 s.addViolation(new Violation("TN01BB2222","B","Signal Jump",500.0,"Katpadi"));
 assertEquals(1500.0,s.calculateTotalFine(),0.001);
 }
 @Test
 public void testFinePayment() throws Exception {
 App s=new App();
 Violation v=new Violation("TN01CC3333","C","No Helmet",500.0,"Vellore");
 s.addViolation(v);
 s.payFine("TN01CC3333");
 assertEquals("Paid",v.getStatus());
 assertEquals(0.0,s.calculateTotalFine(),0.001);
 }
 @Test
 public void testSortByFineDescending() throws Exception {
 App s=new App();
 s.addViolation(new Violation("A","A","Speeding",500.0,"Vellore"));
 s.addViolation(new Violation("B","B","Dangerous Driving",2000.0,"Vellore"));
 assertEquals("B",s.sortByFineDescending().get(0).getVehicleNumber());
 }
 @Test
 public void testLateFee() throws Exception {
 assertEquals(100.0,App.calculateLateFee(1000.0,5),0.001);
 }
 @Test
 public void testMarkPaid() throws Exception {
 Violation v=new Violation("TN01DD4444","D","Parking",300.0,"Vellore");
 v.markPaid();
 assertEquals("Paid",v.getStatus());
 }
 @Test(expected=InvalidViolationException.class)
 public void testEmptyVehicleNumber() throws Exception {
 new Violation("","Ravi","Speeding",1000.0,"Vellore");
 }
 @Test(expected=InvalidViolationException.class)
 public void testNegativeFine() throws Exception {
 new Violation("TN01EE5555","E","Speeding",-100.0,"Vellore");
 }
 @Test
 public void testInvalidPayment() throws Exception {
 App s=new App();
 s.addViolation(new Violation("TN01FF6666","F","Speeding",1000.0,"Vellore"));
 try {
 s.payFine("TN01XX9999");
 fail("Expected PaymentException");
 } catch(PaymentException expected) {}
 }
}
