package org.usfirst.team3132.frc2016.behaviors;

import org.usfirst.team3132.frc2016.GlobalSingleton;
import org.usfirst.team3132.lib.util.SystemUtil;

public class ShootFullPowerShot extends Action{

	GlobalSingleton globalSingleton = GlobalSingleton.getInstance();
	double prevTongueAngle = 0.0;
	
	@Override
	public void run() {
		globalSingleton.shooterAutomated = true;
		// turn on flywheel
		shooter.setFlywheelTargetRPM(200);
		
		// move tongue out of way
		prevTongueAngle = shooter.getTongueCurrDeg();
		shooter.setTongueDeg(0);
		
		// wait for things to get to speed/position
		while(!shooter.isReadyToShoot()){
			SystemUtil.fastLoopDelay();
		}
		
		// fire
		shooter.kickerExtend();
		SystemUtil.delay(200);
		shooter.kickerRetract();
		SystemUtil.delay(200);
		shooter.setFlywheelTargetRPM(0.0);
		
		// put tongue back if moved initially
		shooter.setTongueDeg(prevTongueAngle);
		
		globalSingleton.shooterAutomated = false;
	}
	
}
