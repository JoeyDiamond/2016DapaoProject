package org.usfirst.team3132.frc2016.test;

import org.usfirst.team3132.frc2016.subsystems.Shooter;
import org.usfirst.team3132.lib.Test;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class FlywheelTest implements Test{

	Shooter shooter = Shooter.getInstance();
	Joystick stick = new Joystick(0);
	Timer t = new Timer();
	
	double wheelSet = 0;
	
	@Override
	public void testInit() {
		shooter.enable();
		t.reset();
		t.start();
		
	}

	@Override
	public void testPeriodic() {
		if(stick.getRawButton(1)){
			wheelSet = 50;
		} else if(stick.getRawButton(2)){
			wheelSet = 100;
		} else if(stick.getRawButton(3)){
			wheelSet = 150;
		} else if(stick.getRawButton(4)){
			wheelSet = 200;
		} 
		
		if(stick.getRawButton(5)){
			wheelSet = 0;
		}
		
		
		shooter.setFlywheelTargetRPM(wheelSet);
		
		System.out.println("t:	" + t.get() + 
				"	set:	" + shooter.getFlywheelTargetRPM() + 
				"	current:	" + shooter.getFlywheelCurrentRPM() + 
				"	atSpeed:	" + shooter.isFlywheelAtSpeed());
	}

}
