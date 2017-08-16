package org.usfirst.team3132.frc2016.test;

import org.usfirst.team3132.lib.LogitechGamepadF310;
import org.usfirst.team3132.lib.Test;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Timer;

public class ShooterPIDTest implements Test {
	
	CANTalon flywheelMaster = new CANTalon(6);
	CANTalon flywheelSlave = new CANTalon(7);
	
	Timer t = new Timer();
	LogitechGamepadF310 stick = new LogitechGamepadF310(0);
	
	final double flywheelTolerance = 50;
	final int flywheelToleranceCount = 5;
	int flywheelToleranceCounter = 0;
	
	
	public ShooterPIDTest(){
		// flywheel slave setup
		flywheelSlave.changeControlMode(TalonControlMode.Follower);
		flywheelSlave.enableBrakeMode(false);
		flywheelSlave.set(6);
		
		// flywheel master setup
		flywheelMaster.changeControlMode(TalonControlMode.Speed);
		flywheelMaster.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		flywheelMaster.enableBrakeMode(false);
		flywheelMaster.configEncoderCodesPerRev(360);
		flywheelMaster.reverseSensor(true);
		flywheelMaster.reverseOutput(true);
		
		flywheelMaster.setPID(1.9, 0.00000001, 50);//0.000001
		flywheelMaster.setIZone(1);
		flywheelMaster.setF(0.1);
		flywheelMaster.configMaxOutputVoltage(12.0);
    	flywheelMaster.configNominalOutputVoltage(0.0, 0.0);
    	
	}
	
	double wheelSet = 0;
	
	public void testInit(){
		//System.out.flush();
		t.reset();
		t.start();
		//flywheelMaster.changeControlMode(TalonControlMode.PercentVbus);
		//wheelSet = 1;
		wheelSet = 1000;
	}
	
	public void testPeriodic(){
		if(stick.getBackButton()){
			flywheelMaster.changeControlMode(TalonControlMode.PercentVbus);
			wheelSet = 0.0;
		} else if(stick.getRedButton()){
			flywheelMaster.changeControlMode(TalonControlMode.Speed);
			wheelSet = 2000;
		} else if(stick.getYellowButton()){
			flywheelMaster.changeControlMode(TalonControlMode.Speed);
			wheelSet = 3000;
		} else if(stick.getBlueButton()){
			flywheelMaster.changeControlMode(TalonControlMode.Speed);
			wheelSet = 3500;
		} else if(stick.getGreenButton()){
			flywheelMaster.changeControlMode(TalonControlMode.Speed);
			wheelSet = 1000;
		}
		
		setFlywheelTargetRPM(wheelSet);
		System.out.println(t.get() + "	" + 
				getFlywheelTargetRPM() + "	" + 
				getFlywheelCurrentRPM() + "	" + 
				isFlywheelAtSpeed());
		/*System.out.println("P: " + flywheelMaster.getP() + 
				"	I:	" + flywheelMaster.getI() + 
				"	D:	" + flywheelMaster.getD() + 
				"	F:	" + flywheelMaster.getF());
		*/
	}
	
	public void setFlywheelTargetRPM(double speed){
		if(speed == 0){
			flywheelMaster.changeControlMode(TalonControlMode.PercentVbus);
			flywheelMaster.set(0.0);
			return;
		} else {
			flywheelMaster.changeControlMode(TalonControlMode.Speed);
			flywheelMaster.set(speed);// * 60.0);
		}
		
	}

	public boolean isFlywheelAtSpeed(){
		if(Math.abs(getFlywheelTargetRPM() - getFlywheelCurrentRPM()) < flywheelTolerance){
			flywheelToleranceCounter++;
		} else
			flywheelToleranceCounter = 0;
		
		return flywheelToleranceCounter >= flywheelToleranceCount;
	}
	
	public double getFlywheelTargetRPM(){
		return flywheelMaster.getSetpoint();// / 60;
	}
	
	public double getFlywheelCurrentRPM(){
		return flywheelMaster.getSpeed();// / 60;
	}
	
}
