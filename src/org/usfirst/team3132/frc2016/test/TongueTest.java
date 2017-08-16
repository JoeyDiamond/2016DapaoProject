package org.usfirst.team3132.frc2016.test;

import org.usfirst.team3132.frc2016.subsystems.Shooter;
import org.usfirst.team3132.lib.Test;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class TongueTest implements Test{
	
	Shooter shooter = Shooter.getInstance();
	Joystick stick = new Joystick(0);
	Timer t = new Timer();
	
	double tongueSet = 0;

	@Override
	public void testInit() {
		shooter.enable();
		t.reset();
		t.start();
		
		
	}

	@Override
	public void testPeriodic() {
		if(stick.getRawButton(1)){
			tongueSet = -10;
		} else if(stick.getRawButton(2)){
			tongueSet = 10;
		} else if(stick.getRawButton(3)){
			tongueSet = 20;
		} else if(stick.getRawButton(4)){
			tongueSet = 45;
		} 
		
		if(stick.getRawButton(5)){
			tongueSet = 0;
		}
		
		
		shooter.setTongueDeg(tongueSet);
		
		System.out.println("t:	" + t.get() + 
				"	set:	" + shooter.getTongueTargetDeg() + 
				"	current:	" + shooter.getTongueCurrDeg() + 
				"	atSpeed:	" + shooter.isTongueInPosition());
		
	}

}
