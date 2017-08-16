package org.usfirst.team3132.frc2016.autonomous;

import org.usfirst.team3132.frc2016.GlobalSingleton;
import org.usfirst.team3132.frc2016.subsystems.Climber;
import org.usfirst.team3132.frc2016.subsystems.Drivebase;
import org.usfirst.team3132.frc2016.subsystems.Shooter;

import edu.wpi.first.wpilibj.DriverStation;

public abstract class AutonomousRoutine implements Runnable{
	
	GlobalSingleton globalSingleton = GlobalSingleton.getInstance();
	Drivebase drivebase = Drivebase.getInstance();
	Shooter shooter = Shooter.getInstance();
	Climber climber = Climber.getInstance();
	
	DriverStation ds = DriverStation.getInstance();
	
	/**
	 * put autonomous routine initialization code here
	 */
	public abstract void init();
	
	/**
	 * put autonomous routine code here
	 */
	public abstract void run();
	
	/**
	 * put code to abort or kill routine if necessary
	 */
	public void end(){
		drivebase.setThrottle(0.0, 0.0);
		shooter.setFlywheelTargetRPM(0.0);
	}
}
