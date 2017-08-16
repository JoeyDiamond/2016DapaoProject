package org.usfirst.team3132.frc2016.test;

import org.usfirst.team3132.frc2016.subsystems.Climber;
import org.usfirst.team3132.lib.Test;

import edu.wpi.first.wpilibj.Timer;

public class ClimberTest implements Test{

	Climber climber = Climber.getInstance();
	Timer t = new Timer();
	
	@Override
	public void testInit() {
		t.reset();
		t.start();
		climber.enable();
		
		climber.extend();
	}

	@Override
	public void testPeriodic() {
		if(t.get() < 5){
			climber.extend();
			System.out.println("t: " + t.get() + "  Climber extended");
		} else if(t.get() < 10) {
			climber.retract();
			System.out.println("t: " + t.get() + "  Climber retracted");
		} else {
			t.reset();
		}
	}

}
