package org.usfirst.team3132.frc2016.test;

import org.usfirst.team3132.frc2016.subsystems.Shooter;
import org.usfirst.team3132.lib.Test;

public class ShooterClassTest implements Test{

	Shooter shooter = Shooter.getInstance();
	
	@Override
	public void testInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testPeriodic() {
		// TODO Auto-generated method stub
		System.out.println("raw tongue: " + shooter.getTongueCurrRaw());
	}

}
