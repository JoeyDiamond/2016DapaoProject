package org.usfirst.team3132.frc2016.test;

import org.usfirst.team3132.lib.LogitechGamepadF310;
import org.usfirst.team3132.lib.Test;

import edu.wpi.first.wpilibj.Timer;

public class JoystickScalingTest implements Test{
	
	LogitechGamepadF310 gamepad = new LogitechGamepadF310(0);
	Timer t = new Timer();

	@Override
	public void testInit() {
		t.reset();
		t.start();
	}

	@Override
	public void testPeriodic() {
		System.out.println("t:	" + t.get() + 
				"	raw left y:	" + gamepad.getLeftY() + 
				"	scaled left y:	" + gamepad.getLeftYScaled());
		
	}
	
	
}
