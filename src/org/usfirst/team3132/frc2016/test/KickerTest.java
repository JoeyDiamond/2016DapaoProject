package org.usfirst.team3132.frc2016.test;

import org.usfirst.team3132.frc2016.subsystems.Shooter;
import org.usfirst.team3132.lib.Test;
import edu.wpi.first.wpilibj.Timer;

public class KickerTest implements Test{

	Shooter shooter = Shooter.getInstance();
	Timer t = new Timer();
	
	@Override
	public void testInit() {
		shooter.enable();
		System.out.flush();
		t.reset();
		t.start();
		shooter.kickerExtend();
		shooter.setTongueDeg(30);
	}

	@Override
	public void testPeriodic() {
		if(t.get() < 2){
			shooter.kickerExtend();
			System.out.println("Kicker Extended");
		} else if(t.get() < 4) {
			shooter.kickerRetract();
			System.out.println("Kicker Retracted");
		} else {
			t.reset();
		}
	}

}
